package org.metadatacenter.config;

public interface SearchRetrieveSettings {
  int getLimit();

  int getLimitIndexRegeneration();

  int getMaxAttempts();

  int getDelayAttempts();
}
