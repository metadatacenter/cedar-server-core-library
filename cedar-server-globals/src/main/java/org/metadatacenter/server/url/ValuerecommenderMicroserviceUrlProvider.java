package org.metadatacenter.server.url;

import org.metadatacenter.config.ServerConfig;
import org.metadatacenter.util.http.UrlUtil;

public class ValuerecommenderMicroserviceUrlProvider extends MicroserviceUrlProvider {

  public ValuerecommenderMicroserviceUrlProvider(ServerConfig server) {
    super(server.getBase());
  }

  public String getCommandGenerateRules(String templateId) {
    return base + "command/generate-rules/" + UrlUtil.urlEncode(templateId);
  }
}
