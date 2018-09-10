package org.metadatacenter.server.search.elasticsearch.document;

import org.metadatacenter.server.search.permission.IdNodePermissionPair;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;

import java.util.ArrayList;
import java.util.List;

public class IndexingDocumentDocument extends IndexedDocumentDocument {

  private List<IdNodePermissionPair> users;

  private List<IdNodePermissionPair> groups;


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
      users.add(new IdNodePermissionPair(userId, permissions.getUserPermissions().get(userId)));
    }
    for (String groupId : permissions.getGroupPermissions().keySet()) {
      groups.add(new IdNodePermissionPair(groupId, permissions.getGroupPermissions().get(groupId)));
    }
  }

  public List<IdNodePermissionPair> getUsers() {
    return users;
  }

  public List<IdNodePermissionPair> getGroups() {
    return groups;
  }

}
