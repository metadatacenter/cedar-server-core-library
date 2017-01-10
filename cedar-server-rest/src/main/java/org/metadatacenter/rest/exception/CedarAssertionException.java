package org.metadatacenter.rest.exception;

import org.metadatacenter.error.CedarAssertionResult;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.operation.CedarOperationDescriptor;

public class CedarAssertionException extends CedarException {

  public static final String ERROR_CODE = "errorCode";
  public static final String SUGGESTED_ACTION = "suggestedAction";
  public static final String ERROR_PARAMS = "errorParams";
  public static final String MESSAGE = "message";
  public static final String LOCALIZED_MESSAGE = "localizedMessage";
  public static final String STRING = "string";
  public static final String STACK_TRACE = "stackTrace";
  public static final String OPERATION = "operation";
  public static final String SOURCE_STACK_TRACE = "sourceStackTrace";

  private CedarAssertionResult result;
  private CedarOperationDescriptor operation;

  public CedarAssertionException(String message) {
    super(message);
  }

  /*public CedarAssertionException(CedarAssertionResult result, CedarOperationDescriptor operation,
                                 Exception sourceException) {
    this.errorPack = result.getErrorPack();
    errorPack.setOperation(operation);
    errorPack.setSourceException(sourceException);

    this(result != null ? result.getMessage() : sourceException != null ? sourceException.getMessage() : "");
    this.result = result;
    if (result != null) {
      this.status = result.getStatus();
    }
  }*/

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