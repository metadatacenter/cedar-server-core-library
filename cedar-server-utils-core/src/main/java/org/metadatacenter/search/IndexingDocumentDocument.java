package org.metadatacenter.search;

import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedCategories;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.security.model.auth.NodePermission;

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
      NodePermission nodePermission = permissions.getUserPermissions().get(userId);
      users.add(CedarNodeMaterializedPermissions.getKey(userId, NodePermission.READ));
      if (nodePermission.equals(nodePermission.WRITE)) {
        users.add(CedarNodeMaterializedPermissions.getKey(userId, NodePermission.WRITE));
      }
    }
    for (String groupId : permissions.getGroupPermissions().keySet()) {
      NodePermission nodePermission = permissions.getGroupPermissions().get(groupId);
      groups.add(CedarNodeMaterializedPermissions.getKey(groupId, NodePermission.READ));
      if (nodePermission.equals(NodePermission.WRITE)) {
        groups.add(CedarNodeMaterializedPermissions.getKey(groupId, NodePermission.WRITE));
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
