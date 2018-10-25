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

    String aliasName = cedarConfig.getElasticsearchConfig().getIndexes().getSearchIndex().getName();

    indexUtils.ensureIndexAndAliasExist(esManagementService, aliasName);
  }

  public void regenerateSearchIndex(boolean force, CedarRequestContext requestContext) throws CedarProcessingException {
    log.info("Regenerating search index. Force:" + force);

    IndexUtils indexUtils = new IndexUtils(cedarConfig);
    ElasticsearchServiceFactory esServiceFactory = ElasticsearchServiceFactory.getInstance(cedarConfig);
    ElasticsearchManagementService esManagementService = esServiceFactory.getManagementService();
    NodeSearchingService nodeSearchingService = esServiceFactory.nodeSearchingService();

    String aliasName = cedarConfig.getElasticsearchConfig().getIndexes().getSearchIndex().getName();

    boolean regenerate = true;
    try {
      PermissionServiceSession permissionSession = CedarDataServices.getPermissionServiceSession(requestContext);
      // Get all resources
      log.info("Reading all resources from the existing search index.");
      List<FolderServerNode> resources = indexUtils.findAllResources(requestContext);
      // Checks if is necessary to regenerate the index or not
      if (!force) {
        log.info("Force is false. Checking if it is necessary to regenerate the search index from Neo4j.");
        // Check if the index exists (using the alias). If it exists, check if it contains all resources
        if (esManagementService.indexExists(aliasName)) {
          log.warn("The search index/alias '" + aliasName + "' is present!");
          // Use the resource ids to check if the resources in the DBs and in the index are different
          List<String> dbResourceIds = getResourceIds(resources);
          log.info("No. of nodes in Neo4j that are expected to be indexed: " + dbResourceIds.size());
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
              log.info("Neo4j and search index match. It is not necessary to regenerate the index");
            } else {
              log.warn("Neo4j and search index do not match! (different ids)");
            }
          } else {
            log.warn("Neo4j and search index do not match! (different size)");
          }
        } else {
          log.warn("The search index/alias '" + aliasName + "' does not exist!");
        }
      } else {
        log.info("Force is true. It is not needed to compare the search index and Neo4j");
      }
      if (regenerate) {
        log.info("After all the checks were performed, it seems that the index needs to be regenerated!");
        // Create new index and set it up
        String newIndexName = indexUtils.getNewIndexName(aliasName);
        esManagementService.createSearchIndex(newIndexName);
        log.info("Search index created:" + newIndexName);

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
            count++;
          } catch (Exception e) {
            log.error("Error while indexing document: " + node.getId(), e);
          }
        }
        // Point alias to new index
        esManagementService.addAlias(newIndexName, aliasName);

        // Delete any other index previously associated to the alias
        indexUtils.deleteOldIndices(esManagementService, aliasName, newIndexName);
      } else {
        log.info(
            "After all the checks were performed, it seems that the index does not need to be regenerated this time.");
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
