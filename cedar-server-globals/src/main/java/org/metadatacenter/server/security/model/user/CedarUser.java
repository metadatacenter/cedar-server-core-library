package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarUser {

  public static final String UI_POPULATE_A_TEMPLATE = "populateATemplate";
  public static final String UI_FOLDER_VIEW = "folderView";
  public static final String UI_RESOURCE_TYPE_FILTERS = "resourceTypeFilters";
  public static final String UI_FACETED_SEARCH = "facetedSearch";
  public static final String UI_INFO_PANEL = "infoPanel";

  private String userId;
  private String screenName;
  private String homeFolderId;
  private List<CedarUserApiKey> apiKeys;
  private List<CedarUserRole> roles;
  private List<String> permissions;
  private Map<String, CedarUserUIPreferences> uiPreferences;

  public CedarUser() {
    this.apiKeys = new ArrayList<>();
    this.uiPreferences = new HashMap<>();
    this.roles = new ArrayList<>();
    this.uiPreferences.put(UI_POPULATE_A_TEMPLATE, new CedarUserUIPopulateATemplate());
    this.uiPreferences.put(UI_FOLDER_VIEW, new CedarUserUIFolderView());
    this.uiPreferences.put(UI_RESOURCE_TYPE_FILTERS, new CedarUserUIResourceTypeFilters());
    this.uiPreferences.put(UI_FACETED_SEARCH, new CedarUserUIFacetedSearch());
    this.uiPreferences.put(UI_INFO_PANEL, new CedarUserUIInfoPanel());
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public String getScreenName() {
    return screenName;
  }

  public void setScreenName(String screenName) {
    this.screenName = screenName;
  }

  public String getHomeFolderId() {
    return homeFolderId;
  }

  public void setHomeFolderId(String homeFolderId) {
    this.homeFolderId = homeFolderId;
  }

  public List<CedarUserApiKey> getApiKeys() {
    return apiKeys;
  }

  public void setApiKeys(List<CedarUserApiKey> apiKeys) {
    this.apiKeys = apiKeys;
  }

  public List<CedarUserRole> getRoles() {
    return roles;
  }

  public void setRoles(List<CedarUserRole> roles) {
    this.roles = roles;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<String> permissions) {
    this.permissions = permissions;
  }

  public Map<String, CedarUserUIPreferences> getUiPreferences() {
    return uiPreferences;
  }

  public void setUiPreferences(Map<String, CedarUserUIPreferences> uiPreferences) {
    this.uiPreferences = uiPreferences;
  }

  @JsonIgnore
  public CedarUserUIPopulateATemplate getPopulateATemplate() {
    return (CedarUserUIPopulateATemplate) uiPreferences.get(UI_POPULATE_A_TEMPLATE);
  }

  @JsonIgnore
  public CedarUserUIFolderView getFolderView() {
    return (CedarUserUIFolderView) uiPreferences.get(UI_FOLDER_VIEW);
  }

  @JsonIgnore
  public CedarUserUIResourceTypeFilters getResourceTypeFilters() {
    return (CedarUserUIResourceTypeFilters) uiPreferences.get(UI_RESOURCE_TYPE_FILTERS);
  }

  @JsonIgnore
  public String getFirstActiveApiKey() {
    if (apiKeys != null && !apiKeys.isEmpty()) {
      for (CedarUserApiKey k : apiKeys) {
        if (k.isEnabled()) {
          return k.getKey();
        }
      }
    }
    return null;
  }
}
