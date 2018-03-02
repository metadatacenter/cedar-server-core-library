package org.metadatacenter.server.search.elasticsearch.worker;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.metadatacenter.constant.ElasticsearchConstants.ES_TEMPLATEID_FIELD;

public class ValueRecommenderIndexingWorker extends ElasticsearchIndexingWorker {

  private static final Logger log = LoggerFactory.getLogger(ValueRecommenderIndexingWorker.class);

  public ValueRecommenderIndexingWorker(ElasticsearchConfig config, Client client, IndexedDocumentType
      indexedDocumentType) {
    this.client = client;
    this.indexName = config.getIndexes().getValueRecommenderIndex().getName();
    this.documentType = config.getIndexes().getValueRecommenderIndex().getType(indexedDocumentType);
  }

  public long removeAllFromIndex(String templateId) throws CedarProcessingException {
    BulkByScrollResponse response =
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
            .filter(QueryBuilders.matchQuery(ES_TEMPLATEID_FIELD, templateId))
            .source(indexName).get();

    long removedCount = response.getDeleted();
    return removedCount;
  }

}
