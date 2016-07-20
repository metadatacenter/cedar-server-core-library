package org.metadatacenter.server.security.exception;

public class InvalidOfflineAccessTokenException extends CedarAccessException {

  public InvalidOfflineAccessTokenException() {
    super("Invalid offline access token", "tokenInvalid", "logout");
  }
}
