package org.metadatacenter.rest.exception;

public class CedarProcessingException extends CedarAssertionException {

  public CedarProcessingException(Exception e, String message) {
    super(message);
    this.sourceException = e;
  }

  public CedarProcessingException(Exception e) {
    super(e);
  }

  public CedarProcessingException(String message) {
    super(message);
  }
}
