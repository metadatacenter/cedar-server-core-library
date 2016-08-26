package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.Map;
import java.util.HashMap;

public class CedarNodePermissions {

  private CedarUserExtract owner;
  private Map<String, CedarNodeUserPermission> userPermissions;
  private Map<String, CedarNodeGroupPermission> groupPermissions;


  public CedarNodePermissions() {
    userPermissions = new HashMap<>();
    groupPermissions = new HashMap<>();
  }

  public CedarUserExtract getOwner() {
    return owner;
  }

  public void setOwner(CedarUserExtract owner) {
    this.owner = owner;
  }

  public Map<String, CedarNodeUserPermission> getUserPermissions() {
    return userPermissions;
  }

  public void addUserPermissions(CedarNodeUserPermission userPermission) {
    userPermissions.put(userPermission.getUser().getId(), userPermission);
  }

  public Map<String, CedarNodeGroupPermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void addGroupPermissions(CedarNodeGroupPermission groupPermission) {
    groupPermissions.put(groupPermission.getGroup().getGroupId(), groupPermission);
  }
}
