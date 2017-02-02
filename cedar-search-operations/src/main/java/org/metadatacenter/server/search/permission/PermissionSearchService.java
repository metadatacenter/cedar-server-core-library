package org.metadatacenter.server.search.permission;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.server.search.elasticsearch.ElasticsearchService;
import org.metadatacenter.server.search.util.IndexUtils;
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


  public void indexResource(CedarNodeMaterializedPermissions permissions, String indexName, String documentType)
      throws CedarProcessingException {
    log.debug("Indexing permissions (id = " + permissions.getId() + ")");
    CedarPermissionIndexResource ir = new CedarPermissionIndexResource(permissions);
    JsonNode jsonResource = JsonMapper.MAPPER.convertValue(ir, JsonNode.class);
    esService.addToIndex(jsonResource, indexName, documentType);
  }

  public void indexResource(CedarNodeMaterializedPermissions permissions) throws
      CedarProcessingException {
    indexResource(permissions, esIndex, esType);
  }

  public void removeResourceFromIndex(String resourceId, String indexName, String documentType) throws
      CedarProcessingException {
    log.debug("Removing resource from index (id = " + resourceId);
    esService.removeFromIndex(resourceId, indexName, documentType);
  }

  public void removeResourceFromIndex(String resourceId) throws CedarProcessingException {
    removeResourceFromIndex(resourceId, esIndex, esType);
  }

  public void updateIndexedResource(CedarNodeMaterializedPermissions permissions, String indexName, String
      documentType) throws CedarProcessingException {
    log.debug("Updating resource (id = " + permissions.getId());
    removeResourceFromIndex(permissions.getId(), indexName, documentType);
    indexResource(permissions, indexName, documentType);
  }

  public void updateIndexedResource(CedarNodeMaterializedPermissions permissions) throws CedarProcessingException {
    updateIndexedResource(permissions, esIndex, esType);
  }
}
