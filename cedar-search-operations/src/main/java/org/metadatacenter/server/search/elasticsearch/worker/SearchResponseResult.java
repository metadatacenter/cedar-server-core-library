package org.metadatacenter.server.search.elasticsearch.worker;

import org.elasticsearch.action.search.SearchResponse;

public class SearchResponseResult {

  private SearchResponse response;
  private long totalCount;

  public SearchResponseResult(SearchResponse response, long totalCount) {
    this.response = response;
    this.totalCount = totalCount;
  }

  public SearchResponse getResponse() {
    return response;
  }

  public long getTotalCount() {
    return totalCount;
  }

}
