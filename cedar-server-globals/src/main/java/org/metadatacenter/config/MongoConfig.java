package org.metadatacenter.config;

import java.util.Map;

public interface MongoConfig {
  String getDatabaseName();

  Map<String, String> getCollections();
}
