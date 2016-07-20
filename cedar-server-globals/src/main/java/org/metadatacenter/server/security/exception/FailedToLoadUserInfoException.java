package org.metadatacenter.server.security.exception;

public class FailedToLoadUserInfoException extends CedarAccessException {

  public FailedToLoadUserInfoException() {
    super("Failed to load user info from the authentication sever", "userInfoLoadFailed", "logout");
  }
}
