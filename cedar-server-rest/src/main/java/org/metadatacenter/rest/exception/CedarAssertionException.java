package org.metadatacenter.rest.exception;

import org.metadatacenter.error.CedarAssertionResult;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.operation.CedarOperationDescriptor;

public class CedarAssertionException extends CedarException {

  public CedarAssertionException(String message) {
    super(message);
  }

  public CedarAssertionException(CedarAssertionResult result, CedarOperationDescriptor operation) {
    super(result.getErrorPack());
    if (operation != null) {
      this.errorPack.operation(operation);
    }
    showFullStackTrace = false;
  }

  public CedarAssertionException(CedarErrorPack errorPack) {
    super(errorPack);
    showFullStackTrace = false;
  }

  public CedarAssertionException(CedarAssertionResult result) {
    super(result.getErrorPack());
    showFullStackTrace = false;
  }

  public CedarAssertionException(Exception sourceException) {
    super(sourceException);
    showFullStackTrace = false;
  }

}