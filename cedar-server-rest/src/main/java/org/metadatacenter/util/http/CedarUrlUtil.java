package org.metadatacenter.util.http;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;

public abstract class CedarUrlUtil {

  public static String urlEncode(String value) {
    try {
      return URLEncoder.encode(value, "UTF-8");
    } catch (Exception e) {
      // Do nothing
    }
    return null;
  }

  public static String urlDecode(String value) {
    try {
      return URLDecoder.decode(value, "UTF-8");
    } catch (Exception e) {
      // Do nothing
    }
    return null;
  }

  public static String encodeIfNeeded(String uri) throws UnsupportedEncodingException {
    String decodedUri = urlDecode(uri);
    // It is necessary to encode it
    if (uri.compareTo(decodedUri) == 0) {
      return urlEncode(uri);
    }
    // If was already encoded
    else {
      return uri;
    }
  }

  public static URI getIdURI(UriInfo uriInfo, String id) {
    UriBuilder builder = uriInfo.getAbsolutePathBuilder();
    return builder.path(CedarUrlUtil.urlEncode(id)).build();
  }

  public static URI getLocationURI(HttpResponse httpResponse) {
    URI location = null;
    if (httpResponse != null) {
      Header locationHeader = httpResponse.getFirstHeader(HttpHeaders.LOCATION);
      if (locationHeader != null) {
        location = URI.create(locationHeader.getValue());
      }
    }
    return location;
  }


}
