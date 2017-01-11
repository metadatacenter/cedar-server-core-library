package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;

public class CedarUserNotFoundException extends CedarAccessException {

  public CedarUserNotFoundException(Exception ex) {
    super("CEDAR user not found.", CedarErrorKey.CEDAR_USER_NOT_FOUND, null, ex);
  }
}
