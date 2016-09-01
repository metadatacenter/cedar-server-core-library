package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.ArrayList;
import java.util.List;

public class CedarNodePermissions {

  private CedarUserExtract owner;
  private List<CedarNodeUserPermission> userPermissions;
  private List<CedarNodeGroupPermission> groupPermissions;


  public CedarNodePermissions() {
    userPermissions = new ArrayList<>();
    groupPermissions = new ArrayList<>();
  }

  public CedarUserExtract getOwner() {
    return owner;
  }

  public void setOwner(CedarUserExtract owner) {
    this.owner = owner;
  }

  public List<CedarNodeUserPermission> getUserPermissions() {
    return userPermissions;
  }

  public void addUserPermissions(CedarNodeUserPermission userPermission) {
    userPermissions.add(userPermission);
  }

  public List<CedarNodeGroupPermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void addGroupPermissions(CedarNodeGroupPermission groupPermission) {
    groupPermissions.add(groupPermission);
  }
}
