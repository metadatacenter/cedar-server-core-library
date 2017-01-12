package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarErrorType;
import org.metadatacenter.error.CedarSuggestedAction;
import org.metadatacenter.exception.CedarException;

public class CedarAccessException extends CedarException {

  public CedarAccessException(String message, CedarErrorKey errorKey,
                              CedarSuggestedAction suggestedAction, Exception e) {
    super(message, e);
    errorPack.setErrorType(CedarErrorType.AUTHORIZATION);
    errorPack.setErrorKey(errorKey);
    errorPack.setSuggestedAction(suggestedAction);
  }

  public CedarAccessException(String message, CedarErrorKey errorKey, CedarSuggestedAction suggestedAction) {
    this(message, errorKey, suggestedAction, null);
  }
}
