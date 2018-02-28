package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.Client;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.document.IndexingDocumentUsers;
import org.metadatacenter.server.search.elasticsearch.worker.ContentIndexingWorker;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchIndexingWorker;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPermissionIndexingService extends AbstractIndexingService {

  private static final Logger log = LoggerFactory.getLogger(UserPermissionIndexingService.class);

  private final ContentIndexingWorker indexWorker;

  UserPermissionIndexingService(String indexName, CedarConfig cedarConfig, Client client) {
    indexWorker = new ContentIndexingWorker(indexName, cedarConfig.getElasticsearchConfig(), client,
        IndexedDocumentType.USERS);
  }

  public IndexedDocumentId indexDocument(CedarNodeMaterializedPermissions permissions, IndexedDocumentId parent)
      throws CedarProcessingException {
    if (permissions != null) {
      log.debug("Indexing user permissions (id = " + permissions.getId() + ")");
      IndexingDocumentUsers ir = new IndexingDocumentUsers(permissions);
      JsonNode jsonResource = JsonMapper.MAPPER.convertValue(ir, JsonNode.class);
      return indexWorker.addToIndex(jsonResource, parent);
    }
    return null;
  }

  public void removeDocumentFromIndex(String resourceId, IndexedDocumentId parent) throws CedarProcessingException {
    if (resourceId != null) {
      log.debug("Removing user permissions from index (id = " + resourceId);
      indexWorker.removeAllFromIndex(resourceId, parent);
    }
  }

  public IndexedDocumentId updateIndexedDocument(CedarNodeMaterializedPermissions permissions, IndexedDocumentId
      parent) throws CedarProcessingException {
    if (permissions != null) {
      log.debug("Updating user permissions (id = " + permissions.getId());
      removeDocumentFromIndex(permissions.getId(), parent);
      return indexDocument(permissions, parent);
    }
    return null;
  }
}
