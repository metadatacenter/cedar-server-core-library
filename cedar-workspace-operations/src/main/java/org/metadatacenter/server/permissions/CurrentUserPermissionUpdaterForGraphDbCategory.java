package org.metadatacenter.server.permissions;

import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.id.CedarCategoryId;
import org.metadatacenter.permission.currentuserpermission.CurrentUserCategoryPermissionUpdater;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.security.model.auth.CategoryWithCurrentUserPermissions;
import org.metadatacenter.server.security.model.auth.CurrentUserCategoryPermissions;

public class CurrentUserPermissionUpdaterForGraphDbCategory extends CurrentUserCategoryPermissionUpdater {

  private final PermissionServiceSession permissionSession;
  private final CategoryWithCurrentUserPermissions category;

  private CurrentUserPermissionUpdaterForGraphDbCategory(PermissionServiceSession permissionSession,
                                                         CategoryWithCurrentUserPermissions category) {
    this.permissionSession = permissionSession;
    this.category = category;
  }

  public static CurrentUserCategoryPermissionUpdater get(PermissionServiceSession permissionSession,
                                                         CategoryWithCurrentUserPermissions category) {
    return new CurrentUserPermissionUpdaterForGraphDbCategory(permissionSession, category);
  }

  @Override
  public void update(CurrentUserCategoryPermissions currentUserCategoryPermissions) {
    String id = category.getId();
    CedarCategoryId ccId = null;
    try {
      ccId = CedarCategoryId.build(id);
    } catch (CedarProcessingException e) {
      e.printStackTrace();
    }
    category.getCurrentUserPermissions().setCanRead(true);
    if (permissionSession.userHasWriteAccessToCategory(ccId)) {
      category.getCurrentUserPermissions().setCanWrite(true);
      category.getCurrentUserPermissions().setCanDelete(true);
      category.getCurrentUserPermissions().setCanAttach(true);
      category.getCurrentUserPermissions().setCanDetach(true);
      if (!category.isRoot()) {
        category.getCurrentUserPermissions().setCanShare(true);
      }
    } else if (permissionSession.userHasAttachAccessToCategory(ccId)) {
      category.getCurrentUserPermissions().setCanAttach(true);
      category.getCurrentUserPermissions().setCanDetach(true);
    }
    if (permissionSession.userCanChangeOwnerOfCategory(ccId)) {
      category.getCurrentUserPermissions().setCanChangeOwner(true);
    }
  }
}
