package org.metadatacenter.server.search.permission;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.server.search.elasticsearch.ElasticsearchService;
import org.metadatacenter.server.search.elasticsearch.IndexedDocumentId;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PermissionSearchService {

  private static final Logger log = LoggerFactory.getLogger(PermissionSearchService.class);

  private CedarConfig cedarConfig;
  private ElasticsearchService esService;
  private String esIndex;
  private String esType;

  public PermissionSearchService(CedarConfig cedarConfig, ElasticsearchService esService, String esIndex, String
      esType) {
    this.cedarConfig = cedarConfig;
    this.esService = esService;
    this.esIndex = esIndex;
    this.esType = esType;
  }

  public IndexedDocumentId indexResource(CedarNodeMaterializedPermissions permissions, String indexName,
                                         IndexedDocumentId parent) throws CedarProcessingException {
    if (permissions != null) {
      log.debug("Indexing permissions (id = " + permissions.getId() + ")");
      CedarPermissionIndexResource ir = new CedarPermissionIndexResource(permissions);
      JsonNode jsonResource = JsonMapper.MAPPER.convertValue(ir, JsonNode.class);
      return esService.addToIndex(jsonResource, indexName, esType, parent);
    }
    return null;
  }

  public IndexedDocumentId indexResource(CedarNodeMaterializedPermissions permissions, IndexedDocumentId parent) throws
      CedarProcessingException {
    return indexResource(permissions, esIndex, parent);
  }

  public void removeResourceFromIndex(String resourceId) throws CedarProcessingException {
    if (resourceId != null) {
      log.debug("Removing resource from index (id = " + resourceId);
      esService.removeFromIndex(resourceId, esIndex, esType);
    }
  }

  public IndexedDocumentId updateIndexedResource(CedarNodeMaterializedPermissions permissions, IndexedDocumentId
      parent) throws CedarProcessingException {
    if (permissions != null) {
      log.debug("Updating resource (id = " + permissions.getId());
      removeResourceFromIndex(permissions.getId());
      return indexResource(permissions, parent);
    }
    return null;
  }
}
