package org.metadatacenter.config;

import java.util.HashMap;

public class ElasticsearchSettingsMappingsConfig {

  private HashMap<String, Object> settings;

  private HashMap<String, Object> mappingsResource;

  private HashMap<String, Object> mappingsPermissions;

  public HashMap<String, Object> getSettings() {
    return settings;
  }

  public HashMap<String, Object> getMappingsResource() {
    return mappingsResource;
  }

  public HashMap<String, Object> getMappingsPermissions() {
    return mappingsPermissions;
  }
}