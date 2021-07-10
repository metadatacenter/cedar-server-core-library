package org.metadatacenter.model.response;

import java.util.List;

public class ResourceRecommendationResponse {

  private long totalCount;
  List<ResourceRecommendation> recommendations;

  public ResourceRecommendationResponse() { }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public List<ResourceRecommendation> getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(List<ResourceRecommendation> recommendations) {
    this.recommendations = recommendations;
  }
}
