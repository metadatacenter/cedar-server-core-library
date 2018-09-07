package org.metadatacenter.util.http;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public final class UrlUtil {

  private UrlUtil() {
  }

  public static String trimUrlParameters(String url) {
    if (url != null) {
      int p = url.indexOf('?');
      if (p > -1) {
        return url.substring(0, p - 1);
      }
    }
    return url;
  }

  public static String urlEncode(String value) {
    try {
      return URLEncoder.encode(value, StandardCharsets.UTF_8);
    } catch (Exception e) {
      // Do nothing
    }
    return null;
  }

  public static String urlDecode(String value) {
    try {
      return URLDecoder.decode(value, StandardCharsets.UTF_8);
    } catch (Exception e) {
      // Do nothing
    }
    return null;
  }
}
