package org.metadatacenter.server.security.exception;

public class MissingPermissionException extends CedarAccessException {

  private String permissionName;

  public MissingPermissionException(String permissionName) {
    super("Missing permission named: " + permissionName, "permissionMissing", "requestRole");
    this.permissionName = permissionName;
  }

  public String getPermissionName() {
    return permissionName;
  }
}
