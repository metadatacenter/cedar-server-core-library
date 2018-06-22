package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.folderserverextract.FolderServerFolderExtract;
import org.metadatacenter.model.folderserverextract.FolderServerNodeExtract;
import org.metadatacenter.model.request.NodeListQueryType;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerNodeListResponse extends AbstractNodeListResponse {

  private List<FolderServerNodeExtract> resources;
  private List<FolderServerFolderExtract> pathInfo;
  private NodeListQueryType nodeListQueryType;

  public List<FolderServerNodeExtract> getResources() {
    return resources;
  }

  public void setResources(List<FolderServerNodeExtract> resources) {
    this.resources = resources;
  }

  public List<FolderServerFolderExtract> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(List<FolderServerFolderExtract> pathInfo) {
    this.pathInfo = pathInfo;
  }

  public NodeListQueryType getNodeListQueryType() {
    return nodeListQueryType;
  }

  public void setNodeListQueryType(NodeListQueryType nodeListQueryType) {
    this.nodeListQueryType = nodeListQueryType;
  }
}
