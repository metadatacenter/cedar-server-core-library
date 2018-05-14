package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.Client;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerInstance;
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

import static org.metadatacenter.model.ModelNodeNames.SCHEMA_IS_BASED_ON;

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
    return indexNode(folder, null, context, parent, FolderOrResource.FOLDER);
  }

  public IndexedDocumentId indexResource(FolderServerNode resource, JsonNode resourceContent, CedarRequestContext
      context, IndexedDocumentId parent) throws CedarProcessingException {
    return indexNode(resource, resourceContent, context, parent, FolderOrResource.RESOURCE);
  }

  private IndexedDocumentId indexNode(FolderServerNode node, JsonNode resourceContent, CedarRequestContext
      context, IndexedDocumentId parent, FolderOrResource folderOrResource) throws CedarProcessingException {
    JsonNode summarizedContent = null;
    if (folderOrResource == FolderOrResource.RESOURCE) {
      if (resourceContent != null) {
        try {
          summarizedContent = indexUtils.extractSummarizedContent(node.getType(), resourceContent, context);
        } catch (Exception e) {
          log.error("There was an error while extracting summarized content", e);
        }
      }
      if (summarizedContent == null) {
        log.error("The resource indexing was cancelled, all related documents will be purged from the index (id = " +
            node.getId() + ")");
        return null;
      }
    }
    log.debug("Indexing resource (id = " + node.getId() + ")");
    // Set resource details
    String isBasedOn = null;
    if (CedarNodeType.INSTANCE.equals(node.getType())) {
      if (resourceContent != null) {
        JsonNode isBasedOnNode = resourceContent.get(SCHEMA_IS_BASED_ON);
        if (isBasedOnNode != null && !isBasedOnNode.isMissingNode()) {
          isBasedOn = isBasedOnNode.asText();
        }
      }
      if (isBasedOn == null) {
        log.error("Unable to determine templateId for instance:" + node.getId());
      }
      ((FolderServerInstance)node).setIsBasedOn1(isBasedOn);
    }
    IndexingDocumentContent ir = new IndexingDocumentContent(node, summarizedContent);
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

}
