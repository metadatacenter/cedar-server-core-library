package org.metadatacenter.server.search.permission;

import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;

import java.util.ArrayList;
import java.util.List;

public class CedarPermissionIndexResource {
  private String id;
  private List<IdNodePermissionPair> users;
  private List<IdNodePermissionPair> groups;

  public CedarPermissionIndexResource(CedarNodeMaterializedPermissions permissions) {
    this.id = permissions.getId();
    users = new ArrayList<>();
    groups = new ArrayList<>();
    for(String userId : permissions.getUserPermissions().keySet()) {
      users.add(new IdNodePermissionPair(userId, permissions.getUserPermissions().get(userId)));
    }
    for(String groupId : permissions.getGroupPermissions().keySet()) {
      groups.add(new IdNodePermissionPair(groupId, permissions.getGroupPermissions().get(groupId)));
    }
  }

  public String getId() {
    return id;
  }

  public List<IdNodePermissionPair> getUsers() {
    return users;
  }

  public List<IdNodePermissionPair> getGroups() {
    return groups;
  }
}
