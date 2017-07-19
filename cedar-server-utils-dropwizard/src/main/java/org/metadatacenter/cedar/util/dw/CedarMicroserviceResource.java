package org.metadatacenter.cedar.util.dw;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.security.model.user.CedarUserSummary;
import org.metadatacenter.server.url.MicroserviceUrlUtil;
import org.metadatacenter.util.http.ProxyUtil;
import org.metadatacenter.util.json.JsonMapper;
import org.metadatacenter.util.provenance.ProvenanceUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;

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

  protected CedarUserSummary getUserSummary(String id, CedarRequestContext context) throws CedarProcessingException {
    String uuid = extractUserUUID(id);
    String url = microserviceUrlUtil.getUser().UuidSummary(uuid);
    HttpResponse proxyResponse = null;
    try {
      proxyResponse = ProxyUtil.proxyGet(url, context);
      HttpEntity entity = proxyResponse.getEntity();
      if (entity != null) {
        String userSummaryString = EntityUtils.toString(entity);
        if (userSummaryString != null && !userSummaryString.isEmpty()) {
          JsonNode jsonNode = JsonMapper.MAPPER.readTree(userSummaryString);
          JsonNode at = jsonNode.at("/screenName");
          if (at != null && !at.isMissingNode()) {
            CedarUserSummary summary = new CedarUserSummary();
            summary.setScreenName(at.asText());
            summary.setId(id);
            return summary;
          }
        }
      }
    } catch (IOException e) {
      throw new CedarProcessingException(e);
    }
    return null;
  }

  private static String extractUserUUID(String userURL) {
    String id = userURL;
    try {
      int pos = userURL.lastIndexOf('/');
      if (pos > -1) {
        id = userURL.substring(pos + 1);
      }
      id = new URLCodec().encode(id);
    } catch (EncoderException e) {
      log.error("Error while extracting user UUID", e);
    }
    return id;
  }


}
