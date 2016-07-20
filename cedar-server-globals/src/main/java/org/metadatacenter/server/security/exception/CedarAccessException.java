package org.metadatacenter.server.security.exception;


public class CedarAccessException extends Exception {

  private final String errorCode;
  private final String suggestedAction;

  public CedarAccessException(String message, String errorCode, String suggestedAction) {
    super(message);
    this.errorCode = errorCode;
    this.suggestedAction = suggestedAction;
  }

  public CedarAccessException(String message, String errorCode, String suggestedAction, Throwable t) {
    super(message, t);
    this.errorCode = errorCode;
    this.suggestedAction = suggestedAction;
  }

  public String getSuggestedAction() {
    return suggestedAction;
  }

  public String getErrorCode() {
    return errorCode;
  }
}
