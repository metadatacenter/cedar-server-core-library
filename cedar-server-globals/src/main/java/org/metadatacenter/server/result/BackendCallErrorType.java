package org.metadatacenter.server.result;

public enum BackendCallErrorType {
  NOT_FOUND("notFound"),
  INVALID_ARGUMENT("invalidArgument"),
  AUTHENTICATION("authentication"),
  AUTHORIZATION("authorization"),
  SERVER_ERROR("server");

  private final String value;

  BackendCallErrorType(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
