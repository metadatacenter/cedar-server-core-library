package org.metadatacenter.rest.exception;

import org.metadatacenter.server.result.BackendCallResult;

import java.util.HashMap;
import java.util.Map;

import static org.metadatacenter.constant.HttpConstants.*;

public class CedarAssertionResult {

  private int code;
  private String message;
  private final String errorSubType = "other";
  private final String suggestedAction = "none";
  private final String errorCode = null;
  private final Map<String, Object> parameters;

  public CedarAssertionResult(String message) {
    this.message = message;
    code = HTTP_INTERNAL_SERVER_ERROR;
    parameters = new HashMap<>();
  }

  public CedarAssertionResult(BackendCallResult backendCallResult) {
    this(backendCallResult.getFirstErrorMessage());
    // TODO : implement this. Data from backendCallResult should make into CedarAssertionResult
    // implementation sample is in AbstractCedarController.putErrorDetails
  }

  public CedarAssertionResult internalServerError() {
    code = HTTP_INTERNAL_SERVER_ERROR;
    return this;
  }

  public CedarAssertionResult forbidden() {
    code = HTTP_FORBIDDEN;
    return this;
  }

  public CedarAssertionResult unauthorized() {
    code = HTTP_UNAUTHORIZED;
    return this;
  }

  public CedarAssertionResult notFound() {
    code = HTTP_NOT_FOUND;
    return this;
  }

  public CedarAssertionResult badRequest() {
    code = HTTP_BAD_REQUEST;
    return this;
  }

  public CedarAssertionResult setParameter(String name, Object value) {
    this.parameters.put(name, value);
    return this;
  }

  public void setCode(int code) {
    this.code = code;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public int getCode() {
    return code;
  }

  public String getErrorSubType() {
    return errorSubType;
  }

  public String getSuggestedAction() {
    return suggestedAction;
  }

  public String getErrorCode() {
    return errorCode;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }
}