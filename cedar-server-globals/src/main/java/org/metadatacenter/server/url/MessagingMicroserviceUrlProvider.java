package org.metadatacenter.server.url;

import org.metadatacenter.config.ServerConfig;
import org.metadatacenter.model.CedarNodeType;

public class MessagingMicroserviceUrlProvider extends MicroserviceUrlProvider {

  public MessagingMicroserviceUrlProvider(ServerConfig server) {
    super(server.getBase());
  }

  public String getMessages() {
    return base + "/" + CedarNodeType.Prefix.MESSAGES;
  }
}
