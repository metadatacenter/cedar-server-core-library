package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.Client;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarBackendException;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchIndexingWorker;
import org.metadatacenter.server.search.elasticsearch.worker.ValueRecommenderIndexingWorker;
import org.metadatacenter.server.search.util.IndexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class ValueRecommenderIndexingService extends AbstractIndexingService {

  private static final Logger log = LoggerFactory.getLogger(ValueRecommenderIndexingService.class);

  private final ValueRecommenderIndexingWorker indexWorker;
  private final IndexUtils indexUtils;

  ValueRecommenderIndexingService(CedarConfig cedarConfig, Client client) throws CedarProcessingException {
    indexWorker = new ValueRecommenderIndexingWorker(cedarConfig.getElasticsearchConfig(), client,
        IndexedDocumentType.RULES_DOC);
    indexUtils = new IndexUtils(cedarConfig);

    // If the cedar-value-recommender index does not exist, create it
    ElasticsearchServiceFactory esServiceFactory = ElasticsearchServiceFactory.getInstance(cedarConfig);
    ElasticsearchManagementService esManagementService = esServiceFactory.getManagementService();
    String indexName = cedarConfig.getElasticsearchConfig().getIndexes().getValueRecommenderIndex().getName();
    // Check if the index exists. If it does not exist, create it
    if (!esManagementService.indexExists(indexName)) {
      log.info("The " + indexName + " index does not exist. Creating it...");
      esManagementService.createIndex(indexName, indexName);
    }
  }

  public void indexTemplateRules(JsonNode rules, String templateId) throws CedarProcessingException {
    log.info("Indexing template rules for template: " + templateId);
    Iterator it = rules.iterator();
    while (it.hasNext()) {
      indexTemplateRule((JsonNode) it.next());
    }
  }

  public IndexedDocumentId indexTemplateRule(JsonNode rule) throws CedarProcessingException {
    return indexWorker.addToIndex(rule);
  }

  public long removeTemplateRulesFromIndex(String templateId) throws CedarProcessingException {
    if (templateId != null) {
      return indexWorker.removeAllFromIndex(templateId);
    } else {
      throw new CedarProcessingException("TemplateId is null");
    }
  }


}
