package org.metadatacenter.config;

import java.util.Map;

public class MongoConfig {

  private String databaseName;

  private String databaseNameTest;

  private Map<String, String> collections;

  public String getDatabaseName() {
    return databaseName;
  }

  public String getDatabaseNameTest() {
    return databaseNameTest;
  }

  public Map<String, String> getCollections() {
    return collections;
  }
}
