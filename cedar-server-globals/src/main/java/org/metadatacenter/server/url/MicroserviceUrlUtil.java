package org.metadatacenter.server.url;

import org.metadatacenter.config.ServersConfig;

public class MicroserviceUrlUtil {

  private final WorkspaceMicroserviceUrlProvider workspace;
  private final UserMicroserviceUrlProvider user;
  private final TemplateMicroserviceUrlProvider template;
  private final MessagingMicroserviceUrlProvider messaging;
  private final ValuerecommenderMicroserviceUrlProvider valuerecommender;

  public MicroserviceUrlUtil(ServersConfig servers) {
    workspace = new WorkspaceMicroserviceUrlProvider(servers.getWorkspace());
    user = new UserMicroserviceUrlProvider(servers.getUser());
    template = new TemplateMicroserviceUrlProvider(servers.getTemplate());
    messaging = new MessagingMicroserviceUrlProvider(servers.getMessaging());
    valuerecommender = new ValuerecommenderMicroserviceUrlProvider(servers.getValuerecommender());
  }

  public WorkspaceMicroserviceUrlProvider getWorkspace() {
    return workspace;
  }

  public UserMicroserviceUrlProvider getUser() {
    return user;
  }

  public TemplateMicroserviceUrlProvider getTemplate() {
    return template;
  }

  public MessagingMicroserviceUrlProvider getMessaging() {
    return messaging;
  }

  public ValuerecommenderMicroserviceUrlProvider getValuerecommender() {
    return valuerecommender;
  }
}
