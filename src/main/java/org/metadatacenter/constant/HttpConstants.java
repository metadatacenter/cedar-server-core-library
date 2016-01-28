package org.metadatacenter.constant;

public final class HttpConstants {

  private HttpConstants() {
  }

  // HTTP headers
  public final static String HTTP_HEADER_LOCATION = "Location";
  public final static String HTTP_HEADER_LINK = "Link";
  public final static String HTTP_CUSTOM_HEADER_TOTAL_COUNT = "Total-Count";
  public final static String HTTP_HEADER_HOST = "Host";

  // HTTP Link header types
  public final static String HEADER_LINK_TYPE_FIRST = "first";
  public final static String HEADER_LINK_TYPE_LAST = "last";
  public final static String HEADER_LINK_TYPE_PREV = "prev";
  public final static String HEADER_LINK_TYPE_NEXT = "next";

}
