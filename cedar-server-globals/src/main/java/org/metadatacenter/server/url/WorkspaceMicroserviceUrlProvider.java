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

  public String getFolderWithId(String id) {
    return base + CedarNodeType.Prefix.FOLDERS + "/" + UrlUtil.urlEncode(id);
  }

  public String getResourceWithId(String id) {
    return base + PREFIX_RESOURCES + "/" + UrlUtil.urlEncode(id);
  }

  public String getFolders() {
    return base + CedarNodeType.Prefix.FOLDERS;
  }

  public String getResourceWithIdPermissions(String id) {
    return base + PREFIX_RESOURCES + "/" + UrlUtil.urlEncode(id) + "/permissions";
  }

  public String getBase() {
    return base;
  }

  public String getCommand(String command) {
    return base + "command/" + command;
  }

  public String getFolderWithIdPermissions(String id) {
    return base + CedarNodeType.Prefix.FOLDERS + "/" + UrlUtil.urlEncode(id) + "/permissions";
  }

  public String getNodes() {
    return base + PREFIX_NODES;
  }

  public String getUsers() {
    return users;
  }

  public String getResourceWithIdReport(String id) {
    return base + PREFIX_RESOURCES + "/" + UrlUtil.urlEncode(id) + "/report";
  }
}
