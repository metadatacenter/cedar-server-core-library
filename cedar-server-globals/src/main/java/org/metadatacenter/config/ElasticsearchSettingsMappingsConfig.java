package org.metadatacenter.config;

import java.util.HashMap;

public class ElasticsearchSettingsMappingsConfig {

  private HashMap<String, Object> settings;

  private ElasticsearchMappingsConfig mappings;

  public HashMap<String, Object> getSettings() {
    return settings;
  }

  public ElasticsearchMappingsConfig getMappings() {
    return mappings;
  }
}