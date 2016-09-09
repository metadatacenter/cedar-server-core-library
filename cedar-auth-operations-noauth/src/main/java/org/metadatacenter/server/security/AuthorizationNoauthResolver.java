package org.metadatacenter.server.security;

import org.metadatacenter.server.security.exception.CedarAccessException;
import org.metadatacenter.server.security.model.IAuthRequest;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;

public class AuthorizationNoauthResolver implements IAuthorizationResolver {

  @Override
  public CedarUser getUserAndEnsurePermission(IAuthRequest authRequest, CedarPermission permission, IUserService
      userService) throws CedarAccessException {
    return null;
  }

  @Override
  public CedarUser getUser(IAuthRequest authRequest, IUserService userService) throws CedarAccessException {
    return null;
  }

}