package org.metadatacenter.constant;

public final class HttpConstants {

  private HttpConstants() {
  }

  // HTTP headers
  public static final String HTTP_HEADER_LOCATION = "Location";
  public static final String HTTP_HEADER_LINK = "Link";
  public static final String HTTP_CUSTOM_HEADER_TOTAL_COUNT = "Total-Count";
  public static final String HTTP_HEADER_HOST = "Host";

  // HTTP Link header types
  public static final String HEADER_LINK_TYPE_FIRST = "first";
  public static final String HEADER_LINK_TYPE_LAST = "last";
  public static final String HEADER_LINK_TYPE_PREV = "prev";
  public static final String HEADER_LINK_TYPE_NEXT = "next";

}
