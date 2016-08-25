package org.metadatacenter.server.security.model.auth;

import java.util.HashMap;
import java.util.Map;

public class CedarNodePermissionsRequest {

  private String owner;
  private Map<String, NodePermission> userPermissions;
  private Map<String, NodePermission> groupPermissions;


  public CedarNodePermissionsRequest() {
    userPermissions = new HashMap<>();
    groupPermissions = new HashMap<>();
  }

  public String getOwner() {
    return owner;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public Map<String, NodePermission> getUserPermissions() {
    return userPermissions;
  }

  public Map<String, NodePermission> getGroupPermissions() {
    return groupPermissions;
  }

}
