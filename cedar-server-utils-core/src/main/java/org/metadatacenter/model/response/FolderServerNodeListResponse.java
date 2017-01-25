package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.request.NodeListQueryType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerNodeListResponse extends AbstractNodeListResponse {

  private List<FolderServerNode> resources;
  private List<FolderServerFolder> pathInfo;
  private NodeListQueryType nodeListQueryType;

  public List<FolderServerNode> getResources() {
    return resources;
  }

  public void setResources(List<FolderServerNode> resources) {
    this.resources = resources;
  }

  public List<FolderServerFolder> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(List<FolderServerFolder> pathInfo) {
    this.pathInfo = pathInfo;
  }

  public NodeListQueryType getNodeListQueryType() {
    return nodeListQueryType;
  }

  public void setNodeListQueryType(NodeListQueryType nodeListQueryType) {
    this.nodeListQueryType = nodeListQueryType;
  }
}
