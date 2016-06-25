package org.metadatacenter.server.security.exception;

public class CedarUserNotFoundException extends CedarAccessException {

  public CedarUserNotFoundException(Throwable cause) {
    super("Cedar user not found", "cedarUserNotFound", null, cause);
  }
}
