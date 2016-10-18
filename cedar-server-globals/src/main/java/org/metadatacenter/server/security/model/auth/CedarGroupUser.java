package org.metadatacenter.server.security.model.auth;

import org.metadatacenter.server.security.model.user.CedarUserExtract;

public class CedarGroupUser {

  private CedarUserExtract user;
  private boolean administrator;
  private boolean member;

  public CedarGroupUser(CedarUserExtract user, boolean administrator, boolean member) {
    this.user = user;
    this.administrator = administrator;
    this.member = member;
  }

  public CedarUserExtract getUser() {
    return user;
  }

  public void setUser(CedarUserExtract user) {
    this.user = user;
  }

  public boolean isAdministrator() {
    return administrator;
  }

  public boolean isMember() {
    return member;
  }
}
