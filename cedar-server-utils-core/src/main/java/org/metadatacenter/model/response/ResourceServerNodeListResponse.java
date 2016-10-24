package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.resourceserver.ResourceServerFolder;
import org.metadatacenter.model.resourceserver.ResourceServerNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceServerNodeListResponse extends AbstractNodeListResponse {

  private List<ResourceServerNode> resources;
  private List<ResourceServerFolder> pathInfo;

  public List<ResourceServerNode> getResources() {
    return resources;
  }

  public void setResources(List<ResourceServerNode> resources) {
    this.resources = resources;
  }

  public List<ResourceServerFolder> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(List<ResourceServerFolder> pathInfo) {
    this.pathInfo = pathInfo;
  }
}
