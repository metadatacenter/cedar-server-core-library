package org.metadatacenter.server.search.elasticsearch.worker;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.search.IndexedDocumentType;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.metadatacenter.constant.ElasticsearchConstants.DOCUMENT_CEDAR_ID;

public class ElasticsearchIndexingWorker {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchIndexingWorker.class);

  private final Client client;
  private final String indexName;
  private final String documentType;

  public ElasticsearchIndexingWorker(String indexName, Client client) {
    this.client = client;
    this.indexName = indexName;
    this.documentType = IndexedDocumentType.DOC.getValue();
  }

  public IndexedDocumentId addToIndex(JsonNode json) throws CedarProcessingException {
    IndexedDocumentId newId = null;
    try {
      boolean again = true;
      int maxAttempts = 20;
      int count = 0;
      while (again) {
        try {
          IndexRequestBuilder indexRequestBuilder = client.prepareIndex(indexName, documentType)
              .setSource(JsonMapper.MAPPER.writeValueAsString(json), XContentType.JSON);
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

  public long removeAllFromIndex(String resourceId) throws CedarProcessingException {
    log.debug("Removing " + documentType + " cid:" + resourceId + " from the index.");
    try {
      // Get resources by resource id
      SearchResponse responseSearch = client.prepareSearch(indexName).setTypes(documentType)
          .setQuery(QueryBuilders.matchQuery(DOCUMENT_CEDAR_ID, resourceId))
          .execute().actionGet();

      long removedCount = 0;
      // Delete by Elasticsearch id
      for (SearchHit hit : responseSearch.getHits()) {
        String hitId = hit.getId();
        log.debug(("Try to delete " + documentType + " _id:" + hitId));
        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete(indexName, documentType, hit.getId());
        DeleteResponse responseDelete = deleteRequestBuilder.execute().actionGet();
        if (responseDelete.status() != RestStatus.OK) {
          throw new CedarProcessingException("Failed to remove " + documentType
              + " _id:" + hitId + " cid:" + resourceId + " from the index");
        } else {
          log.debug("The " + documentType + " " + hitId + " has been removed from the index");
          removedCount++;
        }
      }
      if (removedCount == 0) {
        log.error("The " + documentType + " cid:" + resourceId + " was not removed from the index.");
      } else {
        log.debug("Removed " + removedCount + " documents of type " + documentType + " cid:" + resourceId
            + " from the index.");
      }
      return removedCount;
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
  }

  public void removeFromIndex(IndexedDocumentId indexedDocumentId) {
    log.debug("Removing " + documentType + " _id:" + indexedDocumentId.getId() + " from the index.");
    client.prepareDelete(indexName, documentType, indexedDocumentId.getId()).get();
  }
}
