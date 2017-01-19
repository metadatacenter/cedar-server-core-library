package org.metadatacenter.util.http;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.metadatacenter.constant.HttpConnectionConstants;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.rest.context.CedarRequestContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ProxyUtil {

  public static final String ZERO_LENGTH = "0";

  public static HttpResponse proxyGet(String url, CedarRequestContext context) throws CedarProcessingException {
    Request proxyRequest = Request.Get(url)
        .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
        .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
        .addHeader(HttpHeaders.AUTHORIZATION, context.request().getAuthorizationHeader());
    try {
      return proxyRequest.execute().returnResponse();
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
  }

  public static HttpResponse proxyDelete(String url, CedarRequestContext context) throws CedarProcessingException {
    Request proxyRequest = Request.Delete(url)
        .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
        .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
        .addHeader(HttpHeaders.AUTHORIZATION, context.request().getAuthorizationHeader())
        .addHeader(HttpHeaders.CONTENT_LENGTH, ZERO_LENGTH)
        .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.TEXT_PLAIN.toString());
    try {
      return proxyRequest.execute().returnResponse();
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
  }

  public static HttpResponse proxyPost(String url, CedarRequestContext context) throws CedarProcessingException {
    return proxyPost(url, context, context.request().getRequestBody().asJsonString());
  }

  public static HttpResponse proxyPost(String url, CedarRequestContext context, String content) throws
      CedarProcessingException {
    Request proxyRequest = Request.Post(url)
        .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
        .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
        .addHeader(HttpHeaders.AUTHORIZATION, context.request().getAuthorizationHeader())
        .bodyString(content, ContentType.APPLICATION_JSON);
    try {
      return proxyRequest.execute().returnResponse();
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
  }

  public static HttpResponse proxyPut(String url, CedarRequestContext context) throws CedarProcessingException {
    return proxyPut(url, context, context.request().getRequestBody().asJsonString());
  }

  public static HttpResponse proxyPut(String url, CedarRequestContext context, String content) throws
      CedarProcessingException {
    Request proxyRequest = Request.Put(url)
        .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
        .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
        .addHeader(HttpHeaders.AUTHORIZATION, context.request().getAuthorizationHeader())
        .bodyString(content, ContentType.APPLICATION_JSON);
    try {
      return proxyRequest.execute().returnResponse();
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
  }

  public static void proxyResponseHeaders(HttpResponse proxyResponse, HttpServletResponse response) {
    HeaderIterator headerIterator = proxyResponse.headerIterator();
    while (headerIterator.hasNext()) {
      Header header = headerIterator.nextHeader();
      if (HttpHeaders.CONTENT_TYPE.equals(header.getName())) {
        response.setHeader(header.getName(), header.getValue());
      }
    }
  }

}
