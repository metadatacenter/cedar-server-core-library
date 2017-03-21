package org.metadatacenter.config;

import java.util.Map;

public class MongoConfig {

  private MongoConnection mongoConnection;

  private String databaseNameTest;

  private Map<String, String> collections;

  public MongoConnection getMongoConnection() {
    return mongoConnection;
  }

  public String getDatabaseName() {
    return mongoConnection.getDatabaseName();
  }

  public String getDatabaseNameTest() {
    return databaseNameTest;
  }

  public Map<String, String> getCollections() {
    return collections;
  }
}
