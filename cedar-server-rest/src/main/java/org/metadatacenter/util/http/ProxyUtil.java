package org.metadatacenter.util.http;

import com.google.common.collect.Lists;
import org.apache.http.Header;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.metadatacenter.constant.CedarHeaderParameters;
import org.metadatacenter.constant.CustomHttpConstants;
import org.metadatacenter.constant.HttpConnectionConstants;
import org.metadatacenter.exception.CedarBadRequestException;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.rest.context.CedarRequestContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ProxyUtil {

  public static final String ZERO_LENGTH = "0";

  private static final List<String> CEDAR_HEADERS = Lists.newArrayList(
      HttpHeaders.CONTENT_TYPE,
      CustomHttpConstants.HEADER_CEDAR_VALIDATION_STATUS,
      CustomHttpConstants.HEADER_CEDAR_VALIDATION_REPORT);

  public static HttpResponse proxyGet(String url, CedarRequestContext context) throws CedarProcessingException {
    Request proxyRequest = Request.Get(url)
        .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
        .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
        .addHeader(HttpHeaders.AUTHORIZATION, context.request().getAuthorizationHeader())
        .addHeader(CedarHeaderParameters.HP_DEBUG, context.request().getDebugHeader());
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
        .addHeader(CedarHeaderParameters.HP_DEBUG, context.request().getDebugHeader())
        .addHeader(HttpHeaders.CONTENT_LENGTH, ZERO_LENGTH)
        .addHeader(HttpHeaders.CONTENT_TYPE, ContentType.TEXT_PLAIN.toString());
    try {
      return proxyRequest.execute().returnResponse();
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
  }

  public static HttpResponse proxyPost(String url, CedarRequestContext context) throws CedarProcessingException,
      CedarBadRequestException {
    return proxyPost(url, context, context.request().getRequestBody().asJsonString());
  }

  public static HttpResponse proxyPost(String url, CedarRequestContext context, String content) throws
      CedarProcessingException {
    Request proxyRequest = Request.Post(url)
        .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
        .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
        .addHeader(HttpHeaders.AUTHORIZATION, context.request().getAuthorizationHeader())
        .addHeader(CedarHeaderParameters.HP_DEBUG, context.request().getDebugHeader())
        .bodyString(content, ContentType.APPLICATION_JSON);
    try {
      return proxyRequest.execute().returnResponse();
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
  }

  public static HttpResponse proxyPut(String url, CedarRequestContext context) throws CedarProcessingException,
      CedarBadRequestException {
    return proxyPut(url, context, context.request().getRequestBody().asJsonString());
  }

  public static HttpResponse proxyPut(String url, CedarRequestContext context, String content) throws
      CedarProcessingException {
    Request proxyRequest = Request.Put(url)
        .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
        .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
        .addHeader(HttpHeaders.AUTHORIZATION, context.request().getAuthorizationHeader())
        .addHeader(CedarHeaderParameters.HP_DEBUG, context.request().getDebugHeader())
        .bodyString(content, ContentType.APPLICATION_JSON);
    try {
      return proxyRequest.execute().returnResponse();
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
  }

  public static void proxyResponseHeaders(HttpResponse proxyResponse, HttpServletResponse response) {
    for (Header header : proxyResponse.getAllHeaders()) {
      if (CEDAR_HEADERS.contains(header.getName())) {
        response.setHeader(header.getName(), header.getValue());
      }
    }
  }

}
