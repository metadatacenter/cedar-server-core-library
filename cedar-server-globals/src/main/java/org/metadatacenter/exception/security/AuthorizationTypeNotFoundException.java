package org.metadatacenter.exception.security;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.error.CedarSuggestedAction;

import javax.ws.rs.core.Response;

public class AuthorizationTypeNotFoundException extends CedarAccessException {

  public AuthorizationTypeNotFoundException() {
    super("Authorization not found.", CedarErrorKey.AUTHORIZATION_TYPE_NOT_FOUND, CedarSuggestedAction
        .PROVIDE_AUTHORIZATION_HEADER);
    errorPack.status(Response.Status.FORBIDDEN);
  }
}
