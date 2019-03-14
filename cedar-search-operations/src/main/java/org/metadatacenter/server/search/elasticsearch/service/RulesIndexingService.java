package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.Client;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarBackendException;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.search.IndexedDocumentType;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchIndexingWorker;
import org.metadatacenter.server.search.util.IndexUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

import static org.metadatacenter.constant.ElasticsearchConstants.TEMPLATE_ID;

public class RulesIndexingService extends AbstractIndexingService {

  private static final Logger log = LoggerFactory.getLogger(RulesIndexingService.class);

  private final ElasticsearchIndexingWorker indexWorker;
  //private final IndexUtils indexUtils;

  RulesIndexingService(String indexName, Client client) {
    indexWorker = new ElasticsearchIndexingWorker(indexName, client);

    // If the cedar-value-recommender index does not exist, create it
//    ElasticsearchServiceFactory esServiceFactory = ElasticsearchServiceFactory.getInstance(cedarConfig);
//    ElasticsearchManagementService esManagementService = esServiceFactory.getManagementService();
//    String indexName = cedarConfig.getElasticsearchConfig().getIndexes().getValueRecommenderIndex().getName();
//    // Check if the index exists. If it does not exist, create it
//    if (!esManagementService.indexExists(indexName)) {
//      log.info("The " + indexName + " index does not exist. Creating it...");
//      esManagementService.createIndex(indexName, indexName);
//    }
  }

  public void indexRules(JsonNode rules, String templateId) throws CedarProcessingException {
    log.info("Indexing template rules for template: " + templateId);
    Iterator it = rules.iterator();
    while (it.hasNext()) {
      indexRule((JsonNode) it.next());
    }
  }

  public IndexedDocumentId indexRule(JsonNode rule) throws CedarProcessingException {
    return indexWorker.addToIndex(rule);
  }

  public long removeRulesFromIndex(String templateId) throws CedarProcessingException {
    if (templateId != null) {
      return indexWorker.removeAllFromIndex(TEMPLATE_ID, templateId);
    } else {
      throw new CedarProcessingException("templateId is null");
    }
  }


}
