package org.metadatacenter.server.result;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.error.CedarErrorType;
import org.metadatacenter.error.CedarSuggestedAction;
import org.metadatacenter.operation.CedarOperationDescriptor;

public class BackendCallError {

  private CedarErrorPack errorPack;

  BackendCallError(CedarErrorType errorType) {
    errorPack = new CedarErrorPack();
    errorPack.setErrorType(errorType);
  }

  public BackendCallError message(String message) {
    errorPack.setMessage(message);
    return this;
  }

  public BackendCallError parameter(String name, Object value) {
    errorPack.setParameter(name, value);
    return this;
  }

  public BackendCallError suggestedAction(CedarSuggestedAction suggestedAction) {
    errorPack.setSuggestedAction(suggestedAction);
    return this;
  }

  public BackendCallError errorKey(CedarErrorKey errorKey) {
    errorPack.setErrorKey(errorKey);
    return this;
  }

  public BackendCallError operation(CedarOperationDescriptor operation) {
    errorPack.setOperation(operation);
    return this;
  }

  public CedarErrorPack getErrorPack() {
    return errorPack;
  }
}
