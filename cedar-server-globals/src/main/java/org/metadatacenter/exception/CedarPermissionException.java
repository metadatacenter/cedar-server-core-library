package org.metadatacenter.exception;

public class CedarPermissionException extends CedarException {

  public CedarPermissionException(String message, Exception sourceException) {
    super(message, sourceException);
  }

  public CedarPermissionException(Exception sourceException) {
    super(sourceException);
  }

  public CedarPermissionException(String message) {
    super(message);
  }

}
