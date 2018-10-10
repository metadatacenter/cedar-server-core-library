package org.metadatacenter.server.search.util;

import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.service.ElasticsearchManagementService;
import org.metadatacenter.server.search.elasticsearch.service.ElasticsearchServiceFactory;
import org.metadatacenter.server.search.elasticsearch.service.NodeIndexingService;
import org.metadatacenter.server.search.elasticsearch.service.NodeSearchingService;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.metadatacenter.constant.ElasticsearchConstants.DOCUMENT_CEDAR_ID;

public class RegenerateSearchIndexTask {

  private static final Logger log = LoggerFactory.getLogger(RegenerateSearchIndexTask.class);

  private final CedarConfig cedarConfig;


  public RegenerateSearchIndexTask(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
  }

  public void ensureSearchIndexExists() throws CedarProcessingException {
    IndexUtils indexUtils = new IndexUtils(cedarConfig);
    ElasticsearchServiceFactory esServiceFactory = ElasticsearchServiceFactory.getInstance(cedarConfig);
    ElasticsearchManagementService esManagementService = esServiceFactory.getManagementService();

    String indexName = cedarConfig.getElasticsearchConfig().getIndexName();
    int cedarIndexCount = 0;

    log.info("Looking for existing CEDAR indices...");
    List<String> indexNames = esManagementService.getAllIndices();
    log.info("Found total of " + indexNames.size() + " indices");
    for (String iName : indexNames) {
      log.info("Looking at index:" + iName);
      if (iName.startsWith(indexName)) {
        log.info("Found CEDAR index:" + iName);
        cedarIndexCount++;
      }
    }
    log.info("Found total of " + cedarIndexCount + " CEDAR indices");
    if (cedarIndexCount > 0) {
      log.info("Nothing to do!");
    } else {
      String newIndexName = indexUtils.getNewIndexName(indexName);
      log.info("Creating brand new CEDAR index:" + newIndexName);
      esManagementService.createIndex(ElasticsearchManagementService.IndexType.SEARCH, newIndexName);
      esManagementService.addAlias(newIndexName, indexName);
    }
  }

  public void regenerateSearchIndex(boolean force, CedarRequestContext requestContext) throws CedarProcessingException {
    IndexUtils indexUtils = new IndexUtils(cedarConfig);
    ElasticsearchServiceFactory esServiceFactory = ElasticsearchServiceFactory.getInstance(cedarConfig);
    ElasticsearchManagementService esManagementService = esServiceFactory.getManagementService();
    NodeSearchingService nodeSearchingService = esServiceFactory.nodeSearchingService();

    String indexName = cedarConfig.getElasticsearchConfig().getIndexName();

    boolean regenerate = true;
    try {
      PermissionServiceSession permissionSession = CedarDataServices.getPermissionServiceSession(requestContext);
      // Get all resources
      List<FolderServerNode> resources = indexUtils.findAllResources(requestContext);
      // Checks if is necessary to regenerate the index or not
      if (!force) {
        log.info("Checking if it is necessary to regenerate the search index from DB");
        // Check if the index exists (using the alias). If it exists, check if it contains all resources
        if (esManagementService.indexExists(indexName)) {
          // Use the resource ids to check if the resources in the DBs and in the index are different
          List<String> dbResourceIds = getResourceIds(resources);
          log.info("No. of nodes in DB that are expected to be indexed: " + dbResourceIds.size());
          List<String> indexResourceIds = nodeSearchingService.findAllValuesForField(DOCUMENT_CEDAR_ID);
          log.info("No. of content document in the index: " + indexResourceIds.size());
          if (dbResourceIds.size() == indexResourceIds.size()) {
            // Compare the two lists
            List<String> tmp1 = new ArrayList(dbResourceIds);
            List<String> tmp2 = new ArrayList(indexResourceIds);
            Collections.sort(tmp1);
            Collections.sort(tmp2);
            if (tmp1.equals(tmp2)) {
              regenerate = false;
              log.info("DB and search index match. It is not necessary to regenerate the index");
            } else {
              log.warn("DB and search index do not match! (different ids)");
            }
          } else {
            log.warn("DB and search index do not match! (different size)");
          }
        } else {
          log.warn("The search index '" + indexName + "' does not exist!");
        }
      }
      if (regenerate) {
        log.info("Regenerating index");
        // Create new index and set it up
        String newIndexName = indexUtils.getNewIndexName(indexName);
        esManagementService.createIndex(ElasticsearchManagementService.IndexType.SEARCH, newIndexName);

        NodeIndexingService nodeIndexingService = esServiceFactory.nodeIndexingService(newIndexName);

        // Get resources content and index it
        int count = 1;
        for (FolderServerNode node : resources) {
          try {
            CedarNodeMaterializedPermissions perm = null;
            if (node.getType() == CedarNodeType.FOLDER) {
              perm = permissionSession.getNodeMaterializedPermission(node.getId(), FolderOrResource.FOLDER);
            } else {
              perm = permissionSession.getNodeMaterializedPermission(node.getId(), FolderOrResource.RESOURCE);
            }
            IndexedDocumentId indexedNodeId = nodeIndexingService.indexDocument(node, perm);

            if (count % 100 == 0) {
              float progress = (100 * count++) / resources.size();
              log.info(String.format("Progress: %.0f%%", progress));
            }
          } catch (Exception e) {
            log.error("Error while indexing document: " + node.getId(), e);
          }
        }
        // Point alias to new index
        esManagementService.addAlias(newIndexName, indexName);
        // Delete any other index previously associated to the alias
        log.info("Looking for old indices...");
        List<String> indexNames = esManagementService.getAllIndices();
        log.info("Found " + indexNames.size());
        for (String iName : indexNames) {
          log.info("Found index:" + iName);
          if (iName.startsWith(indexName)) {
            if (!iName.equals(newIndexName)) {
              log.info("Deleting old index:" + iName);
              esManagementService.deleteIndex(iName);
            } else {
              log.info("Not deleting it, this was just generated");
            }
          } else {
            log.info("Not touching it, this is not ours");
          }
        }
      }
    } catch (Exception e) {
      log.error("Error while regenerating index", e);
      throw new CedarProcessingException(e);
    }
  }

  private List<String> getResourceIds(List<FolderServerNode> resources) {
    List<String> ids = new ArrayList<>();
    for (FolderServerNode resource : resources) {
      ids.add(resource.getId());
    }
    return ids;
  }

}
