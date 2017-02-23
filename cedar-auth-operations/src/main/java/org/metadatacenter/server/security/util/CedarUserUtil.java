package org.metadatacenter.server.security.util;

import org.metadatacenter.config.BlueprintUIPreferences;
import org.metadatacenter.config.BlueprintUserProfile;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.security.CedarUserRolePermissionUtil;
import org.metadatacenter.server.security.model.user.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CedarUserUtil {

  private CedarUserUtil() {
  }

  public static CedarUser createUserFromBlueprint(CedarConfig cedarConfig, CedarUserRepresentation ur, String apiKey,
                                                  List<CedarUserRole> roles) {
    BlueprintUserProfile blueprint = cedarConfig.getBlueprintUserProfile();
    BlueprintUIPreferences uiPref = cedarConfig.getBlueprintUserProfile().getUiPreferences();

    CedarUser user = new CedarUser();
    user.setId(ur.getId());
    user.setFirstName(ur.getFirstName());
    user.setLastName(ur.getLastName());
    user.setEmail(ur.getEmail());

    LocalDateTime now = LocalDateTime.now();
    // create a default API Key
    CedarUserApiKey apiKeyObject = new CedarUserApiKey();
    if (apiKey != null) {
      apiKeyObject.setKey(apiKey);
    } else {
      apiKeyObject.setKey(UUID.randomUUID().toString());
    }
    apiKeyObject.setCreationDate(now);
    apiKeyObject.setEnabled(true);
    apiKeyObject.setServiceName(blueprint.getDefaultAPIKey().getServiceName());
    apiKeyObject.setDescription(blueprint.getDefaultAPIKey().getDescription());

    user.getApiKeys().add(apiKeyObject);

    if (roles == null || roles.isEmpty()) {
      user.getRoles().add(CedarUserRole.TEMPLATE_CREATOR);
      user.getRoles().add(CedarUserRole.TEMPLATE_INSTANTIATOR);
    } else {
      user.getRoles().addAll(roles);
    }
    CedarUserRolePermissionUtil.expandRolesIntoPermissions(user);

    // set folder view defaults
    CedarUserUIFolderView folderView = user.getUiPreferences().getFolderView();
    folderView.setSortBy(uiPref.getFolderView().getSortBy());
    folderView.setSortDirection(SortDirection.forValue(uiPref.getFolderView().getSortDirection()));
    folderView.setViewMode(ViewMode.forValue(uiPref.getFolderView().getViewMode()));

    // set resource type filter defaults
    CedarUserUIResourceTypeFilters resourceTypeFilters = user.getUiPreferences().getResourceTypeFilters();
    resourceTypeFilters.setField(false);
    resourceTypeFilters.setElement(true);
    resourceTypeFilters.setTemplate(true);
    resourceTypeFilters.setInstance(true);

    CedarUserUIInfoPanel infoPanel = user.getUiPreferences().getInfoPanel();
    infoPanel.setOpened(false);

    CedarUserUITemplateEditor templateEditor = user.getUiPreferences().getTemplateEditor();
    templateEditor.setTemplateJsonViewer(false);

    CedarUserUIMetadataEditor metadataEditor = user.getUiPreferences().getMetadataEditor();
    metadataEditor.setTemplateJsonViewer(false);
    metadataEditor.setMetadataJsonViewer(false);

    user.getUiPreferences().setStylesheet(blueprint.getUiPreferences().getStylesheet());

    return user;
  }

}
