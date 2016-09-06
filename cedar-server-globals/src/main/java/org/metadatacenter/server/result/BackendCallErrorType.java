package org.metadatacenter.server.result;

public enum BackendCallErrorType {
  INVALID_ARGUMENT("invalidArgument"),
  AUTHENTICATION("authentication"),
  PERMISSION("permission"),
  SERVER_ERROR("server");

  private String value;

  BackendCallErrorType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
