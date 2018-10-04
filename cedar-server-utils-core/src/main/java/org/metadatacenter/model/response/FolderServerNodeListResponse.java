package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;
import org.metadatacenter.model.request.NodeListQueryType;
import org.metadatacenter.util.FolderServerNodeContext;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerNodeListResponse extends AbstractNodeListResponse {

  private List<? extends FolderServerNodeExtract> resources;
  private List<? extends FolderServerNodeExtract> pathInfo;
  private NodeListQueryType nodeListQueryType;

  public List<? extends FolderServerNodeExtract> getResources() {
    return resources;
  }

  public void setResources(List<? extends FolderServerNodeExtract> resources) {
    this.resources = resources;
  }

  public List<? extends FolderServerNodeExtract> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(List<FolderServerNodeExtract> pathInfo) {
    this.pathInfo = pathInfo;
  }

  public NodeListQueryType getNodeListQueryType() {
    return nodeListQueryType;
  }

  public void setNodeListQueryType(NodeListQueryType nodeListQueryType) {
    this.nodeListQueryType = nodeListQueryType;
  }

  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return FolderServerNodeContext.getContext();
  }
}
