package org.metadatacenter.server.url;

import org.metadatacenter.config.ServerConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.util.http.UrlUtil;

import java.util.Optional;

import static org.metadatacenter.constant.CedarQueryParameters.QP_FORMAT;
import static org.metadatacenter.constant.CedarQueryParameters.QP_RESOURCE_TYPE;

public class ArtifactMicroserviceUrlProvider extends MicroserviceUrlProvider {

  protected static final String VALIDATE_COMMAND = "command/validate";

  public ArtifactMicroserviceUrlProvider(ServerConfig server) {
    super(server.getBase());
  }

  public String getNodeType(CedarNodeType nodeType) {
    return base + nodeType.getPrefix();
  }

  public String getNodeTypeWithId(CedarNodeType nodeType, String id, Optional<String> format) {
    String f = "";
    if (format.isPresent()) {
      f = "?" + QP_FORMAT + "=" + format.get();
    }
    return base + nodeType.getPrefix() + "/" + UrlUtil.urlEncode(id) + f;
  }

  public String getNodeTypeWithId(CedarNodeType nodeType, String id) {
    return getNodeTypeWithId(nodeType, id, Optional.empty());
  }

  public String getValidateCommand(String resourceType) {
    return String.format("%s%s?%s=%s", base, VALIDATE_COMMAND, QP_RESOURCE_TYPE, resourceType);
  }
}
