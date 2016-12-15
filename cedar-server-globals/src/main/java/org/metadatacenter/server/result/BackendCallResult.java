package org.metadatacenter.server.result;

import org.metadatacenter.error.CedarErrorType;

import java.util.ArrayList;
import java.util.List;

public class BackendCallResult {

  private final List<BackendCallError> errors;

  public BackendCallResult() {
    this.errors = new ArrayList<>();
  }

  public boolean isOk() {
    return !isError();
  }

  public boolean isError() {
    return !errors.isEmpty();
  }

  public BackendCallError addError(CedarErrorType type) {
    BackendCallError e = new BackendCallError(type);
    errors.add(e);
    return e;
  }

  public String getFirstErrorMessage() {
    if (errors != null) {
      if (errors.get(0) != null) {
        return errors.get(0).getErrorPack().getMessage();
      }
    }
    return null;
  }

  public BackendCallError getFirstError() {
    if (errors != null) {
      if (errors.get(0) != null) {
        return errors.get(0);
      }
    }
    return null;
  }
}
