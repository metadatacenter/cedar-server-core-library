package org.metadatacenter.server.security;

import org.metadatacenter.server.security.model.AuthRequest;
import play.mvc.Http;

import static org.metadatacenter.constant.HttpConstants.HTTP_AUTH_HEADER_BEARER_PREFIX;
import static org.metadatacenter.constant.HttpConstants.HTTP_HEADER_AUTHORIZATION;

public class CedarBearerAuthRequest implements AuthRequest {

  private String authHeader;
  private String tokenString;

  private CedarBearerAuthRequest() {
  }

  CedarBearerAuthRequest(Http.Request request) {
    if (request != null) {
      authHeader = request.getHeader(HTTP_HEADER_AUTHORIZATION);
      if (authHeader != null) {
        if (authHeader.startsWith(HTTP_AUTH_HEADER_BEARER_PREFIX)) {
          tokenString = authHeader.substring(HTTP_AUTH_HEADER_BEARER_PREFIX.length());
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
