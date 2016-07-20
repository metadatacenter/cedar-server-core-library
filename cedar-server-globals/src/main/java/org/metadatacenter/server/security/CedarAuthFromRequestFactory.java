package org.metadatacenter.server.security;

import org.metadatacenter.constant.HttpConstants;
import org.metadatacenter.server.security.model.IAuthRequest;
import play.mvc.Http;

public abstract class CedarAuthFromRequestFactory {

  public static IAuthRequest fromRequest(Http.Request request) {
    if (request != null) {
      String auth = request.getHeader(Http.HeaderNames.AUTHORIZATION);
      if (auth != null) {
        if (auth.startsWith(HttpConstants.HTTP_AUTH_HEADER_BEARER_PREFIX)) {
          return new CedarBearerAuthRequest(request);
        } else if (auth.startsWith(HttpConstants.HTTP_AUTH_HEADER_APIKEY_PREFIX)) {
          return new CedarApiKeyAuthRequest(request);
        }
      }
    }
    return new CedarNoAuthRequest();
  }

}
