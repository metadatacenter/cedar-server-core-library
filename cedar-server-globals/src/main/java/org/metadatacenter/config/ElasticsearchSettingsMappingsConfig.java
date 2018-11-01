package org.metadatacenter.config;

import java.util.Map;

public class ElasticsearchSettingsMappingsConfig {

  private Map<String, Object> settings;

  private ElasticsearchMappingsConfig mappings;

  public Map<String, Object> getSettings() {
    return settings;
  }

  public ElasticsearchMappingsConfig getMappings() {
    return mappings;
  }
}