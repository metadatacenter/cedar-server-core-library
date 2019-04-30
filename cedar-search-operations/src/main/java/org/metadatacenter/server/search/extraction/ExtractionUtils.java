package org.metadatacenter.server.search.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.constant.HttpConstants;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.util.http.CedarUrlUtil;
import org.metadatacenter.util.http.ProxyUtil;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

/**
 * Utilities used to extract information from CEDAR artifacts
 */
public class ExtractionUtils {

  private final CedarConfig cedarConfig;

  public ExtractionUtils(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
  }

  public JsonNode getArtifactById(String artifactId, CedarNodeType nodeType,
                                  CedarRequestContext requestContext) throws CedarProcessingException {
    String url =
        cedarConfig.getMicroserviceUrlUtil().getArtifact().getNodeType(nodeType) + "/"
            + CedarUrlUtil.urlEncode(artifactId);
    HttpResponse proxyResponse = ProxyUtil.proxyGet(url, requestContext);
    HttpEntity entity = proxyResponse.getEntity();
    if (proxyResponse.getStatusLine().getStatusCode() == HttpConstants.OK && entity != null) {
      String artifactString = null;
      JsonNode artifactJson = null;
      try {
        artifactString = EntityUtils.toString(entity);
        artifactJson = JsonMapper.MAPPER.readTree(artifactString);
      } catch (IOException e) {
        throw new CedarProcessingException("Error when reading artifact as Json: " + artifactId);
      }
      return artifactJson;
    } else {
      throw new CedarProcessingException("Error when retrieving artifact: " + artifactId);
    }
  }

}
