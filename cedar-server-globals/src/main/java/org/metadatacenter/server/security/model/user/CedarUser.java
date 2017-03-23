package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.auth.CedarPermission;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

import static org.metadatacenter.constant.HttpConstants.HTTP_AUTH_HEADER_APIKEY_PREFIX;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarUser implements CedarUserRepresentation {

  public static final String UI_POPULATE_A_TEMPLATE = "populateATemplate";
  public static final String UI_FOLDER_VIEW = "folderView";
  public static final String UI_RESOURCE_TYPE_FILTERS = "resourceTypeFilters";
  public static final String UI_FACETED_SEARCH = "facetedSearch";
  public static final String UI_INFO_PANEL = "infoPanel";

  @NotNull
  private String id;

  @NotNull
  private String firstName;

  @NotNull
  private String lastName;

  @NotNull
  private String email;

  private String homeFolderId;

  @NotNull
  private List<CedarUserApiKey> apiKeys;

  @NotNull
  private List<CedarUserRole> roles;

  @NotNull
  private List<String> permissions;

  @NotNull
  private CedarUserUIPreferences uiPreferences;

  public CedarUser() {
    this.apiKeys = new ArrayList<>();
    this.roles = new ArrayList<>();
    this.uiPreferences = new CedarUserUIPreferences();
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public CedarUserUIPreferences getUiPreferences() {
    return uiPreferences;
  }

  public void setUiPreferences(CedarUserUIPreferences uiPreferences) {
    this.uiPreferences = uiPreferences;
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

  @JsonIgnore
  public String getFirstApiKeyAuthHeader() {
    String apiKey = this.getFirstActiveApiKey();
    if (apiKey != null) {
      return HTTP_AUTH_HEADER_APIKEY_PREFIX + apiKey;
    } else {
      return null;
    }
  }

  public boolean has(CedarPermission permission) {
    return permission != null && permissions != null && permissions.contains(permission.getPermissionName());
  }

}
