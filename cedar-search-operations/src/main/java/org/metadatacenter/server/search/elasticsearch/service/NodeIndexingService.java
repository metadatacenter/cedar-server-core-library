package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.Client;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.document.IndexingDocumentNode;
import org.metadatacenter.server.search.elasticsearch.worker.ContentIndexingWorker;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchIndexingWorker;
import org.metadatacenter.util.StringUtil;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NodeIndexingService extends AbstractIndexingService {

  private static final Logger log = LoggerFactory.getLogger(NodeIndexingService.class);

  private final ContentIndexingWorker indexWorker;

  NodeIndexingService(String indexName, CedarConfig cedarConfig, Client client) {
    indexWorker = new ContentIndexingWorker(indexName, cedarConfig.getElasticsearchConfig(), client,
        IndexedDocumentType.NODE);
  }

  public IndexedDocumentId indexDocument(String resourceId, String name, CedarNodeType nodeType) throws CedarProcessingException {
    log.debug("Indexing node (id = " + resourceId + ")");
    IndexingDocumentNode ir = new IndexingDocumentNode(resourceId, StringUtil.comparisonValue(name), nodeType.getValue());
    JsonNode jsonResource = JsonMapper.MAPPER.convertValue(ir, JsonNode.class);
    return indexWorker.addToIndex(jsonResource, null);
  }

  public long removeDocumentFromIndex(String resourceId) throws CedarProcessingException {
    if (resourceId != null) {
      log.debug("Removing node from index (id = " + resourceId);
      return indexWorker.removeAllFromIndex(resourceId, null);
    } else {
      return -1;
    }
  }

  public void removeDocumentFromIndex(IndexedDocumentId indexedDocumentId) {
    log.debug("Removing node from index (_id = " + indexedDocumentId.getId());
    indexWorker.removeFromIndex(indexedDocumentId);
  }

}
