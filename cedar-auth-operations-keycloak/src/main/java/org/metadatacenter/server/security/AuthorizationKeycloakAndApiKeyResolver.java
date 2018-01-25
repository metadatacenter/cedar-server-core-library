package org.metadatacenter.server.security;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.exception.security.*;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.security.model.AuthRequest;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;

public class AuthorizationKeycloakAndApiKeyResolver implements IAuthorizationResolver {

  public AuthorizationKeycloakAndApiKeyResolver() {
  }

  @Override
  public CedarUser getUserAndEnsurePermission(LinkedDataUtil linkedDataUtil, AuthRequest authRequest, CedarPermission
      permission, IUserService userService) throws CedarAccessException {
    CedarUser user = getUser(linkedDataUtil, authRequest, userService);
    if (user == null) {
      throw new CedarUserNotFoundException(new ApiKeyNotFoundException(authRequest.getAuthString()));
    } else {
      String cn = permission.getPermissionName();
      if (user.getPermissions() == null || !user.getPermissions().contains(cn)) {
        throw new PermissionNotOwnedException(cn);
      }
    }
    return user;
  }

  public CedarUser getUser(LinkedDataUtil linkedDataUtil, AuthRequest authRequest, IUserService userService) throws
      CedarAccessException {
    CedarUser user;
    if (authRequest instanceof CedarBearerAuthRequest) {
      user = KeycloakUtils.getUserFromAuthRequest(linkedDataUtil, authRequest, userService);
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
    } else if (authRequest instanceof CedarUnknownAuthRequest) {
      throw new UnknownAuthorizationTypeException(authRequest.getAuthHeader());
    } else {
      throw new AuthorizationNotFoundException();
    }
  }

}
