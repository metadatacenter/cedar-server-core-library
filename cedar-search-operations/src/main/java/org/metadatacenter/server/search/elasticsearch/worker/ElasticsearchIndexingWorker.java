package org.metadatacenter.server.search.elasticsearch.worker;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class ElasticsearchIndexingWorker {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexingWorker.class);

  private Client client;
  private String indexName;
  private String documentType;

  public IndexedDocumentId addToIndex(JsonNode json) throws CedarProcessingException {
    return addToIndex(json, null);
  }

  public IndexedDocumentId addToIndex(JsonNode json, IndexedDocumentId parent) throws CedarProcessingException {
    IndexedDocumentId newId = null;
    try {
      boolean again = true;
      int maxAttempts = 20;
      int count = 0;
      while (again) {
        try {
          IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, documentType)
              .setSource(JsonMapper.MAPPER.writeValueAsString(json), XContentType.JSON);
          if (parent != null && parent.getId() != null) {
            indexRequestBuilder.setParent(parent.getId());
          }
          IndexResponse response = indexRequestBuilder.get();
          if (response.status() == RestStatus.CREATED) {
            log.debug("The " + documentType + " has been indexed");
            again = false;
            newId = new IndexedDocumentId(response.getId());
          } else {
            throw new CedarProcessingException("Failed to index " + documentType);
          }
        } catch (NoNodeAvailableException e) {
          if (count++ > maxAttempts) {
            throw e;
          }
        }
      }
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
    return newId;
  }

  public void removeFromIndex(IndexedDocumentId indexedDocumentId) {
    log.debug("Removing " + documentType + " _id:" + indexedDocumentId.getId() + " from the index.");
    client.prepareDelete(indexName, documentType, indexedDocumentId.getId()).get();
  }
}
