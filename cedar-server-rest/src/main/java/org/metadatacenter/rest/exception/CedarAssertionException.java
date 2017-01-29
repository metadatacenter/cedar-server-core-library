package org.metadatacenter.rest.exception;

import org.metadatacenter.error.CedarAssertionResult;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.operation.CedarOperationDescriptor;

public class CedarAssertionException extends CedarException {

  public CedarAssertionException(String message) {
    super(message);
  }

  public CedarAssertionException(CedarAssertionResult result, CedarOperationDescriptor operation) {
    super(result.getErrorPack());
    if (operation != null) {
      this.errorPack.setOperation(operation);
    }
  }

  public CedarAssertionException(CedarAssertionResult result) {
    super(result.getErrorPack());
  }

  public CedarAssertionException(Exception sourceException) {
    super(sourceException);
  }

}