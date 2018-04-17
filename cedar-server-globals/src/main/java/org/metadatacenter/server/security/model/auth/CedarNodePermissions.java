package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.ArrayList;
import java.util.List;

public class CedarNodePermissions {

  private CedarUserExtract owner;
  private final List<CedarNodeUserPermission> userPermissions;
  private final List<CedarNodeGroupPermission> groupPermissions;


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

  public CedarNodePermissionsRequest toRequest() {
    CedarNodePermissionsRequest r = new CedarNodePermissionsRequest();
    // copy owner
    NodePermissionUser newOwner = new NodePermissionUser();
    newOwner.setId(owner.getId());
    r.setOwner(newOwner);
    // copy users
    for (CedarNodeUserPermission up : userPermissions) {
      r.getUserPermissions().add(up.getAsUserIdPermissionPair());
    }
    // copy groups
    for (CedarNodeGroupPermission gp : groupPermissions) {
      r.getGroupPermissions().add(gp.getAsGroupIdPermissionPair());
    }
    return r;
  }
}
