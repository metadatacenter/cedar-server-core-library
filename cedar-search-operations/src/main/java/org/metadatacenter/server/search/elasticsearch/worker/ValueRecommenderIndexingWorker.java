package org.metadatacenter.server.search.elasticsearch.worker;

import org.elasticsearch.client.Client;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ValueRecommenderIndexingWorker extends ElasticsearchIndexingWorker {

  private static final Logger log = LoggerFactory.getLogger(ValueRecommenderIndexingWorker.class);

  private final Client client;
  private final String indexName;
  private final String documentType;

  public ValueRecommenderIndexingWorker(ElasticsearchConfig config, Client client, IndexedDocumentType
      indexedDocumentType) {
    this.client = client;
    this.indexName = config.getIndexes().getValueRecommenderIndex().getName();
    this.documentType = config.getIndexes().getValueRecommenderIndex().getType(indexedDocumentType);
  }

}
