package org.metadatacenter.server.search.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.service.*;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.metadatacenter.constant.ElasticsearchConstants.ES_DOCUMENT_CEDAR_ID;

public class RegenerateSearchIndexTask {

  private static final Logger log = LoggerFactory.getLogger(RegenerateSearchIndexTask.class);

  private CedarConfig cedarConfig;


  public RegenerateSearchIndexTask(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
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
          List<String> indexResourceIds = nodeSearchingService.findAllValuesForField(ES_DOCUMENT_CEDAR_ID);
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
        esManagementService.createIndex(newIndexName);

        NodeIndexingService nodeIndexingService = esServiceFactory.nodeIndexingService(newIndexName);
        ContentIndexingService contentIndexingService = esServiceFactory.contentIndexingService(newIndexName);
        UserPermissionIndexingService userPermissionIndexingService = esServiceFactory.userPermissionsIndexingService
            (newIndexName);
        GroupPermissionIndexingService groupPermissionIndexingService = esServiceFactory
            .groupPermissionsIndexingService(newIndexName);

        // Get resources content and index it
        int count = 1;
        for (FolderServerNode resource : resources) {
          try {
            CedarNodeMaterializedPermissions perm = null;
            IndexedDocumentId indexedNodeId = nodeIndexingService.indexDocument(resource.getId());
            if (resource.getType() == CedarNodeType.FOLDER) {
              contentIndexingService.indexFolder(resource, requestContext, indexedNodeId);
              perm = permissionSession.getNodeMaterializedPermission(resource.getId(), FolderOrResource.FOLDER);
            } else {
              JsonNode resourceContent = indexUtils.findResourceContent(resource.getId(), resource.getType(),
                  requestContext);
              if (resourceContent != null) {
                contentIndexingService.indexResource(resource, resourceContent, requestContext, indexedNodeId);
                perm = permissionSession.getNodeMaterializedPermission(resource.getId(), FolderOrResource.RESOURCE);
              }
            }
            if (perm != null) {
              userPermissionIndexingService.indexDocument(perm, indexedNodeId);
              groupPermissionIndexingService.indexDocument(perm, indexedNodeId);
            } else {
              log.error("Permissions not found for " + resource.getType() + ":" + resource.getId());
            }
            float progress = (100 * count++) / resources.size();
            log.info(String.format("Progress: %.0f%%", progress));
          } catch (Exception e) {
            log.error("Error while indexing docvument", e);
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
