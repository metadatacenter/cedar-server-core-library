package org.metadatacenter.config;

public class ElasticsearchIndexes {

  private ElasticsearchIndex searchIndex;

  private ElasticsearchIndex valueRecommenderIndex;

  public ElasticsearchIndex getSearchIndex() {
    return searchIndex;
  }

  public ElasticsearchIndex getValueRecommenderIndex() {
    return valueRecommenderIndex;
  }

}