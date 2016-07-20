package org.metadatacenter.server.security.exception;

public class AuthorizationNotFoundException extends CedarAccessException {

  private String permissionName;

  public String getPermissionName() {
    return permissionName;
  }

  public AuthorizationNotFoundException(Exception e, String permissionName) {
    super("Authorization data not found:" + permissionName, "authorizationNotFound", null, e);
    this.permissionName = permissionName;
  }
}
