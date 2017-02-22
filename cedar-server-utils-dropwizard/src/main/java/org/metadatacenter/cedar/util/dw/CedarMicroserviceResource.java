package org.metadatacenter.cedar.util.dw;

import org.metadatacenter.config.CedarConfig;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;

public abstract class CedarMicroserviceResource {

  protected
  @Context
  UriInfo uriInfo;

  protected
  @Context
  HttpServletRequest request;

  protected
  @Context
  HttpServletResponse response;

  protected final CedarConfig cedarConfig;

  protected CedarMicroserviceResource(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
  }
}
