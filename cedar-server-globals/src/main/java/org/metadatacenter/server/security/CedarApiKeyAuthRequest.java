package org.metadatacenter.server.security;

import org.metadatacenter.constant.HttpConstants;
import org.metadatacenter.server.security.model.IAuthRequest;
import play.mvc.Http;

public class CedarApiKeyAuthRequest implements IAuthRequest {

  private String authHeader;
  private String apiKey;

  private CedarApiKeyAuthRequest() {
  }

  CedarApiKeyAuthRequest(Http.Request request) {
    if (request != null) {
      authHeader = request.getHeader(Http.HeaderNames.AUTHORIZATION);
      if (authHeader != null) {
        if (authHeader.startsWith(HttpConstants.HTTP_AUTH_HEADER_APIKEY_PREFIX)) {
          apiKey = authHeader.substring(HttpConstants.HTTP_AUTH_HEADER_APIKEY_PREFIX.length());
        }
      }
    }
  }

  public CedarApiKeyAuthRequest(String apiKey) {
    this.apiKey = apiKey;
    this.authHeader = HttpConstants.HTTP_AUTH_HEADER_APIKEY_PREFIX + apiKey;
  }

  @Override
  public String getAuthString() {
    return apiKey;
  }

  @Override
  public String getAuthHeader() {
    return authHeader;
  }
}
