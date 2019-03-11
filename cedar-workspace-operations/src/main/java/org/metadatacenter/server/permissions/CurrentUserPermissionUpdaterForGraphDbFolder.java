package org.metadatacenter.server.permissions;

import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.security.model.auth.CurrentUserPermissions;
import org.metadatacenter.server.security.model.auth.FolderWithCurrentUserPermissions;

public class CurrentUserPermissionUpdaterForGraphDbFolder extends CurrentUserPermissionUpdater {

  private final PermissionServiceSession permissionSession;
  private final FolderWithCurrentUserPermissions folder;

  private CurrentUserPermissionUpdaterForGraphDbFolder(PermissionServiceSession permissionSession,
                                                       FolderWithCurrentUserPermissions folder) {
    this.permissionSession = permissionSession;
    this.folder = folder;
  }

  public static CurrentUserPermissionUpdater get(PermissionServiceSession permissionSession,
                                                 FolderWithCurrentUserPermissions folder) {
    return new CurrentUserPermissionUpdaterForGraphDbFolder(permissionSession, folder);
  }

  @Override
  public void update(CurrentUserPermissions currentUserPermissions) {
    String id = folder.getId();
    if (permissionSession.userHasWriteAccessToNode(id)) {
      folder.getCurrentUserPermissions().setCanWrite(true);
      folder.getCurrentUserPermissions().setCanDelete(true);
      folder.getCurrentUserPermissions().setCanRead(true);
      if (!folder.isRoot() && !folder.isSystem() && !folder.isUserHome()) {
        folder.getCurrentUserPermissions().setCanShare(true);
      }
    } else if (permissionSession.userHasReadAccessToNode(id)) {
      folder.getCurrentUserPermissions().setCanRead(true);
    }
    if (permissionSession.userCanChangeOwnerOfNode(id)) {
      folder.getCurrentUserPermissions().setCanChangeOwner(true);
    }
  }
}
