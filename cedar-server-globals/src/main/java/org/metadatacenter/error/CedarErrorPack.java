package org.metadatacenter.error;

import org.metadatacenter.operation.CedarOperationDescriptor;

import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

public class CedarErrorPack {

  private Response.Status status = Response.Status.INTERNAL_SERVER_ERROR;
  private CedarErrorType errorType = CedarErrorType.NONE;
  private CedarErrorKey errorKey = CedarErrorKey.NONE;
  private CedarErrorReasonKey errorReasonKey = CedarErrorReasonKey.NONE;
  private String message;
  private final Map<String, Object> parameters;
  private CedarSuggestedAction suggestedAction = CedarSuggestedAction.NONE;
  private CedarErrorPackException sourceException;
  private CedarOperationDescriptor operation;

  public CedarErrorPack() {
    parameters = new HashMap<>();
  }

  public CedarErrorPack(CedarErrorPack other) {
    this();
    if (other != null) {
      this.status = other.getStatus();
      errorType = other.getErrorType();
      errorKey = other.getErrorKey();
      errorReasonKey = other.getErrorReasonKey();
      message = other.getMessage();
      parameters.putAll(other.getParameters());
      suggestedAction = other.getSuggestedAction();
      sourceException = other.getSourceException();
      operation = other.getOperation();
    }
  }

  public Response.Status getStatus() {
    return status;
  }

  public void setStatus(Response.Status status) {
    this.status = status;
  }

  public CedarErrorType getErrorType() {
    return errorType;
  }

  public void setErrorType(CedarErrorType errorType) {
    this.errorType = errorType;
  }

  public CedarErrorKey getErrorKey() {
    return errorKey;
  }

  public void setErrorKey(CedarErrorKey errorKey) {
    this.errorKey = errorKey;
  }

  public CedarErrorReasonKey getErrorReasonKey() {
    return errorReasonKey;
  }

  public void setErrorReasonKey(CedarErrorReasonKey errorReasonKey) {
    this.errorReasonKey = errorReasonKey;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public void setParameter(String name, Object value) {
    this.parameters.put(name, value);
  }

  public CedarSuggestedAction getSuggestedAction() {
    return suggestedAction;
  }

  public void setSuggestedAction(CedarSuggestedAction suggestedAction) {
    this.suggestedAction = suggestedAction;
  }

  public CedarErrorPackException getSourceException() {
    return sourceException;
  }

  public void setSourceException(Exception sourceException) {
    this.sourceException = new CedarErrorPackException(sourceException);
  }

  public void setSourceException(CedarErrorPackException sourceException) {
    this.sourceException = sourceException;
  }

  public CedarOperationDescriptor getOperation() {
    return operation;
  }

  public void setOperation(CedarOperationDescriptor operation) {
    this.operation = operation;
  }
}
