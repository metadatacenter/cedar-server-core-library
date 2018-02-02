package org.metadatacenter.config;

import java.util.HashMap;

public class ElasticsearchVrIndexSettingsMappingsConfig {

  private HashMap<String, Object> settings;

  private ElasticsearchVrIndexMappingsConfig mappings;

  public HashMap<String, Object> getSettings() {
    return settings;
  }

  public ElasticsearchVrIndexMappingsConfig getMappings() {
    return mappings;
  }
}