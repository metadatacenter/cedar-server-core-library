package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.resourceserver.CedarRSFolder;
import org.metadatacenter.model.resourceserver.CedarRSNode;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RSNodeListResponse extends AbstractNodeListResponse {

  private List<CedarRSNode> resources;
  private List<CedarRSFolder> pathInfo;

  public List<CedarRSNode> getResources() {
    return resources;
  }

  public void setResources(List<CedarRSNode> resources) {
    this.resources = resources;
  }

  public List<CedarRSFolder> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(List<CedarRSFolder> pathInfo) {
    this.pathInfo = pathInfo;
  }
}
