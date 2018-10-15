package org.metadatacenter.server.logging.filter;

import org.metadatacenter.server.logging.AppLogger;
import org.metadatacenter.server.logging.model.AppLogParam;
import org.metadatacenter.server.logging.model.AppLogSubType;
import org.metadatacenter.server.logging.model.AppLogType;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

import static org.metadatacenter.constant.CedarHeaderParameters.REQUEST_ID_KEY;

@Provider
public class RESTLoggerFilter implements ContainerResponseFilter {

  @Override
  public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {
    String requestId = requestContext.getHeaderString(REQUEST_ID_KEY);
    AppLogger.message(AppLogType.REQUEST_FILTER, AppLogSubType.END)
        .param(AppLogParam.REQUEST_ID, requestId)
        .enqueue();
  }
}
