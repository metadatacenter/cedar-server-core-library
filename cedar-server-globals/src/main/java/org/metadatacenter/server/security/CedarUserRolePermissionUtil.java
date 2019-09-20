package org.metadatacenter.server.security;

import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.CedarUserRole;

import java.util.*;

public abstract class CedarUserRolePermissionUtil {

  private static final Map<CedarUserRole, Set<String>> roleToPermissions;
  private static final Set<String> templateCreatorPermissions;
  private static final Set<String> metadataCreatorPermissions;
  private static final Set<String> userAdministratorPermissions;
  private static final Set<String> groupAdministratorPermissions;
  private static final Set<String> filesystemAdministratorPermissions;
  private static final Set<String> categoryAdministratorPermissions;
  private static final Set<String> categoryPrivilegedAdministratorPermissions;
  private static final Set<String> searchReindexerPermissions;
  private static final Set<String> builtInSystemAdministratorPermissions;

  static {
    templateCreatorPermissions = new HashSet<>();
    templateCreatorPermissions.add(CedarPermission.LOGGED_IN.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_FIELD_CREATE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_FIELD_READ.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_FIELD_UPDATE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_FIELD_DELETE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_ELEMENT_CREATE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_ELEMENT_READ.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_ELEMENT_UPDATE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_ELEMENT_DELETE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_CREATE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_READ.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_UPDATE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.TEMPLATE_DELETE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.FOLDER_CREATE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.FOLDER_READ.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.FOLDER_UPDATE.getPermissionName());
    templateCreatorPermissions.add(CedarPermission.FOLDER_DELETE.getPermissionName());

    metadataCreatorPermissions = new HashSet<>();
    metadataCreatorPermissions.add(CedarPermission.LOGGED_IN.getPermissionName());
    metadataCreatorPermissions.add(CedarPermission.TEMPLATE_INSTANCE_CREATE.getPermissionName());
    metadataCreatorPermissions.add(CedarPermission.TEMPLATE_INSTANCE_READ.getPermissionName());
    metadataCreatorPermissions.add(CedarPermission.TEMPLATE_INSTANCE_UPDATE.getPermissionName());
    metadataCreatorPermissions.add(CedarPermission.TEMPLATE_INSTANCE_DELETE.getPermissionName());

    userAdministratorPermissions = new HashSet<>();
    userAdministratorPermissions.add(CedarPermission.USER_READ.getPermissionName());
    userAdministratorPermissions.add(CedarPermission.USER_UPDATE.getPermissionName());

    groupAdministratorPermissions = new HashSet<>();
    groupAdministratorPermissions.add(CedarPermission.GROUP_CREATE.getPermissionName());
    groupAdministratorPermissions.add(CedarPermission.GROUP_READ.getPermissionName());
    groupAdministratorPermissions.add(CedarPermission.GROUP_UPDATE.getPermissionName());
    groupAdministratorPermissions.add(CedarPermission.GROUP_DELETE.getPermissionName());
    groupAdministratorPermissions.add(CedarPermission.UPDATE_NOT_ADMINISTERED_GROUP.getPermissionName());

    filesystemAdministratorPermissions = new HashSet<>();
    filesystemAdministratorPermissions.add(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE.getPermissionName());
    filesystemAdministratorPermissions.add(CedarPermission.READ_NOT_READABLE_NODE.getPermissionName());
    filesystemAdministratorPermissions.add(CedarPermission.WRITE_NOT_WRITABLE_NODE.getPermissionName());

    categoryAdministratorPermissions = new HashSet<>();
    categoryAdministratorPermissions.add(CedarPermission.CATEGORY_CREATE.getPermissionName());
    categoryAdministratorPermissions.add(CedarPermission.CATEGORY_READ.getPermissionName());
    categoryAdministratorPermissions.add(CedarPermission.CATEGORY_UPDATE.getPermissionName());
    categoryAdministratorPermissions.add(CedarPermission.CATEGORY_DELETE.getPermissionName());

    categoryPrivilegedAdministratorPermissions = new HashSet<>();
    categoryPrivilegedAdministratorPermissions.add(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_CATEGORY.getPermissionName());
    categoryPrivilegedAdministratorPermissions.add(CedarPermission.WRITE_NOT_WRITABLE_CATEGORY.getPermissionName());

    searchReindexerPermissions = new HashSet<>();
    searchReindexerPermissions.add(CedarPermission.LOGGED_IN.getPermissionName());
    searchReindexerPermissions.add(CedarPermission.SEARCH_INDEX_REINDEX.getPermissionName());
    searchReindexerPermissions.add(CedarPermission.RULES_INDEX_REINDEX.getPermissionName());

    builtInSystemAdministratorPermissions = new HashSet<>();
    builtInSystemAdministratorPermissions.addAll(templateCreatorPermissions);
    builtInSystemAdministratorPermissions.addAll(metadataCreatorPermissions);
    builtInSystemAdministratorPermissions.addAll(userAdministratorPermissions);
    builtInSystemAdministratorPermissions.addAll(groupAdministratorPermissions);
    builtInSystemAdministratorPermissions.addAll(filesystemAdministratorPermissions);
    builtInSystemAdministratorPermissions.addAll(categoryAdministratorPermissions);
    builtInSystemAdministratorPermissions.addAll(categoryPrivilegedAdministratorPermissions);
    builtInSystemAdministratorPermissions.addAll(searchReindexerPermissions);
    builtInSystemAdministratorPermissions.add(CedarPermission.SEND_PROCESS_MESSAGE.getPermissionName());

    roleToPermissions = new HashMap<>();
    roleToPermissions.put(CedarUserRole.TEMPLATE_CREATOR, templateCreatorPermissions);
    roleToPermissions.put(CedarUserRole.METADATA_CREATOR, metadataCreatorPermissions);
    roleToPermissions.put(CedarUserRole.USER_ADMINISTRATOR, userAdministratorPermissions);
    roleToPermissions.put(CedarUserRole.GROUP_ADMINISTRATOR, groupAdministratorPermissions);
    roleToPermissions.put(CedarUserRole.FILESYSTEM_ADMINISTRATOR, filesystemAdministratorPermissions);
    roleToPermissions.put(CedarUserRole.CATEGORY_ADMINISTRATOR, categoryAdministratorPermissions);
    roleToPermissions.put(CedarUserRole.CATEGORY_PRIVILEGED_ADMINISTRATOR, categoryPrivilegedAdministratorPermissions);
    roleToPermissions.put(CedarUserRole.SEARCH_REINDEXER, searchReindexerPermissions);
    roleToPermissions.put(CedarUserRole.BUILT_IN_SYSTEM_ADMINISTRATOR, builtInSystemAdministratorPermissions);
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
