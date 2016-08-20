package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarUserExtract;

public class CedarNodePermission {

  private CedarUserExtract user;
  private NodePermission permission;

  public CedarNodePermission() {
  }

  public CedarNodePermission(CedarUserExtract user, NodePermission permission) {
    this.user = user;
    this.permission = permission;
  }

  public CedarUserExtract getUser() {
    return user;
  }

  public void setUser(CedarUserExtract user) {
    this.user = user;
  }

  public NodePermission getPermission() {
    return permission;
  }

  public void setPermission(NodePermission permission) {
    this.permission = permission;
  }
}
