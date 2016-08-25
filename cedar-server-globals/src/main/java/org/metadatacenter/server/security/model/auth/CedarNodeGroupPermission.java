package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarGroupExtract;

public class CedarNodeGroupPermission extends CedarNodePermission {

  private CedarGroupExtract group;

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

  @Override
  protected String getObjectId() {
    return group.getGroupId();
  }

}
