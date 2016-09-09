package org.metadatacenter.server.security.util;

import org.metadatacenter.config.BlueprintUIPreferences;
import org.metadatacenter.config.BlueprintUserProfile;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.security.CedarUserRolePermissionUtil;
import org.metadatacenter.server.security.model.user.*;
import org.metadatacenter.util.CedarUserNameUtil;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class CedarUserUtil {

  private CedarUserUtil() {
  }

  public static CedarUser createUserFromBlueprint(ICedarUserRepresentation ur) {
    return createUserFromBlueprint(ur, null);
  }

  public static CedarUser createUserFromBlueprint(ICedarUserRepresentation ur, List<CedarUserRole> roles) {
    CedarConfig cedarConfig = CedarConfig.getInstance();
    BlueprintUserProfile blueprint = cedarConfig.getBlueprintUserProfile();
    BlueprintUIPreferences uiPref = cedarConfig.getBlueprintUIPreferences();

    CedarUser user = new CedarUser();
    user.setId(ur.getId());
    user.setFirstName(ur.getFirstName());
    user.setLastName(ur.getLastName());
    user.setEmail(ur.getEmail());

    LocalDateTime now = LocalDateTime.now();
    // create a default API Key
    CedarUserApiKey apiKey = new CedarUserApiKey();
    apiKey.setKey(UUID.randomUUID().toString());
    apiKey.setCreationDate(now);
    apiKey.setEnabled(true);
    apiKey.setServiceName(blueprint.getDefaultAPIKey().getServiceName());
    apiKey.setDescription(blueprint.getDefaultAPIKey().getDescription());

    user.getApiKeys().add(apiKey);

    if (roles == null || roles.isEmpty()) {
      user.getRoles().add(CedarUserRole.TEMPLATE_CREATOR);
      user.getRoles().add(CedarUserRole.TEMPLATE_INSTANTIATOR);
    } else {
      user.getRoles().addAll(roles);
    }
    CedarUserRolePermissionUtil.expandRolesIntoPermissions(user);

    // set folder view defaults
    CedarUserUIFolderView folderView = user.getFolderView();
    folderView.setSortBy(uiPref.getFolderView().getSortBy());
    folderView.setSortDirection(SortDirection.forValue(uiPref.getFolderView().getSortDirection()));
    folderView.setViewMode(ViewMode.forValue(uiPref.getFolderView().getViewMode()));

    // set resource type filter defaults
    CedarUserUIResourceTypeFilters resourceTypeFilters = user.getResourceTypeFilters();
    resourceTypeFilters.setField(true);
    resourceTypeFilters.setElement(true);
    resourceTypeFilters.setTemplate(true);
    resourceTypeFilters.setInstance(true);

    // set populate-a-template view defaults
    CedarUserUIPopulateATemplate populateATemplate = user.getPopulateATemplate();
    populateATemplate.setOpened(uiPref.getPopulateATemplate().getOpened());
    populateATemplate.setSortBy(uiPref.getPopulateATemplate().getSortBy());
    populateATemplate.setSortDirection(SortDirection.forValue(uiPref.getPopulateATemplate().getSortDirection()));
    populateATemplate.setViewMode(ViewMode.forValue(uiPref.getPopulateATemplate().getViewMode()));
    return user;
  }

}
