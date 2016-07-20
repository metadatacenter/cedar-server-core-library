package org.metadatacenter.server.security;

import org.metadatacenter.server.security.exception.CedarAccessException;
import org.metadatacenter.server.security.model.IAuthRequest;
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

  public static CedarUser getUserAndEnsurePermission(IAuthRequest authRequest, CedarPermission permission) throws
      CedarAccessException {
    return resolver.getUserAndEnsurePermission(authRequest, permission, userService);
  }

  public static CedarUser getUser(IAuthRequest authRequest) throws CedarAccessException {
    return resolver.getUser(authRequest, userService);
  }

}