package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarUserExtract;

public class CedarNodeUserPermission extends CedarNodePermission {

  private CedarUserExtract user;

  public CedarNodeUserPermission() {
  }

  public CedarNodeUserPermission(CedarUserExtract user, NodePermission permission) {
    this.user = user;
    this.permission = permission;
  }

  public CedarUserExtract getUser() {
    return user;
  }

  public void setUser(CedarUserExtract user) {
    this.user = user;
  }

  @Override
  protected String getObjectId() {
    return user.getUserId();
  }
}
