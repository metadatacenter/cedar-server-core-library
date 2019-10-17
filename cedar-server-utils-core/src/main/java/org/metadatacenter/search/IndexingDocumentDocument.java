package org.metadatacenter.search;

import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedCategories;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermission;

import java.util.ArrayList;
import java.util.List;

public class IndexingDocumentDocument extends IndexedDocumentDocument {

  private List<String> groups;

  public IndexingDocumentDocument() {
  }

  public IndexingDocumentDocument(String cid) {
    this.cid = cid;
    resetUsers();
    resetGroups();
  }

  private void resetUsers() {
    users = new ArrayList<>();
  }

  private void resetGroups() {
    groups = new ArrayList<>();
  }

  public void setMaterializedPermissions(CedarNodeMaterializedPermissions permissions) {
    resetUsers();
    resetGroups();
    for (String userId : permissions.getUserPermissions().keySet()) {
      ResourcePermission nodePermission = permissions.getUserPermissions().get(userId);
      users.add(CedarNodeMaterializedPermissions.getKey(userId, ResourcePermission.READ));
      if (nodePermission.equals(nodePermission.WRITE)) {
        users.add(CedarNodeMaterializedPermissions.getKey(userId, ResourcePermission.WRITE));
      }
    }
    for (String groupId : permissions.getGroupPermissions().keySet()) {
      ResourcePermission nodePermission = permissions.getGroupPermissions().get(groupId);
      groups.add(CedarNodeMaterializedPermissions.getKey(groupId, ResourcePermission.READ));
      if (nodePermission.equals(ResourcePermission.WRITE)) {
        groups.add(CedarNodeMaterializedPermissions.getKey(groupId, ResourcePermission.WRITE));
      }
    }
  }

  public List<String> getGroups() {
    return groups;
  }

  public void setMaterializedCategories(CedarNodeMaterializedCategories categories) {
    this.categories = new ArrayList<>();
    this.categories.addAll(categories.getCategoryIds());
  }

}
