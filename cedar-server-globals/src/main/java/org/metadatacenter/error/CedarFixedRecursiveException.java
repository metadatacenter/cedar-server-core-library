package org.metadatacenter.error;

public class CedarFixedRecursiveException extends Exception {
  public CedarFixedRecursiveException(Exception otherException) {
    super(otherException.getMessage());
    this.setStackTrace(otherException.getStackTrace());
  }
}
