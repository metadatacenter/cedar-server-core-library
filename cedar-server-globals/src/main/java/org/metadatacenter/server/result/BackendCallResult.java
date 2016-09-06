package org.metadatacenter.server.result;

import java.util.ArrayList;
import java.util.List;

public class BackendCallResult {

  private List<BackendCallError> errors;

  public BackendCallResult() {
    this.errors = new ArrayList<>();
  }

  public boolean isOk() {
    return !isError();
  }

  public boolean isError() {
    return !errors.isEmpty();
  }

  public BackendCallError addError(BackendCallErrorType type) {
    BackendCallError e = new BackendCallError(type);
    errors.add(e);
    return e;
  }
}
