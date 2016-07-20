package org.metadatacenter.util.http;

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
}
