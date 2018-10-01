package org.metadatacenter.permission.currentuserpermission;

import org.metadatacenter.server.security.model.auth.CurrentUserPermissions;

public abstract class CurrentUserPermissionUpdater {

  public abstract void update(CurrentUserPermissions currentUserPermissions);
}
