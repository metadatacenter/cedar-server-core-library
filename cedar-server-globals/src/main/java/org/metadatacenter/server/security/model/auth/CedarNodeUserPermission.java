package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

@JsonIgnoreProperties({"asUserIdPermissionPair", "key"})
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
    return user.getId();
  }

  public NodePermissionUserIdPair getAsUserIdPermissionPair() {
    return new NodePermissionUserIdPair(getUser().getId(), getPermission());
  }
}
