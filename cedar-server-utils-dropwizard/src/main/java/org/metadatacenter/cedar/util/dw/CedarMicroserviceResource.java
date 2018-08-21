package org.metadatacenter.cedar.util.dw;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.url.MicroserviceUrlUtil;
import org.metadatacenter.util.provenance.ProvenanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Produces(MediaType.APPLICATION_JSON)
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

  private static final Logger log = LoggerFactory.getLogger(CedarMicroserviceResource.class);

  protected final CedarConfig cedarConfig;
  protected final LinkedDataUtil linkedDataUtil;
  protected final MicroserviceUrlUtil microserviceUrlUtil;
  protected final ProvenanceUtil provenanceUtil;

  protected CedarMicroserviceResource(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
    linkedDataUtil = cedarConfig.getLinkedDataUtil();
    microserviceUrlUtil = cedarConfig.getMicroserviceUrlUtil();
    provenanceUtil = new ProvenanceUtil();
  }

}
