package org.metadatacenter.server.security;

import org.metadatacenter.constant.HttpConstants;
import org.metadatacenter.server.security.model.IAuthRequest;
import play.mvc.Http;

public class CedarBearerAuthRequest implements IAuthRequest {

  private String authHeader;
  private String tokenString;

  private CedarBearerAuthRequest() {
  }

  CedarBearerAuthRequest(Http.Request request) {
    if (request != null) {
      authHeader = request.getHeader(Http.HeaderNames.AUTHORIZATION);
      if (authHeader != null) {
        if (authHeader.startsWith(HttpConstants.HTTP_AUTH_HEADER_BEARER_PREFIX)) {
          tokenString = authHeader.substring(HttpConstants.HTTP_AUTH_HEADER_BEARER_PREFIX.length());
        }
      }
    }
  }

  @Override
  public String getAuthString() {
    return tokenString;
  }

  @Override
  public String getAuthHeader() {
    return authHeader;
  }
}
