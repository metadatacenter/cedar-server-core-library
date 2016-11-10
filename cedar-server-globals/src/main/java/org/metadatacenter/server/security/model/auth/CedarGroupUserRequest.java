package org.metadatacenter.server.security.model.auth;

public class CedarGroupUserRequest {

  private NodePermissionUser user;
  private boolean administrator;
  private boolean member;

  public CedarGroupUserRequest() {
  }

  public CedarGroupUserRequest(NodePermissionUser user, boolean administrator, boolean member) {
    this.user = user;
    this.administrator = administrator;
    this.member = member;
  }

  public NodePermissionUser getUser() {
    return user;
  }

  public void setUser(NodePermissionUser user) {
    this.user = user;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public void setAdministrator(boolean administrator) {
    this.administrator = administrator;
  }

  public boolean isMember() {
    return member;
  }

  public void setMember(boolean member) {
    this.member = member;
  }
}
