package org.metadatacenter.server.search.elasticsearch.worker;

import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.metadatacenter.constant.ElasticsearchConstants.ES_DOCUMENT_CEDAR_ID;

public class ContentIndexingWorker extends ElasticsearchIndexingWorker {

  private static final Logger log = LoggerFactory.getLogger(ContentIndexingWorker.class);

  public ContentIndexingWorker(String indexName, ElasticsearchConfig config, Client client, IndexedDocumentType
      indexedDocumentType) {
    this.client = client;
    this.indexName = indexName;
    this.documentType = config.getIndexes().getSearchIndex().getType(indexedDocumentType);
  }

  public long removeAllFromIndex(String resourceId, IndexedDocumentId parent) throws CedarProcessingException {
    log.debug("Removing " + documentType + " cid:" + resourceId + " from the index.");
    try {
      // Get resources by resource id
      SearchResponse responseSearch = client.prepareSearch(indexName).setTypes(documentType)
          .setQuery(QueryBuilders.matchQuery(ES_DOCUMENT_CEDAR_ID, resourceId))
          .execute().actionGet();

      long removedCount = 0;
      // Delete by Elasticsearch id
      for (SearchHit hit : responseSearch.getHits()) {
        String hitId = hit.getId();
        log.debug(("Try to delete " + documentType + " _id:" + hitId));
        DeleteRequestBuilder deleteRequestBuilder = client.prepareDelete(indexName, documentType, hit.getId());
        if (parent != null) {
          deleteRequestBuilder.setParent(parent.getId());
        }
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

}
