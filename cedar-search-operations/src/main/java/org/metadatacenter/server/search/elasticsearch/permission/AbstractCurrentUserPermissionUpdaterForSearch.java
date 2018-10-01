package org.metadatacenter.server.search.elasticsearch.permission;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.search.IdNodePermissionPair;
import org.metadatacenter.search.IndexedDocumentDocument;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.auth.NodePermission;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.List;

public abstract class AbstractCurrentUserPermissionUpdaterForSearch extends CurrentUserPermissionUpdater {

  protected final IndexedDocumentDocument indexedDocument;
  protected final CedarUser cedarUser;
  protected final CedarConfig cedarConfig;


  protected AbstractCurrentUserPermissionUpdaterForSearch(IndexedDocumentDocument indexedDocument,
                                                          CedarUser cedarUser, CedarConfig cedarConfig) {
    this.indexedDocument = indexedDocument;
    this.cedarUser = cedarUser;
    this.cedarConfig = cedarConfig;
  }

  protected boolean userCanWrite() {
    if (cedarUser.has(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE)) {
      return true;
    }
    return containsPermissions(indexedDocument.getUsers(), NodePermission.WRITE);
  }

  protected boolean userCanRead() {
    if (cedarUser.has(CedarPermission.READ_NOT_READABLE_NODE)) {
      return true;
    }
    return containsPermissions(indexedDocument.getUsers(), NodePermission.READ);
  }

  protected boolean containsPermissions(List<IdNodePermissionPair> users, NodePermission permission) {
    for (IdNodePermissionPair pair : users) {
      if (pair.getPermission().equals(permission) && pair.getId().equals(cedarUser.getId())) {
        return true;
      }
    }
    return false;
  }

  protected boolean userCanChangeOwnerOfFolder() {
    if (cedarUser.has(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE)) {
      return true;
    } else {
      return documentIsOwned();
    }
  }

  protected boolean userCanPerformVersioning() {
    return indexedDocument.getInfo().getType().isVersioned() && documentIsOwned();
  }

  private boolean documentIsOwned() {
    return indexedDocument.getInfo().getOwnedBy() != null &&
        indexedDocument.getInfo().getOwnedBy().equals(cedarUser.getId());
  }

  public boolean resourceCanBePublished() {
    if (indexedDocument.getInfo().isLatestVersion()) {
      return indexedDocument.getInfo().getPublicationStatus() == BiboStatus.DRAFT;
    }
    return false;
  }

  public boolean resourceCanBeDrafted() {
    if (indexedDocument.getInfo().isLatestVersion()) {
      return indexedDocument.getInfo().getPublicationStatus() == BiboStatus.PUBLISHED;
    }
    return false;
  }

}
