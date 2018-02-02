package org.metadatacenter.config;

import java.util.HashMap;

public class ElasticsearchSearchIndexSettingsMappingsConfig {

  private HashMap<String, Object> settings;

  private ElasticsearchSearchIndexMappingsConfig mappings;

  public HashMap<String, Object> getSettings() {
    return settings;
  }

  public ElasticsearchSearchIndexMappingsConfig getMappings() {
    return mappings;
  }
}