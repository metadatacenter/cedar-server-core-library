package org.metadatacenter.server.search.elasticsearch.worker;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.metadatacenter.constant.ElasticsearchConstants.*;

public class ElasticsearchPermissionEnabledContentSearchingWorker {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchPermissionEnabledContentSearchingWorker.class);

  private final Client client;
  private final String indexName;
  private final CedarConfig cedarConfig;
  private final ElasticsearchConfig config;
  private final LinkedDataUtil linkedDataUtil;

  public ElasticsearchPermissionEnabledContentSearchingWorker(CedarConfig cedarConfig, Client client) {
    this.cedarConfig = cedarConfig;
    this.config = cedarConfig.getElasticsearchConfig();
    this.client = client;
    this.indexName = config.getIndexName();
    this.linkedDataUtil = cedarConfig.buildLinkedDataUtil();
  }

  public SearchResponseResult search(CedarRequestContext rctx, String query, List<String> resourceTypes, List<String>
      sortList, String templateId, int limit, int offset) throws CedarProcessingException {

    SearchRequestBuilder searchRequest = getSearchRequestBuilder(rctx, query, resourceTypes, sortList, templateId);

    searchRequest.setFrom(offset);
    searchRequest.setSize(limit);
    log.info("Search query in Query DSL: " + searchRequest.internalBuilder());

    // Execute request
    SearchResponse response = searchRequest.execute().actionGet();
    NodeIdResultList ids = new NodeIdResultList();
    ids.setTotalCount(response.getHits().getTotalHits());
    for (SearchHit hit : response.getHits()) {
      ids.addId(hit.getId());
    }

    return searchContentWithIds(ids);
  }

  // It uses the scroll API. It retrieves all results. No pagination and therefore no offset. Scrolling is not
  // intended for real time user requests, but rather for processing large amounts of data.
  // More info: https://www.elastic.co/guide/en/elasticsearch/reference/2.3/search-request-scroll.html
  public SearchResponseResult searchDeep(CedarRequestContext rctx, String query, List<String> resourceTypes, List<String>
      sortList, String templateId, int limit) throws CedarProcessingException {

    SearchRequestBuilder searchRequest = getSearchRequestBuilder(rctx, query, resourceTypes, sortList, templateId);

    // Set scroll and scroll size
    TimeValue timeout = TimeValue.timeValueMinutes(2);
    searchRequest.setScroll(timeout);
    searchRequest.setSize(100000);

    log.info("Search query in Query DSL: " + searchRequest.internalBuilder());

    // Execute request
    SearchResponse response = searchRequest.execute().actionGet();

    NodeIdResultList ids = new NodeIdResultList();
    ids.setTotalCount(response.getHits().getTotalHits());
    while (response.getHits().hits().length != 0 || response.getHits().hits().length >= limit) {
      for (SearchHit hit : response.getHits().hits()) {
        ids.addId(hit.getId());
      }
      //next scroll
      response = client.prepareSearchScroll(response.getScrollId()).setScroll(timeout)
          .execute().actionGet();
    }
    return searchContentWithIds(ids);
  }

  private SearchRequestBuilder getSearchRequestBuilder(CedarRequestContext rctx, String query, List<String>
      resourceTypes, List<String> sortList, String templateId) {

    SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName)
        .setTypes(IndexedDocumentType.NODE.getValue());

    BoolQueryBuilder mainQuery = QueryBuilders.boolQuery();

    String userId = linkedDataUtil.getLinkedDataId(CedarNodeType.USER, rctx.getCedarUser().getId());

    // Filter by user
    QueryBuilder userChildQuery = QueryBuilders.hasChildQuery(IndexedDocumentType.USERS.getValue(),
        QueryBuilders.termQuery("users.id", userId)
    );

    mainQuery.must(userChildQuery);

    BoolQueryBuilder contentQuery = QueryBuilders.boolQuery();

    // Filter by content
    if (query != null && query.length() > 0) {
      QueryBuilder contentNameQuery = QueryBuilders.queryStringQuery(query).field(ES_RESOURCE_PREFIX +
          ES_RESOURCE_NAME_FIELD);
      contentQuery.must(contentNameQuery);
    }

    // Filter by resource type
    if (resourceTypes != null && resourceTypes.size() > 0) {
      QueryBuilder resourceTypesQuery = QueryBuilders.termsQuery(ES_RESOURCE_PREFIX + ES_RESOURCE_RESOURCETYPE_FIELD,
          resourceTypes);
      contentQuery.must(resourceTypesQuery);
    }

    // Filter by template id
    if (templateId != null) {
      QueryBuilder templateIdQuery = QueryBuilders.matchQuery(ES_TEMPLATEID_FIELD, templateId);
      contentQuery.must(templateIdQuery);
    }

    QueryBuilder contentChildQuery = QueryBuilders.hasChildQuery(IndexedDocumentType.CONTENT.getValue(),
        contentQuery
    );
    mainQuery.must(contentChildQuery);

    // Set main query
    searchRequestBuilder.setQuery(mainQuery);

    // Sort by field
    // Sort is not supported in parent-child queries, only with score function
    /*if (sortList != null && sortList.size() > 0) {
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
    }*/
    return searchRequestBuilder;
  }

  private SearchResponseResult searchContentWithIds(NodeIdResultList ids) {
    QueryBuilder withParentIds = QueryBuilders.termsQuery("_parent", ids.getIds());
    SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName)
        .setTypes(IndexedDocumentType.CONTENT.getValue());
    searchRequestBuilder.setQuery(withParentIds);

    log.info("Search query in Query DSL: " + searchRequestBuilder.internalBuilder());

    return new SearchResponseResult(searchRequestBuilder.execute().actionGet(), ids.getTotalCount());
  }

}
