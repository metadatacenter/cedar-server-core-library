package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.Map;
import java.util.HashMap;

public class CedarNodePermissions {

  private CedarUserExtract owner;
  private Map<String, CedarNodePermission> userPermissions;


  public CedarNodePermissions() {
    userPermissions = new HashMap<>();
  }

  public CedarUserExtract getOwner() {
    return owner;
  }

  public void setOwner(CedarUserExtract owner) {
    this.owner = owner;
  }

  public Map<String, CedarNodePermission> getUserPermissions() {
    return userPermissions;
  }

  public void addUserPermissions(CedarNodePermission userPermission) {
    userPermissions.put(userPermission.getUser().getUserId(), userPermission);
  }
}
