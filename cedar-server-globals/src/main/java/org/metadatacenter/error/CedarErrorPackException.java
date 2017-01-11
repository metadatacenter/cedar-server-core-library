package org.metadatacenter.error;

public class CedarErrorPackException {

  private StackTraceElement[] stackTrace;
  private String message;
  private String localizedMessage;

  public CedarErrorPackException(Exception e) {
    if (e != null) {
      this.message = e.getMessage();
      this.localizedMessage = e.getLocalizedMessage();
      this.stackTrace = e.getStackTrace();
    }
  }

  public StackTraceElement[] getStackTrace() {
    return stackTrace;
  }

  public String getMessage() {
    return message;
  }

  public String getLocalizedMessage() {
    return localizedMessage;
  }
}
