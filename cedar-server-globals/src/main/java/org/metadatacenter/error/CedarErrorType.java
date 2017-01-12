package org.metadatacenter.error;

import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarErrorType {

  NONE(null),
  NOT_FOUND("notFound"),
  INVALID_ARGUMENT("invalidArgument"),
  AUTHENTICATION("authentication"),
  AUTHORIZATION("authorization"),
  SERVER_ERROR("server");

  private final String value;

  CedarErrorType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
