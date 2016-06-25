package org.metadatacenter.server.security.exception;

public class FailedToLoadUserByTokenException extends CedarAccessException {

  public FailedToLoadUserByTokenException(Exception ex) {
    super("Failed to load user info by token", "userInfoLoadByTokenFailed", null, ex);
  }
}
