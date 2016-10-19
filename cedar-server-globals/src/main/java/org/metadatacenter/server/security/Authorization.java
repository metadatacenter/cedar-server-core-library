package org.metadatacenter.server.security;

import org.metadatacenter.server.security.exception.CedarAccessException;
import org.metadatacenter.server.security.model.AuthRequest;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;

public final class Authorization {

  private static IAuthorizationResolver resolver;
  private static IUserService userService;

  private Authorization() {
  }

  public static void setAuthorizationResolver(IAuthorizationResolver r) {
    resolver = r;
  }

  public static void setUserService(IUserService us) {
    userService = us;
  }

  public static CedarUser getUserAndEnsurePermission(AuthRequest authRequest, CedarPermission permission) throws
      CedarAccessException {
    return resolver.getUserAndEnsurePermission(authRequest, permission, userService);
  }

  public static CedarUser getUser(AuthRequest authRequest) throws CedarAccessException {
    return resolver.getUser(authRequest, userService);
  }

}