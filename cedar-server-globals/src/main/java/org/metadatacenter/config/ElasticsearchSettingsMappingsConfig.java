package org.metadatacenter.config;

import java.util.HashMap;

public class ElasticsearchSettingsMappingsConfig {

  private HashMap<String, Object> settings;

  private HashMap<String, Object> mappings;

  public HashMap<String, Object> getSettings() {
    return settings;
  }

  public HashMap<String, Object> getMappings() {
    return mappings;
  }
}