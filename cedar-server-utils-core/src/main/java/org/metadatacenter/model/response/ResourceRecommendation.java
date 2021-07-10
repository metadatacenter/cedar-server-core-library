package org.metadatacenter.model.response;

import org.metadatacenter.model.folderserver.extract.FolderServerResourceExtract;

public class ResourceRecommendation {

  double recommendationScore;
  int sourceFieldsCount;
  int sourceFieldsMatched;
  FolderServerResourceExtract resourceExtract;

  public ResourceRecommendation() { }

  public ResourceRecommendation(double recommendationScore, int sourceFieldsCount, int sourceFieldsMatched,
                                FolderServerResourceExtract resourceExtract) {
    this.recommendationScore = recommendationScore;
    this.sourceFieldsCount = sourceFieldsCount;
    this.sourceFieldsMatched = sourceFieldsMatched;
    this.resourceExtract = resourceExtract;
  }

  public double getRecommendationScore() {
    return recommendationScore;
  }

  public void setRecommendationScore(double recommendationScore) {
    this.recommendationScore = recommendationScore;
  }

  public int getSourceFieldsCount() {
    return sourceFieldsCount;
  }

  public void setSourceFieldsCount(int sourceFieldsCount) {
    this.sourceFieldsCount = sourceFieldsCount;
  }

  public int getSourceFieldsMatched() {
    return sourceFieldsMatched;
  }

  public void setSourceFieldsMatched(int sourceFieldsMatched) {
    this.sourceFieldsMatched = sourceFieldsMatched;
  }

  public FolderServerResourceExtract getResourceExtract() {
    return resourceExtract;
  }

  public void setResourceExtract(FolderServerResourceExtract resourceExtract) {
    this.resourceExtract = resourceExtract;
  }
}
