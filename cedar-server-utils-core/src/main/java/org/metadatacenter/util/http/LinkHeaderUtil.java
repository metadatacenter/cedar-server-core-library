package org.metadatacenter.util.http;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.constant.HttpConstants;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public final class LinkHeaderUtil {

  private LinkHeaderUtil() {
  }

  public static String getPagingLinkHeader(@NonNull String baseUrl, @NonNull Long total, Integer limit, Integer
      offset) {
    if (limit == null) {
      limit = 0;
    }
    if (offset == null) {
      offset = 0;
    }
    StringBuilder links = new StringBuilder();

    if (offset + limit < total) {
      URI next = createOnePagingLink(baseUrl, offset + limit, limit);
      appendPagingLinkHeader(links, next, HttpConstants.HEADER_LINK_TYPE_NEXT);
    }

    URI last = createOnePagingLink(baseUrl, ((total - 1) / limit) * limit, limit);
    appendPagingLinkHeader(links, last, HttpConstants.HEADER_LINK_TYPE_LAST);

    URI first = createOnePagingLink(baseUrl, 0, limit);
    appendPagingLinkHeader(links, first, HttpConstants.HEADER_LINK_TYPE_FIRST);

    if (offset - limit >= 0) {
      URI prev = createOnePagingLink(baseUrl, offset - limit, limit);
      appendPagingLinkHeader(links, prev, HttpConstants.HEADER_LINK_TYPE_PREV);
    }

    return links.toString();
  }

  private static void appendPagingLinkHeader(StringBuilder sb, URI uri, String type) {
    if (uri != null) {
      if (sb.length() > 0) {
        sb.append(",");
      }
      sb.append("<");
      sb.append(uri.toString());
      sb.append(">");
      sb.append("; rel=\"");
      sb.append(type);
      sb.append("\"");
    }
  }

  private static URI createOnePagingLink(@NonNull String baseUrl, long offset, long limit) {
    URI uri = null;
    try {
      URIBuilder ub = new URIBuilder(baseUrl);
      List<NameValuePair> params = new ArrayList<>();
      params.add(new BasicNameValuePair(CedarConstants.PARAM_OFFSET, String.valueOf(offset)));
      params.add(new BasicNameValuePair(CedarConstants.PARAM_LIMIT, String.valueOf(limit)));
      ub.addParameters(params);
      ub.setCharset(StandardCharsets.UTF_8);
      uri = ub.build();
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return uri;
  }

}
