package org.metadatacenter.constant;

public final class HttpConstants {

  private HttpConstants() {
  }

  // HTTP headers
  public static final String HTTP_HEADER_LINK = "Link";
  public static final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
  public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";
  public static final String HTTP_HEADER_ACCEPT = "Accept";

  // HTTP header prefix
  public final static String HTTP_AUTH_HEADER_BEARER_PREFIX = "Bearer ";
  public final static String HTTP_AUTH_HEADER_APIKEY_PREFIX = "apiKey ";

  // HTTP Link header types
  public static final String HEADER_LINK_TYPE_FIRST = "first";
  public static final String HEADER_LINK_TYPE_LAST = "last";
  public static final String HEADER_LINK_TYPE_PREV = "prev";
  public static final String HEADER_LINK_TYPE_NEXT = "next";

  // Content Types
  public static final String CONTENT_TYPE_APPLICATION_JSON = "application/json";

}
