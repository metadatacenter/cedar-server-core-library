package org.metadatacenter.server.logging.filter;

import org.metadatacenter.server.logging.AppLogger;
import org.metadatacenter.server.logging.model.AppLogParam;
import org.metadatacenter.server.logging.model.AppLogSubType;
import org.metadatacenter.server.logging.model.AppLogType;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static org.metadatacenter.constant.CedarHeaderParameters.REQUEST_ID_KEY;

@Provider
public class RequestIdGeneratorFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {

    String requestId = requestContext.getHeaderString(REQUEST_ID_KEY);
    String requestIdSource = "new";
    String className = null;
    if (requestId == null) {
      requestId = UUID.randomUUID().toString();
      requestContext.getHeaders().add(REQUEST_ID_KEY, requestId);
    } else {
      requestIdSource = "request";
    }
    requestContext.getMethod();
    List<Object> matchedResources = requestContext.getUriInfo().getMatchedResources();
    if (!matchedResources.isEmpty()) {
      Object o = matchedResources.get(0);
      className = o.getClass().getName();
    }

    AppLogger.message(AppLogType.REQUEST_FILTER, AppLogSubType.START, requestId)
        .param(AppLogParam.REQUEST_ID_SOURCE, requestIdSource)
        .param(AppLogParam.CLASS_NAME, className)
        .param(AppLogParam.HTTP_METHOD, requestContext.getMethod())
        .param(AppLogParam.PATH, requestContext.getUriInfo().getPath())
        .param(AppLogParam.QUERY_PARAMETERS, requestContext.getUriInfo().getQueryParameters())
        .enqueue();
  }
}
