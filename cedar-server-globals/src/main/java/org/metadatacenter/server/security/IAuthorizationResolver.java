package org.metadatacenter.server.security;

import org.metadatacenter.server.security.exception.CedarAccessException;
import org.metadatacenter.server.security.model.AuthRequest;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;


public interface IAuthorizationResolver {

  CedarUser getUserAndEnsurePermission(AuthRequest request, CedarPermission permission, IUserService userService)
      throws CedarAccessException;

  CedarUser getUser(AuthRequest authRequest, IUserService userService) throws CedarAccessException;

}