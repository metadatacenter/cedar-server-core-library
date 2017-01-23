package org.metadatacenter.config;

public class SearchRetrieveSettings {

  private int limit;

  private int limitIndexRegeneration;

  private int maxAttempts;

  private int delayAttempts;

  public int getLimit() {
    return limit;
  }

  public int getLimitIndexRegeneration() {
    return limitIndexRegeneration;
  }

  public int getMaxAttempts() {
    return maxAttempts;
  }

  public int getDelayAttempts() {
    return delayAttempts;
  }
}
