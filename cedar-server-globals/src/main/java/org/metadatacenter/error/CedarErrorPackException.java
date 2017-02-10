package org.metadatacenter.error;

import java.util.Arrays;

public class CedarErrorPackException {

  private StackTraceElement[] stackTrace;
  private String message;
  private String localizedMessage;

  public CedarErrorPackException(Exception e) {
    if (e != null) {
      this.message = e.getMessage();
      this.localizedMessage = e.getLocalizedMessage();
      this.stackTrace = e.getStackTrace();
      Throwable cause = e.getCause();
      if (cause != null) {
        this.message += "\n" + cause.getMessage();
        this.localizedMessage += "\n" + cause.getLocalizedMessage();
        StackTraceElement[] causeStackTrace = cause.getStackTrace();
        StackTraceElement[] result = Arrays.copyOf(stackTrace, stackTrace.length + causeStackTrace.length);
        System.arraycopy(causeStackTrace, 0, result, stackTrace.length, causeStackTrace.length);
        stackTrace = result;
      }
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
