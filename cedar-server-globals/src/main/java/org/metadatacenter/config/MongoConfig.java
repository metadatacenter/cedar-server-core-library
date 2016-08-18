package org.metadatacenter.config;

import java.util.Map;

public interface MongoConfig {
  String getDatabaseName();

  String getDatabaseNameTest();

  Map<String, String> getCollections();
}
