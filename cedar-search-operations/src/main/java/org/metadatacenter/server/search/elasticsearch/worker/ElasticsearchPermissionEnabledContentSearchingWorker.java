package org.metadatacenter.server.search.elasticsearch.worker;

import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.join.ScoreMode;
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
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.search.IndexedDocumentType;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.auth.NodePermission;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.metadatacenter.constant.ElasticsearchConstants.*;

public class ElasticsearchPermissionEnabledContentSearchingWorker {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchPermissionEnabledContentSearchingWorker.class);

  private final Client client;
  private final String indexName;

  public ElasticsearchPermissionEnabledContentSearchingWorker(ElasticsearchConfig config, Client client) {
    this.client = client;
    this.indexName = config.getIndexes().getSearchIndex().getName();
  }

  public SearchResponseResult search(CedarRequestContext rctx, String query, List<String> resourceTypes,
                                     ResourceVersionFilter version, ResourcePublicationStatusFilter
                                         publicationStatus, List<String> sortList, int limit, int offset) throws CedarProcessingException {

    SearchRequestBuilder searchRequest = getSearchRequestBuilder(rctx, query, resourceTypes, version,
        publicationStatus, sortList);

    searchRequest.setFrom(offset);
    searchRequest.setSize(limit);

    // Execute request
    SearchResponse response = searchRequest.execute().actionGet();

    SearchResponseResult result = new SearchResponseResult();
    result.setTotalCount(response.getHits().getTotalHits());

    for (SearchHit hit : response.getHits()) {
      result.add(hit);
    }

    return result;
  }

  // It uses the scroll API. It retrieves all results. No pagination and therefore no offset. Scrolling is not
  // intended for real time user requests, but rather for processing large amounts of data.
  // More info: https://www.elastic.co/guide/en/elasticsearch/reference/2.3/search-request-scroll.html
  public SearchResponseResult searchDeep(CedarRequestContext rctx, String query, List<String> resourceTypes,
                                         ResourceVersionFilter version, ResourcePublicationStatusFilter
                                             publicationStatus, List<String> sortList, int limit, int offset) throws CedarProcessingException {

    SearchRequestBuilder searchRequest = getSearchRequestBuilder(rctx, query, resourceTypes, version,
        publicationStatus, sortList);

    // Set scroll and scroll size
    TimeValue timeout = TimeValue.timeValueMinutes(2);
    searchRequest.setScroll(timeout);
    searchRequest.setSize(offset + limit);

    //log.debug("Search query in Query DSL: " + searchRequest);

    // Execute request
    SearchResponse response = searchRequest.execute().actionGet();

    SearchResponseResult result = new SearchResponseResult();
    result.setTotalCount(response.getHits().getTotalHits());

    int counter = 0;
    while (response.getHits().getHits().length != 0) {
      for (SearchHit hit : response.getHits().getHits()) {
        if (counter >= offset && counter < offset + limit) {
          result.add(hit);
        }
        counter++;
      }
      //next scroll
      response = client.prepareSearchScroll(response.getScrollId()).setScroll(timeout).execute().actionGet();
    }
    return result;
  }

  private SearchRequestBuilder getSearchRequestBuilder(CedarRequestContext rctx, String query,
                                                       List<String> resourceTypes, ResourceVersionFilter version,
                                                       ResourcePublicationStatusFilter publicationStatus,
                                                       List<String> sortList) throws CedarProcessingException {

    SearchRequestBuilder searchRequestBuilder = client.prepareSearch(indexName)
        .setTypes(IndexedDocumentType.DOC.getValue());

    BoolQueryBuilder mainQuery = QueryBuilders.boolQuery();

    // Preprocess query to avoid syntax errors when using the query String syntax
    query = preprocessQuery(query);

    // Query artifact id and description (summaryText). Sample query: 'cancer'
    if (!query.contains(":")) {
      if (enclosedByQuotes(query)) {
        query = query.substring(1, query.length() - 1);
        QueryBuilder summaryTextQuery = QueryBuilders.matchPhraseQuery(SUMMARY_RAW_TEXT, query);
        mainQuery.must(summaryTextQuery);
      } else {
        QueryBuilder summaryTextQuery = QueryBuilders.queryStringQuery(query).field(SUMMARY_TEXT);
        mainQuery.must(summaryTextQuery);
      }
    }
    // Query field name/value (infoFields), optionally combined with artifact id and description (summaryText).
    // Sample query: 'disease:cancer AND Template3'
    else {
      // Parse the query using the query parser and rewrite it to query the right index fields. The whitespace
      // analyzer divides text into terms whenever it encounters any whitespace character. It does not lowercase terms.
      QueryParser parser = new QueryParser("", new WhitespaceAnalyzer());
      try {
        Query queryParsed = parser.parse(query);
        mainQuery.must(rewriteQuery(queryParsed));
      } catch (ParseException e) {
        throw new CedarProcessingException("Error processing query: " + query, e);
      }
    }

    String userId = rctx.getCedarUser().getId();
    if (!rctx.getCedarUser().has(CedarPermission.READ_NOT_READABLE_NODE)) {
      // Filter by user
      QueryBuilder userIdQuery = QueryBuilders.termQuery(USERS, CedarNodeMaterializedPermissions.getKey(userId,
          NodePermission.READ));
      BoolQueryBuilder permissionQuery = QueryBuilders.boolQuery();

      QueryBuilder everybodyReadQuery =
          QueryBuilders.termsQuery(INFO_EVERYBODY_PERMISSION, NodeSharePermission.READ.getValue());
      QueryBuilder everybodyWriteQuery =
          QueryBuilders.termsQuery(INFO_EVERYBODY_PERMISSION, NodeSharePermission.WRITE.getValue());

      permissionQuery.should(userIdQuery);
      permissionQuery.should(everybodyReadQuery);
      permissionQuery.should(everybodyWriteQuery);
      mainQuery.must(permissionQuery);
    }

    // Filter by resource type
    if (resourceTypes != null && resourceTypes.size() > 0) {
      QueryBuilder resourceTypesQuery = QueryBuilders.termsQuery(RESOURCE_TYPE, resourceTypes);
      mainQuery.must(resourceTypesQuery);
    }

    // Filter version
    if (version != null && version != ResourceVersionFilter.ALL) {
      BoolQueryBuilder versionQuery = QueryBuilders.boolQuery();
      BoolQueryBuilder inner1Query = QueryBuilders.boolQuery();
      BoolQueryBuilder inner2Query = QueryBuilders.boolQuery();
      if (version == ResourceVersionFilter.LATEST) {
        QueryBuilder versionEqualsQuery = QueryBuilders.termsQuery(INFO_IS_LATEST_VERSION, true);
        inner1Query.must(versionEqualsQuery);
        QueryBuilder versionExistsQuery = QueryBuilders.existsQuery(INFO_IS_LATEST_VERSION);
        inner2Query.mustNot(versionExistsQuery);
      } else if (version == ResourceVersionFilter.LATEST_BY_STATUS) {
        QueryBuilder versionEquals1Query = QueryBuilders.termsQuery(INFO_IS_LATEST_PUBLISHED_VERSION, true);
        QueryBuilder versionEquals2Query = QueryBuilders.termsQuery(INFO_IS_LATEST_DRAFT_VERSION, true);
        inner1Query.should(versionEquals1Query);
        inner1Query.should(versionEquals2Query);
        QueryBuilder versionExists1Query = QueryBuilders.existsQuery(INFO_IS_LATEST_PUBLISHED_VERSION);
        QueryBuilder versionExists2Query = QueryBuilders.existsQuery(INFO_IS_LATEST_DRAFT_VERSION);
        inner2Query.mustNot(versionExists1Query);
        inner2Query.mustNot(versionExists2Query);
      }
      versionQuery.should(inner1Query);
      versionQuery.should(inner2Query);
      mainQuery.must(versionQuery);
    }

    // Filter publicationStatus
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      BoolQueryBuilder publicationStatusQuery = QueryBuilders.boolQuery();
      BoolQueryBuilder inner1Query = QueryBuilders.boolQuery();
      QueryBuilder publicationStatusEqualsQuery = QueryBuilders.termsQuery(INFO_BIBO_STATUS,
          publicationStatus.getValue());
      inner1Query.must(publicationStatusEqualsQuery);
      BoolQueryBuilder inner2Query = QueryBuilders.boolQuery();
      QueryBuilder publicationStatusExistsQuery = QueryBuilders.existsQuery(INFO_BIBO_STATUS);
      inner2Query.mustNot(publicationStatusExistsQuery);
      publicationStatusQuery.should(inner1Query);
      publicationStatusQuery.should(inner2Query);
      mainQuery.must(publicationStatusQuery);
    }

    // Set main query
    searchRequestBuilder.setQuery(mainQuery);
    //log.info("Search query in Query DSL:\n" + mainQuery);

    // Sort by field
    // The name is stored on the node, so we can sort by that
    if (sortList != null && sortList.size() > 0) {
      for (String s : sortList) {
        SortOrder sortOrder = SortOrder.ASC;
        if (s.startsWith(ES_SORT_DESC_PREFIX)) {
          sortOrder = SortOrder.DESC;
          s = s.substring(1);
        }
        if (SORT_BY_NAME.equals(s)) {
          searchRequestBuilder.addSort(INFO_SCHEMA_NAME, sortOrder);
        } else if (SORT_LAST_UPDATED_ON_FIELD.equals(s)) {
          searchRequestBuilder.addSort(INFO_PAV_LAST_UPDATED_ON, sortOrder);
        } else if (SORT_CREATED_ON_FIELD.equals(s)) {
          searchRequestBuilder.addSort(INFO_PAV_CREATED_ON, sortOrder);
        }
      }
    }
    return searchRequestBuilder;
  }

  private boolean enclosedByQuotes(String keyword) {
    return keyword.startsWith("\"") && keyword.endsWith("\"");
  }

  /**
   * Rewrites the query generated by the QueryParser to work with our index. This is a recursive method that iterates
   * over all the query clauses and rewrites them.
   *
   * @param inputQuery
   * @return
   */
  private QueryBuilder rewriteQuery(Query inputQuery) throws CedarProcessingException {

    // Example: 't1 AND (disease:crc OR tissue:kidney)' -> This query has two main clauses
    if (inputQuery instanceof BooleanQuery) {
      BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

      for (BooleanClause clause : ((BooleanQuery) inputQuery).clauses()) {
        if (clause.getOccur().equals(BooleanClause.Occur.SHOULD)) {
          boolQueryBuilder = boolQueryBuilder.should(rewriteQuery(clause.getQuery()));
        } else if (clause.getOccur().equals(BooleanClause.Occur.MUST)) {
          boolQueryBuilder = boolQueryBuilder.must(rewriteQuery(clause.getQuery()));
        } else if (clause.getOccur().equals(BooleanClause.Occur.MUST_NOT)) {
          boolQueryBuilder = boolQueryBuilder.mustNot(rewriteQuery(clause.getQuery()));
        }
      }
      return boolQueryBuilder;
    }
    // Examples: 'kidney', 'tissue:kidney'
    else if (inputQuery instanceof TermQuery) {
      Term term = ((TermQuery) inputQuery).getTerm();
      return rewriteTermQuery(term.field(), term.bytes().utf8ToString());
    }
    // Examples: 'colorectal cancer', 'disease:"colorectal cancer"'
    else if (inputQuery instanceof PhraseQuery) {
      return rewritePhraseQuery((PhraseQuery) inputQuery);
    }
    // Example: 'disease:influ*'
    else if (inputQuery instanceof MultiTermQuery) {

      if (inputQuery instanceof PrefixQuery) {
        Term prefix = ((PrefixQuery) inputQuery).getPrefix();
        return rewriteTermQuery(prefix.field(), prefix.bytes().utf8ToString(), true);
      } else if (inputQuery instanceof WildcardQuery) {
        Term term = ((WildcardQuery) inputQuery).getTerm();
        return rewriteTermQuery(term.field(), term.bytes().utf8ToString());
      } else {
        throw new CedarProcessingException("Query type not supported: " + inputQuery.getClass());
      }
    } else {
      throw new CedarProcessingException("Query type not supported: " + inputQuery.getClass());
    }
  }

  private QueryBuilder rewritePhraseQuery(PhraseQuery inputQuery) throws CedarProcessingException {
    String fieldName = inputQuery.getTerms()[0].field();

    if (fieldName.isEmpty()) { // Example: "colorectal carcinoma"
      return rewriteTermQuery(fieldName, inputQuery.toString());
    } else { // Example: disease:"colorectal carcinoma"
      String[] phraseQueryTokens = inputQuery.toString().split(":");
      if (phraseQueryTokens.length >= 2) {
        String fieldValue = phraseQueryTokens[1];
        return rewriteTermQuery(fieldName, fieldValue);
      } else {
        throw new IllegalArgumentException("Could not parse query");
      }
    }
  }

  private QueryBuilder rewriteTermQuery(String fieldName, String fieldValue) throws CedarProcessingException {
    return rewriteTermQuery(fieldName, fieldValue, false);
  }

  private QueryBuilder rewriteTermQuery(String fieldName, String fieldValue, boolean withPrefix) throws CedarProcessingException {
    if (fieldName == null || fieldName.isEmpty()) {

      // SummaryText query
      QueryBuilder summaryTextQuery = QueryBuilders.matchPhraseQuery(SUMMARY_RAW_TEXT, fieldValue);
      return summaryTextQuery;

    } else { // Field name/value query

      QueryBuilder infoFieldsQueryString =
          QueryBuilders.queryStringQuery(generateInfoFieldsQueryString(fieldName, fieldValue, withPrefix));

      QueryBuilder nestedInfoFieldsQuery =
          QueryBuilders.nestedQuery(INFO_FIELDS, infoFieldsQueryString, ScoreMode.None);

      return nestedInfoFieldsQuery;
    }
  }

  private String generateInfoFieldsQueryString(String fieldName, String fieldValue, boolean withPrefix) throws CedarProcessingException {
    String result;
    // Generate field name query. Example: '(infoFields.fieldName:title1 OR infoFields.fieldName:title2)'
    String fieldNameQueryFragment = "(".concat(INFO_FIELDS_FIELD_NAME).concat(":").concat(fieldName)
        .concat(" OR ").concat(INFO_FIELDS_FIELD_PREFERRED_LABEL).concat(":").concat(fieldName).concat(")");
    String infoFieldsFieldValue = INFO_FIELDS_FIELD_VALUE;
    if (fieldValue.contains("http")) {
      infoFieldsFieldValue = INFO_FIELDS_FIELD_VALUE_URI;
    }
    // Generate field value query. Example: 'infoFields.fieldValue:value1'
    String fieldValueQueryFragment = infoFieldsFieldValue.concat(":").concat(fieldValue);
    String andQueryFragment = " AND ";
    if ((fieldName.compareTo(ANY_STRING) != 0) && (fieldValue.compareTo(ANY_STRING) != 0)) {
      result = fieldNameQueryFragment.concat(andQueryFragment).concat(fieldValueQueryFragment);
    } else if ((fieldName.compareTo(ANY_STRING) != 0) && (fieldValue.compareTo(ANY_STRING) == 0)) {
      result = fieldNameQueryFragment;
    } else if ((fieldName.compareTo(ANY_STRING) == 0) && (fieldValue.compareTo(ANY_STRING) != 0)) {
      result = fieldValueQueryFragment;
    } else { //fieldName:_any_ AND fieldValue:_any -> Return everything
      return "*";
    }

    if (withPrefix) {
      result = result.concat("*");
    }
    return result;
  }

  private String preprocessQuery(String query) throws CedarProcessingException {
    query = encodeWildcards(query);
    query = encodeQueryStringUris(query);
    //query = encodeOther(query);
    return query;
  }

  private String encodeWildcards(String query) {

    /**
     * Encode the stars for the cases *:v1, f1:*, and *:*
     */

    Matcher matcherLeftSide = Pattern.compile("(^|\\s|\\()\\*:").matcher(query);
    while (matcherLeftSide.find()) {
      String matchString = query.substring(matcherLeftSide.start(), matcherLeftSide.end());
      String replacement = matchString.replace("*", ANY_STRING);
      query = query.substring(0, matcherLeftSide.start()) + replacement + query.substring(matcherLeftSide.end());
    }

    Matcher matcherRightSide = Pattern.compile("(:\\*($|\\s|\\())").matcher(query);
    while (matcherRightSide.find()) {
      String matchString = query.substring(matcherRightSide.start(), matcherRightSide.end());
      String replacement = matchString.replace("*", ANY_STRING);
      query = query.substring(0, matcherRightSide.start()) + replacement + query.substring(matcherRightSide.end());
    }

    /**
     * Encode stars and question marks embedded into fieldName and/or fieldValue
     * Elasticsearch does not accept unencoded wildcards in the field name (e.g. disea*:crc), so
     * we will encode them all, including those that are part of the field value for simplicity
     */

    /* Encode stars. Example: aaa*aaa:b\*bb as aaa\*aaa:b\*bb */
    query = query.replace("\\*", "*");
    query = query.replace("*", "\\*");

    /* Encode question marks. Example: aaa?aaa:b\?bb as aaa\?aaa:b\?bb */
    query = query.replace("\\?", "?");
    query = query.replace("?", "\\?");

    return query;
  }

  private String encodeQueryStringUris(String query) throws CedarProcessingException {

    final String URL_REGEX = "(((https?)://)" +
        "(%[0-9A-Fa-f]{2}|[-()_.!~*';/?:@&=+$,A-Za-z0-9])+)" +
        "([).!';/?:,][[:blank:]])?";

    Matcher matcher = Pattern.compile(URL_REGEX).matcher(query);

    while (matcher.find()) {
      String url = query.substring(matcher.start(), matcher.end());
      try {
        String encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString());
        query = query.substring(0, matcher.start()) + encodedUrl + query.substring(matcher.end());
      } catch (UnsupportedEncodingException e) {
        throw new CedarProcessingException(e);
      }
    }
    return query;
  }


}
