package org.metadatacenter.server.search.elasticsearch.worker;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortOrder;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.metadatacenter.constant.ElasticsearchConstants.*;

public class ElasticsearchSearchingWorker {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchSearchingWorker.class);

  private final Client client;
  private final String indexName;
  private final String documentType;
  private final ElasticsearchConfig config;
  private final TimeValue keepAlive;

  public ElasticsearchSearchingWorker(ElasticsearchConfig config, Client client, IndexedDocumentType
      indexedDocumentType) {
    this.config = config;
    this.client = client;
    this.indexName = config.getIndexName();
    this.documentType = config.getType(indexedDocumentType);
    this.keepAlive = new TimeValue(config.getScrollKeepAlive());
  }

  public SearchResponse search(String query, List<String> resourceTypes, List<String> sortList, String templateId,
                               int limit, int offset) throws CedarProcessingException {

    SearchRequestBuilder searchRequest = getSearchRequestBuilder(client, query, resourceTypes, sortList, templateId);

    // Set offset (from) and limit (size)
    searchRequest.setFrom(offset);
    searchRequest.setSize(limit);
    //System.out.println("Search query in Query DSL: " + searchRequest.internalBuilder());

    // Execute request
    SearchResponse response = searchRequest.execute().actionGet();
    return response;
  }

  // It uses the scroll API. It retrieves all results. No pagination and therefore no offset. Scrolling is not
  // intended for real time user requests, but rather for processing large amounts of data.
  // More info: https://www.elastic.co/guide/en/elasticsearch/reference/2.3/search-request-scroll.html
  public List<SearchHit> searchDeep(String query, List<String> resourceTypes, List<String> sortList, String templateId,
                                    int limit) throws CedarProcessingException {

    SearchRequestBuilder searchRequest = getSearchRequestBuilder(client, query, resourceTypes, sortList, templateId);

    // Set scroll and scroll size
    searchRequest.setScroll(TimeValue.timeValueMinutes(2));
    searchRequest.setSize(50000);

    //System.out.println("Search query in Query DSL: " + searchRequest.internalBuilder());

    // Execute request
    SearchResponse response = searchRequest.execute().actionGet();

    List<SearchHit> allHits = new ArrayList<>();

    while (response.getHits().hits().length != 0 || response.getHits().hits().length >= limit) {
      allHits.addAll(Arrays.asList(response.getHits().hits()));
      //next scroll
      response = client.prepareSearchScroll(response.getScrollId()).setScroll(TimeValue.timeValueMinutes(2))
          .execute().actionGet();
    }
    return allHits;
  }

  private SearchRequestBuilder getSearchRequestBuilder(Client client, String query, List<String> resourceTypes,
                                                       List<String> sortList, String templateId) {

    SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName).setTypes(documentType);

    // See: https://www.elastic.co/blog/better-query-execution-coming-elasticsearch-2-0
    BoolQueryBuilder mainQuery = QueryBuilders.boolQuery();

    // Search query
    if (query != null && query.length() > 0) {
      mainQuery.must(QueryBuilders.queryStringQuery(query).field(ES_RESOURCE_PREFIX + ES_RESOURCE_NAME_FIELD));
    } else {
      mainQuery.must(QueryBuilders.matchAllQuery());
    }

    // Filter by resource type
    BoolQueryBuilder resourceTypesQuery = QueryBuilders.boolQuery();
    if (resourceTypes != null && resourceTypes.size() > 0) {
      for (String rt : resourceTypes) {
        resourceTypesQuery.should(QueryBuilders.termQuery(ES_RESOURCE_PREFIX + ES_RESOURCE_RESOURCETYPE_FIELD, rt));
      }
    }
    mainQuery.must(resourceTypesQuery);

    // Filter by template id
    if (templateId != null) {
      mainQuery.must(QueryBuilders.matchQuery(ES_TEMPLATEID_FIELD, templateId));
    }

    // Set main query
    searchRequestBuilder.setQuery(mainQuery);

    // Sort by field
    if (sortList != null && sortList.size() > 0) {
      for (String s : sortList) {
        SortOrder sortOrder = SortOrder.ASC;
        if (s.startsWith(ES_SORT_DESC_PREFIX)) {
          sortOrder = SortOrder.DESC;
          s = s.substring(1);
        }
        String sortField = ES_RESOURCE_PREFIX + (s.compareTo(ES_RESOURCE_NAME_FIELD) == 0 ?
            ES_RESOURCE_SORTABLE_NAME_FIELD : s);
        searchRequestBuilder.addSort(sortField, sortOrder);
      }
    }
    return searchRequestBuilder;
  }


  // Retrieve all values for a fieldName. Dot notation is allowed (e.g. info.@id)
  public List<String> findAllValuesForField(String fieldName) throws CedarProcessingException {
    List<String> fieldValues = new ArrayList<>();
    QueryBuilder qb = QueryBuilders.matchAllQuery();
    SearchRequestBuilder searchRequest = client.prepareSearch(indexName).setTypes(documentType)
        .setFetchSource(new String[]{fieldName}, null)
        .setScroll(keepAlive).setQuery(qb).setSize(config.getSize());
    //System.out.println("Search query in Query DSL: " + searchRequest.internalBuilder());
    SearchResponse response = searchRequest.execute().actionGet();
    // Scroll until no hits are returned
    while (true) {
      for (SearchHit hit : response.getHits().getHits()) {
        Map<String, Object> f = hit.getSource();
        String[] pathFragments = fieldName.split("\\.");
        for (int i = 0; i < pathFragments.length - 1; i++) {
          f = (Map<String, Object>) f.get(pathFragments[0]);
        }
        String fieldValue = (String) f.get(pathFragments[pathFragments.length - 1]);
        fieldValues.add(fieldValue);
      }
      response = client.prepareSearchScroll(response.getScrollId()).setScroll(keepAlive)
          .execute().actionGet();
      // Break condition: No hits are returned
      if (response.getHits().getHits().length == 0) {
        break;
      }
    }
    return fieldValues;
  }


}
