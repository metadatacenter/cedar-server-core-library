package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;

public class AuthorizationTypeNotFoundException extends CedarAccessException {

  public AuthorizationTypeNotFoundException() {
    super("Authorization type not found.", CedarErrorKey.AUTHORIZATION_TYPE_NOT_FOUND, null);
  }
}
