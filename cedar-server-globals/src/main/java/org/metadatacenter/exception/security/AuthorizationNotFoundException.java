package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;

public class AuthorizationNotFoundException extends CedarAccessException {

  public AuthorizationNotFoundException(Exception e, String permissionName) {
    super("Authorization data not found: '" + permissionName + "'.", CedarErrorKey.AUTHORIZATION_NOT_FOUND, null, e);
    errorPack.parameter("permissionName", permissionName);
  }
}
