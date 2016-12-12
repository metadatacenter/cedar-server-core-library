package org.metadatacenter.server.security;

import org.apache.http.HttpHeaders;

import javax.servlet.http.HttpServletRequestWrapper;

import static org.metadatacenter.constant.HttpConstants.HTTP_AUTH_HEADER_APIKEY_PREFIX;

public class CedarApiKeyHttpServletRequest extends HttpServletRequestWrapper {

  private String authorizationHeader;
  private String apiKey;

  public CedarApiKeyHttpServletRequest(String apiKey) {
    super(null);
    this.apiKey = apiKey;
    this.authorizationHeader = HTTP_AUTH_HEADER_APIKEY_PREFIX + apiKey;
  }

  @Override
  public String getHeader(String name) {
    if (HttpHeaders.AUTHORIZATION.equals(name)) {
      return authorizationHeader;
    }
    throw new IllegalArgumentException(
        "Only the:" + HttpHeaders.AUTHORIZATION + " header should be requested on this class");

  }
}

