package org.metadatacenter.server.url;

import org.metadatacenter.config.WorkspaceServerConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.util.http.UrlUtil;

public class WorkspaceMicroserviceUrlProvider extends MicroserviceUrlProvider {

  protected static final String PREFIX_RESOURCES = "resources";
  protected static final String PREFIX_NODES = "nodes";
  protected final String users;

  public WorkspaceMicroserviceUrlProvider(WorkspaceServerConfig server) {
    super(server.getBase());
    users = server.getUsers();
  }

  public String getResources() {
    return base + PREFIX_RESOURCES;
  }

  public String getFolders() {
    return base + CedarNodeType.Prefix.FOLDERS;
  }

  public String getFolderWithId(String id) {
    return getFolders() + "/" + UrlUtil.urlEncode(id);
  }

  public String getFolderWithIdReport(String id) {
    return getFolderWithId(id) + "/report";
  }

  public String getBase() {
    return base;
  }

  public String getCommand(String command) {
    return base + "command/" + command;
  }

  public String getNodes() {
    return base + PREFIX_NODES;
  }

}
