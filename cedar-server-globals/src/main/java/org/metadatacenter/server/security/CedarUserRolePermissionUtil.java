package org.metadatacenter.server.security;

import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.CedarUserRole;

import java.util.*;

public abstract class CedarUserRolePermissionUtil {

  private static final Map<CedarUserRole, List<String>> roleToPermissions;
  private static final List<String> creatorPermissions;
  private static final List<String> instantiatorPermissions;
  private static final List<String> builtInSystemAdministratorPermissions;
  private static final List<String> administratorPermissions;

  static {
    creatorPermissions = new ArrayList<>();
    creatorPermissions.add(CedarPermission.LOGGED_IN.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_FIELD_CREATE.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_FIELD_READ.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_FIELD_UPDATE.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_FIELD_DELETE.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_ELEMENT_CREATE.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_ELEMENT_READ.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_ELEMENT_UPDATE.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_ELEMENT_DELETE.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_CREATE.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_READ.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_UPDATE.getPermissionName());
    creatorPermissions.add(CedarPermission.TEMPLATE_DELETE.getPermissionName());
    creatorPermissions.add(CedarPermission.FOLDER_CREATE.getPermissionName());
    creatorPermissions.add(CedarPermission.FOLDER_READ.getPermissionName());
    creatorPermissions.add(CedarPermission.FOLDER_UPDATE.getPermissionName());
    creatorPermissions.add(CedarPermission.FOLDER_DELETE.getPermissionName());
    creatorPermissions.add(CedarPermission.USER_PROFILE_OWN_READ.getPermissionName());

    instantiatorPermissions = new ArrayList<>();
    instantiatorPermissions.add(CedarPermission.LOGGED_IN.getPermissionName());
    instantiatorPermissions.add(CedarPermission.TEMPLATE_INSTANCE_CREATE.getPermissionName());
    instantiatorPermissions.add(CedarPermission.TEMPLATE_INSTANCE_READ.getPermissionName());
    instantiatorPermissions.add(CedarPermission.TEMPLATE_INSTANCE_UPDATE.getPermissionName());
    instantiatorPermissions.add(CedarPermission.TEMPLATE_INSTANCE_DELETE.getPermissionName());
    instantiatorPermissions.add(CedarPermission.USER_PROFILE_OWN_READ.getPermissionName());

    builtInSystemAdministratorPermissions = new ArrayList<>();
    builtInSystemAdministratorPermissions.add(CedarPermission.LOGGED_IN.getPermissionName());
    builtInSystemAdministratorPermissions.add(CedarPermission.SEARCH_INDEX_REINDEX.getPermissionName());
    builtInSystemAdministratorPermissions.add(CedarPermission.SYSTEM_FOLDER_CREATE.getPermissionName());

    administratorPermissions = new ArrayList<>();
    administratorPermissions.add(CedarPermission.LOGGED_IN.getPermissionName());
    administratorPermissions.add(CedarPermission.SEARCH_INDEX_REINDEX.getPermissionName());

    roleToPermissions = new HashMap<>();
    roleToPermissions.put(CedarUserRole.TEMPLATE_CREATOR, creatorPermissions);
    roleToPermissions.put(CedarUserRole.TEMPLATE_INSTANTIATOR, instantiatorPermissions);
    roleToPermissions.put(CedarUserRole.BUILT_IN_SYSTEM_ADMINISTRATOR, builtInSystemAdministratorPermissions);
    roleToPermissions.put(CedarUserRole.ADMINISTRATOR, administratorPermissions);
  }

  public static void expandRolesIntoPermissions(CedarUser u) {
    Set<String> permissions = new HashSet<>();
    for (CedarUserRole role : u.getRoles()) {
      permissions.addAll(roleToPermissions.get(role));
    }
    if (u.getPermissions() == null) {
      u.setPermissions(new ArrayList<>());
    }
    u.getPermissions().clear();
    u.getPermissions().addAll(permissions);
    Collections.sort(u.getPermissions());
  }
}
