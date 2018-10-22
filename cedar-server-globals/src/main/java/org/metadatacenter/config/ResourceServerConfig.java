package org.metadatacenter.config;

public class ResourceServerConfig extends ServerConfig {

  private String regenerateSearchIndex;
  private String regenerateRulesIndex;

  public String getRegenerateSearchIndex() {
    return regenerateSearchIndex;
  }

  public String getRegenerateRulesIndex() {
    return regenerateRulesIndex;
  }
}
