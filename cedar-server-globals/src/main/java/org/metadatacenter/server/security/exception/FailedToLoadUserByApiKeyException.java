package org.metadatacenter.server.security.exception;

public class FailedToLoadUserByApiKeyException extends CedarAccessException {

  public FailedToLoadUserByApiKeyException(Exception ex) {
    super("Failed to load user info by apiKey", "userInfoLoadByApiKeyFailed", null, ex);
  }
}
