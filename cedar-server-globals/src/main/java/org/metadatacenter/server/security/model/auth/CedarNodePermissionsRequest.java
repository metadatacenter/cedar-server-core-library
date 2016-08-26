package org.metadatacenter.server.security.model.auth;

import java.util.ArrayList;
import java.util.List;

public class CedarNodePermissionsRequest {

  private String owner;
  private List<NodePermissionUserIdPair> userPermissions;
  private List<NodePermissionGroupIdPair> groupPermissions;


  public CedarNodePermissionsRequest() {
    userPermissions = new ArrayList<>();
    groupPermissions = new ArrayList<>();
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public List<NodePermissionUserIdPair> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermissions(List<NodePermissionUserIdPair> userPermissions) {
    this.userPermissions = userPermissions;
  }

  public List<NodePermissionGroupIdPair> getGroupPermissions() {
    return groupPermissions;
  }

  public void setGroupPermissions(List<NodePermissionGroupIdPair> groupPermissions) {
    this.groupPermissions = groupPermissions;
  }
}
