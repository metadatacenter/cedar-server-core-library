package org.metadatacenter.server.security.exception;

public class FailedToLoadUserByIdException extends CedarAccessException {

  public FailedToLoadUserByIdException(Exception ex) {
    super("Failed to load user info by id", "userInfoLoadByIdFailed", null, ex);
  }
}
