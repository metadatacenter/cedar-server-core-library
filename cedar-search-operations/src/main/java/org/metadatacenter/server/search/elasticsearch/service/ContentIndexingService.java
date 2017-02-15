package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.Client;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.document.IndexingDocumentContent;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchIndexingWorker;
import org.metadatacenter.server.search.util.IndexUtils;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContentIndexingService extends AbstractIndexingService {

  private static final Logger log = LoggerFactory.getLogger(ContentIndexingService.class);

  private final ElasticsearchIndexingWorker indexWorker;
  private final IndexUtils indexUtils;

  ContentIndexingService(String indexName, CedarConfig cedarConfig, Client client) {
    indexWorker = new ElasticsearchIndexingWorker(indexName, cedarConfig.getElasticsearchConfig(), client,
        IndexedDocumentType.CONTENT);
    indexUtils = new IndexUtils(cedarConfig);
  }

  public IndexedDocumentId indexFolder(FolderServerNode folder, CedarRequestContext context, IndexedDocumentId
      parent) throws CedarProcessingException {
    return indexNode(folder, null, context, parent);
  }

  public IndexedDocumentId indexResource(FolderServerNode resource, JsonNode resourceContent, CedarRequestContext
      context, IndexedDocumentId parent) throws CedarProcessingException {
    return indexNode(resource, resourceContent, context, parent);
  }

  private IndexedDocumentId indexNode(FolderServerNode node, JsonNode resourceContent, CedarRequestContext
      context, IndexedDocumentId parent) throws CedarProcessingException {
    JsonNode summarizedContent = null;
    if (resourceContent != null) {
      summarizedContent = indexUtils.extractSummarizedContent(node.getType(), resourceContent, context);
    }
    log.debug("Indexing resource (id = " + node.getId() + ")");
    // Set resource details
    String templateId = null;
    if (CedarNodeType.INSTANCE.equals(node.getType())) {
      if (resourceContent != null) {
        JsonNode isBasedOn = resourceContent.get("schema:isBasedOn");
        if (isBasedOn != null && !isBasedOn.isMissingNode()) {
          templateId = isBasedOn.asText();
        }
      }
      if (templateId == null) {
        log.error("Unable to determine templateId for instance:" + node.getId());
      }
    }
    //TODO: Is this setResourceDetails still needed???
    //resource = setResourceDetails(resource);
    IndexingDocumentContent ir = new IndexingDocumentContent(node, summarizedContent, templateId);
    JsonNode jsonResource = JsonMapper.MAPPER.convertValue(ir, JsonNode.class);
    return indexWorker.addToIndex(jsonResource, parent);
  }

  public void removeDocumentFromIndex(String resourceId, IndexedDocumentId parent) throws CedarProcessingException {
    if (resourceId != null) {
      log.debug("Removing content from index (id = " + resourceId);
      indexWorker.removeAllFromIndex(resourceId, parent);
    }
  }

  public IndexedDocumentId updateResource(FolderServerResource resource, JsonNode resourceContent,
                                          CedarRequestContext context, IndexedDocumentId parent) throws
      CedarProcessingException {
    log.debug("Updating content (id = " + resource.getId());
    removeDocumentFromIndex(resource.getId(), parent);
    return indexResource(resource, resourceContent, context, parent);
  }

  public IndexedDocumentId updateFolder(FolderServerFolder folder, CedarRequestContext context, IndexedDocumentId
      parent) throws CedarProcessingException {
    log.debug("Updating content (id = " + folder.getId());
    removeDocumentFromIndex(folder.getId(), parent);
    return indexFolder(folder, context, parent);
  }

  /**
   * PRIVATE METHODS
   */

  /*private FolderServerNode setResourceDetails(FolderServerNode resource) {
    resource.setDisplayName(resource.getName());
    return resource;
  }*/
}
