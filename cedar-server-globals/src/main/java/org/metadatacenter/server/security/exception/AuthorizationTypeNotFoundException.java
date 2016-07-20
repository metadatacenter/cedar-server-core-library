package org.metadatacenter.server.security.exception;

public class AuthorizationTypeNotFoundException extends CedarAccessException {

  public AuthorizationTypeNotFoundException() {
    super("Authorization type not found.", "authorizationTypeNotFound", null);
  }
}
