package org.metadatacenter.server.search.elasticsearch.permission;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.folderserver.info.FolderServerNodeInfo;
import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.search.IndexedDocumentDocument;
import org.metadatacenter.server.security.model.auth.CurrentUserPermissions;
import org.metadatacenter.server.security.model.user.CedarUser;

public class CurrentUserPermissionUpdaterForSearchFolder extends AbstractCurrentUserPermissionUpdaterForSearch {

  private CurrentUserPermissionUpdaterForSearchFolder(IndexedDocumentDocument indexedDocument, CedarUser cedarUser,
                                                      CedarConfig cedarConfig) {
    super(indexedDocument, cedarUser, cedarConfig);
  }

  public static CurrentUserPermissionUpdater get(IndexedDocumentDocument indexedDocument, CedarUser cedarUser,
                                                 CedarConfig cedarConfig) {
    return new CurrentUserPermissionUpdaterForSearchFolder(indexedDocument, cedarUser, cedarConfig);
  }

  @Override
  public void update(CurrentUserPermissions currentUserPermissions) {
    if (userCanWrite()) {
      currentUserPermissions.setCanWrite(true);
      currentUserPermissions.setCanDelete(true);
      currentUserPermissions.setCanRead(true);

      FolderServerNodeInfo info = indexedDocument.getInfo();
      if (!info.getIsRoot() && !info.getIsSystem() && !info.getIsUserHome()) {
        currentUserPermissions.setCanShare(true);
      }
    } else if (userCanRead()) {
      currentUserPermissions.setCanRead(true);
    }
    if (userCanChangeOwnerOfFolder()) {
      currentUserPermissions.setCanChangeOwner(true);
    }
  }

}
