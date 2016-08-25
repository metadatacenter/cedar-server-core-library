package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarGroupExtract;

public class CedarNodeGroupPermission {

  private CedarGroupExtract group;
  private NodePermission permission;

  public CedarNodeGroupPermission() {
  }

  public CedarNodeGroupPermission(CedarGroupExtract group, NodePermission permission) {
    this.group = group;
    this.permission = permission;
  }

  public CedarGroupExtract getGroup() {
    return group;
  }

  public void setGroup(CedarGroupExtract group) {
    this.group = group;
  }

  public NodePermission getPermission() {
    return permission;
  }

  public void setPermission(NodePermission permission) {
    this.permission = permission;
  }
}
