package org.metadatacenter.model.response;

import org.metadatacenter.model.folderserver.CedarFSFolder;
import org.metadatacenter.model.folderserver.CedarFSNode;

import java.util.List;

public class FSNodeListResponse extends AbstractNodeListResponse {

  private List<CedarFSNode> resources;
  private List<CedarFSFolder> pathInfo;

  public List<CedarFSNode> getResources() {
    return resources;
  }

  public void setResources(List<CedarFSNode> resources) {
    this.resources = resources;
  }

  public List<CedarFSFolder> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(List<CedarFSFolder> pathInfo) {
    this.pathInfo = pathInfo;
  }
}
