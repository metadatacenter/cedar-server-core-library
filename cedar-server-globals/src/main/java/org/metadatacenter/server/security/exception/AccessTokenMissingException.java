package org.metadatacenter.server.security.exception;

public class AccessTokenMissingException extends CedarAccessException {

  public AccessTokenMissingException() {
    super("Access token is missing.", "tokenMissing", "logout");
  }
}
