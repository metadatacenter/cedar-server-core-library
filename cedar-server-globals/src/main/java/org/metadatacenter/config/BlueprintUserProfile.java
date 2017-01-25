package org.metadatacenter.config;

import java.util.List;
import java.util.Map;

public class BlueprintUserProfile {

  private String screenNameTemplate;

  private String homeFolderDescriptionTemplate;

  private BlueprintDefaultAPIKey defaultAPIKey;

  private Map<String, List<String>> userRoles;

  private BlueprintUIPreferences uiPreferences;

  public String getScreenNameTemplate() {
    return screenNameTemplate;
  }

  public String getHomeFolderDescriptionTemplate() {
    return homeFolderDescriptionTemplate;
  }

  public BlueprintDefaultAPIKey getDefaultAPIKey() {
    return defaultAPIKey;
  }

  public Map<String, List<String>> getUserRoles() {
    return userRoles;
  }

  public BlueprintUIPreferences getUiPreferences() {
    return uiPreferences;
  }
}
