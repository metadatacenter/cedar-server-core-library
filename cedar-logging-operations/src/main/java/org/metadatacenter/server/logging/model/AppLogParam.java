package org.metadatacenter.server.logging.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum AppLogParam {

  CLASS_NAME("className"),
  METHOD_NAME("methodName"),
  REQUEST_ID("requestId"),
  REQUEST_ID_SOURCE("requestIdSource"),
  QUERY_PARAMETERS("queryParameters"),
  HTTP_METHOD("httpMethod"),
  PATH("path"),
  ORIGINAL_QUERY("originalQuery"),
  RUNNABLE_QUERY("runnableQuery"),
  INTERPOLATED_QUERY("interpolatedQuery"),
  RUNNABLE_QUERY_HASH("runnableQueryHash"),
  QUERY_PARAMETERS_HASH("queryParametersHash"),
  START_TIME("startTime"),
  OPERATION("operation");

  private final String value;

  AppLogParam(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
