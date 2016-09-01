package org.metadatacenter.server.security.model.auth;

import java.util.ArrayList;
import java.util.List;

public class CedarNodePermissionsRequest {

  private NodePermissionUser owner;
  private List<NodePermissionUserPermissionPair> userPermissions;
  private List<NodePermissionGroupPermissionPair> groupPermissions;


  public CedarNodePermissionsRequest() {
    userPermissions = new ArrayList<>();
    groupPermissions = new ArrayList<>();
  }

  public NodePermissionUser getOwner() {
    return owner;
  }

  public void setOwner(NodePermissionUser owner) {
    this.owner = owner;
  }

  public List<NodePermissionUserPermissionPair> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<NodePermissionUserPermissionPair> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public List<NodePermissionGroupPermissionPair> getGroupPermissions() {
    return groupPermissions;
  }

  public void setGroupPermissions(List<NodePermissionGroupPermissionPair> groupPermissions) {
    this.groupPermissions = groupPermissions;
  }
}
