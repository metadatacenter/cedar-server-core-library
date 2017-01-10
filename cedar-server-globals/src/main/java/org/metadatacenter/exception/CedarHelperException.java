package org.metadatacenter.exception;

public class CedarHelperException extends Exception {

  public CedarHelperException() {
    super("This CEDAR exception is used to mark the location of the original condition that resulted in an error. "
        + "It is not a real exception!");
  }
}
