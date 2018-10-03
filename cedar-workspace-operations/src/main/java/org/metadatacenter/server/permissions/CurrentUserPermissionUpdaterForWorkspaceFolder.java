package org.metadatacenter.server.permissions;

import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.security.model.auth.CurrentUserPermissions;
import org.metadatacenter.server.security.model.auth.FolderWithCurrentUserPermissions;

public class CurrentUserPermissionUpdaterForWorkspaceFolder extends CurrentUserPermissionUpdater {

  private final PermissionServiceSession permissionSession;
  private final FolderWithCurrentUserPermissions folder;

  private CurrentUserPermissionUpdaterForWorkspaceFolder(PermissionServiceSession permissionSession,
                                                         FolderWithCurrentUserPermissions folder) {
    this.permissionSession = permissionSession;
    this.folder = folder;
  }

  public static CurrentUserPermissionUpdater get(PermissionServiceSession permissionSession,
                                                 FolderWithCurrentUserPermissions folder) {
    return new CurrentUserPermissionUpdaterForWorkspaceFolder(permissionSession, folder);
  }

  @Override
  public void update(CurrentUserPermissions currentUserPermissions) {
    String id = folder.getId();
    if (permissionSession.userHasWriteAccessToFolder(id)) {
      folder.getCurrentUserPermissions().setCanWrite(true);
      folder.getCurrentUserPermissions().setCanDelete(true);
      folder.getCurrentUserPermissions().setCanRead(true);
      if (!folder.isRoot() && !folder.isSystem() && !folder.isUserHome()) {
        folder.getCurrentUserPermissions().setCanShare(true);
      }
    } else if (permissionSession.userHasReadAccessToFolder(id)) {
      folder.getCurrentUserPermissions().setCanRead(true);
    }
    if (permissionSession.userCanChangeOwnerOfFolder(id)) {
      folder.getCurrentUserPermissions().setCanChangeOwner(true);
    }
  }
}
