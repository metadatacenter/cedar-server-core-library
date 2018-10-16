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
  private final Map<String, Object> entities;
  private CedarSuggestedAction suggestedAction = CedarSuggestedAction.NONE;
  private Exception originalException;
  private CedarErrorPackException sourceException;
  private CedarOperationDescriptor operation;

  public CedarErrorPack() {
    parameters = new HashMap<>();
    entities = new HashMap<>();
  }

  public CedarErrorPack(CedarErrorPack other) {
    this();
    if (other != null) {
      status = other.getStatus();
      errorType = other.getErrorType();
      errorKey = other.getErrorKey();
      errorReasonKey = other.getErrorReasonKey();
      message = other.getMessage();
      parameters.putAll(other.getParameters());
      entities.putAll(other.getEntities());
      suggestedAction = other.getSuggestedAction();
      originalException = other.getOriginalException();
      sourceException = other.getSourceException();
      operation = other.getOperation();
    }
  }

  public void merge(CedarErrorPack other) {
    if (other.status != null) {
      status = other.getStatus();
    }
    if (other.errorType != null) {
      errorType = other.getErrorType();
    }
    if (other.errorKey != null) {
      errorKey = other.getErrorKey();
    }
    if (other.errorReasonKey != null) {
      errorReasonKey = other.getErrorReasonKey();
    }
    if (other.message != null) {
      message = other.getMessage();
    }
    if (!other.parameters.isEmpty()) {
      parameters.clear();
      parameters.putAll(other.getParameters());
    }
    if (!other.entities.isEmpty()) {
      entities.clear();
      entities.putAll(other.getEntities());
    }
    if (other.suggestedAction != null) {
      suggestedAction = other.getSuggestedAction();
    }
    if (other.originalException != null) {
      originalException = other.getOriginalException();
    }
    if (other.sourceException != null) {
      sourceException = other.getSourceException();
    }
    if (other.operation != null) {
      operation = other.getOperation();
    }
  }

  public Response.Status getStatus() {
    return status;
  }

  public CedarErrorPack status(Response.Status status) {
    this.status = status;
    return this;
  }

  public CedarErrorType getErrorType() {
    return errorType;
  }

  public CedarErrorPack errorType(CedarErrorType errorType) {
    this.errorType = errorType;
    return this;
  }

  public CedarErrorKey getErrorKey() {
    return errorKey;
  }

  public CedarErrorPack errorKey(CedarErrorKey errorKey) {
    this.errorKey = errorKey;
    return this;
  }

  public CedarErrorReasonKey getErrorReasonKey() {
    return errorReasonKey;
  }

  public CedarErrorPack errorReasonKey(CedarErrorReasonKey errorReasonKey) {
    this.errorReasonKey = errorReasonKey;
    return this;
  }

  public String getMessage() {
    return message;
  }

  public CedarErrorPack message(String message) {
    this.message = message;
    return this;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  public Map<String, Object> getEntities() {
    return entities;
  }

  public CedarErrorPack parameter(String name, Object value) {
    this.parameters.put(name, value);
    return this;
  }

  public CedarErrorPack entity(String name, Object value) {
    this.entities.put(name, value);
    return this;
  }

  public CedarSuggestedAction getSuggestedAction() {
    return suggestedAction;
  }

  public CedarErrorPack suggestedAction(CedarSuggestedAction suggestedAction) {
    this.suggestedAction = suggestedAction;
    return this;
  }

  public CedarErrorPackException getSourceException() {
    return sourceException;
  }

  public CedarErrorPack sourceException(Exception sourceException) {
    this.originalException = sourceException;
    this.sourceException = new CedarErrorPackException(sourceException);
    return this;
  }

  public Exception getOriginalException() {
    return originalException;
  }

  public void sourceException(CedarErrorPackException sourceException) {
    this.sourceException = sourceException;
  }

  public CedarOperationDescriptor getOperation() {
    return operation;
  }

  public CedarErrorPack operation(CedarOperationDescriptor operation) {
    this.operation = operation;
    return this;
  }

  public void resetSourceException() {
    this.originalException = null;
    this.sourceException = null;
  }

}
