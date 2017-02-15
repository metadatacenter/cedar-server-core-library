package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;

import javax.ws.rs.core.Response;

public class AuthorizationTypeNotFoundException extends CedarAccessException {

  public AuthorizationTypeNotFoundException() {
    super("Authorization type not found.", CedarErrorKey.AUTHORIZATION_TYPE_NOT_FOUND, CedarSuggestedAction.LOGOUT);
    errorPack.setStatus(Response.Status.FORBIDDEN);
  }
}
