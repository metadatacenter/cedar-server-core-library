package org.metadatacenter.exception;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorPack;
import org.metadatacenter.error.CedarErrorPackException;
import org.metadatacenter.error.CedarErrorReasonKey;

public abstract class CedarException extends Exception {

  protected CedarErrorPack errorPack;

  private CedarException() {
  }

  public CedarException(String message, Exception sourceException) {
    super(message, sourceException);
    errorPack = new CedarErrorPack();
    errorPack.setMessage(message);
    if (sourceException != null) {
      errorPack.setSourceException(sourceException);
    } else {
      errorPack.setSourceException(new CedarHelperException());
    }
  }

  public CedarException(String message, CedarErrorPackException errorPackException) {
    super(message);
    errorPack = new CedarErrorPack();
    errorPack.setMessage(message);
    if (errorPackException != null) {
      errorPack.setSourceException(errorPackException);
    } else {
      errorPack.setSourceException(new CedarHelperException());
    }
  }

  protected CedarException(String message) {
    this(message, (Exception)null);
  }

  public CedarException(Exception sourceException) {
    this(sourceException.getMessage(), sourceException);
  }

  public CedarException(CedarErrorPack errorPack) {
    this(errorPack.getMessage(), errorPack.getSourceException());
    this.errorPack = new CedarErrorPack(errorPack);
  }

  public CedarErrorPack getErrorPack() {
    return errorPack;
  }

  public CedarException errorKey(CedarErrorKey errorKey) {
    errorPack.setErrorKey(errorKey);
    return this;
  }

  public CedarException errorReasonKey(CedarErrorReasonKey errorReasonKey) {
    errorPack.setErrorReasonKey(errorReasonKey);
    return this;
  }

  public CedarException parameter(String name, Object value) {
    errorPack.setParameter(name, value);
    return this;
  }
}
