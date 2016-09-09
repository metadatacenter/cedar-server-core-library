package org.metadatacenter.server.security;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.security.exception.*;
import org.metadatacenter.server.security.model.IAuthRequest;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;

public class AuthorizationKeycloakAndApiKeyResolver implements IAuthorizationResolver {

  public AuthorizationKeycloakAndApiKeyResolver() {
  }

  @Override
  public CedarUser getUserAndEnsurePermission(IAuthRequest authRequest, CedarPermission permission, IUserService
      userService) throws CedarAccessException {
    CedarUser user = getUser(authRequest, userService);
    if (user == null) {
      throw new CedarUserNotFoundException(new ApiKeyNotFoundException(authRequest.getAuthString()));
    } else {
      String cn = permission.getPermissionName();
      if (user.getPermissions() == null || !user.getPermissions().contains(cn)) {
        throw new AuthorizationNotFoundException(null, cn);
      }
    }
    return user;
  }

  public CedarUser getUser(IAuthRequest authRequest, IUserService userService) throws CedarAccessException {
    CedarUser user = null;
    if (authRequest instanceof CedarBearerAuthRequest) {
      user = KeycloakUtils.getUserFromAuthRequest(authRequest, userService);
      if (user == null) {
        throw new CedarUserNotFoundException(new FailedToLoadUserByTokenException(null));
      } else {
        return user;
      }
    } else if (authRequest instanceof CedarApiKeyAuthRequest) {
      try {
        user = userService.findUserByApiKey(authRequest.getAuthString());
      } catch (IOException | ProcessingException e) {
        throw new CedarUserNotFoundException(new FailedToLoadUserByApiKeyException(e));
      }
      if (user == null) {
        throw new CedarUserNotFoundException(new ApiKeyNotFoundException(authRequest.getAuthString()));
      } else {
        return user;
      }
    } else {
      throw new AuthorizationTypeNotFoundException();
    }
  }

}
