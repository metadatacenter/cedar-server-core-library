package org.metadatacenter.server.search.elasticsearch.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.document.IndexedDocumentNode;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchSearchingWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

import static org.metadatacenter.constant.ElasticsearchConstants.ES_DOCUMENT_CEDAR_ID;
import static org.metadatacenter.constant.ElasticsearchConstants.ES_RESOURCE_NAME_FIELD;
import static org.metadatacenter.constant.ElasticsearchConstants.ES_RESOURCE_RESOURCETYPE_FIELD;

public class NodeSearchingService extends AbstractSearchingService {

  private static final Logger log = LoggerFactory.getLogger(NodeSearchingService.class);

  private final Client client;
  private final ElasticsearchConfig config;
  private final ElasticsearchSearchingWorker searchWorker;

  NodeSearchingService(CedarConfig cedarConfig, Client client) {
    this.client = client;
    this.config = cedarConfig.getElasticsearchConfig();
    searchWorker = new ElasticsearchSearchingWorker(cedarConfig.getElasticsearchConfig(), client, IndexedDocumentType
        .NODE);
  }

  public IndexedDocumentId getByCedarId(String resourceId) throws CedarProcessingException {
    return getByCedarId(client, resourceId, config.getIndexes().getSearchIndex().getName(), config
        .getIndexes().getSearchIndex().getType(IndexedDocumentType.NODE));
  }

  public IndexedDocumentNode getDocumentByCedarId(String resourceId) throws CedarProcessingException {
    try {
      // Get resources by resource id
      SearchResponse responseSearch = client.prepareSearch(config.getIndexes().getSearchIndex().getName())
          .setTypes(config.getIndexes().getSearchIndex().getType
              (IndexedDocumentType.NODE)).setQuery(QueryBuilders.matchQuery(ES_DOCUMENT_CEDAR_ID, resourceId))
          .execute().actionGet();
      for (SearchHit hit : responseSearch.getHits()) {
        if (hit != null) {
          Map<String, Object> map = hit.getSourceAsMap();
          return new IndexedDocumentNode(
              hit.getId(),
              (String) map.get(ES_DOCUMENT_CEDAR_ID),
              (String) map.get(ES_RESOURCE_NAME_FIELD),
              (String) map.get(ES_RESOURCE_RESOURCETYPE_FIELD));
        }
      }
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
    return null;
  }

  public List<String> findAllValuesForField(String fieldName) throws CedarProcessingException {
    return searchWorker.findAllValuesForField(fieldName);
  }

}
