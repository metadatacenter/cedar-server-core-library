package org.metadatacenter.model.response;

import org.metadatacenter.model.folderserver.CedarFolder;
import org.metadatacenter.model.folderserver.CedarResource;
import org.metadatacenter.model.request.ResourceListRequest;

import java.lang.String;
import java.util.List;
import java.util.Map;

public class ResourceListResponse {

  private ResourceListRequest request;
  private long totalCount;
  private long currentOffset;
  private Map<String, String> paging;
  private List<CedarResource> resources;
  private List<CedarFolder> pathInfo;

  public ResourceListRequest getRequest() {
    return request;
  }

  public void setRequest(ResourceListRequest request) {
    this.request = request;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

  public long getCurrentOffset() {
    return currentOffset;
  }

  public void setCurrentOffset(long currentOffset) {
    this.currentOffset = currentOffset;
  }

  public Map<String, String> getPaging() {
    return paging;
  }

  public void setPaging(Map<String, String> paging) {
    this.paging = paging;
  }

  public List<CedarResource> getResources() {
    return resources;
  }

  public void setResources(List<CedarResource> resources) {
    this.resources = resources;
  }

  public List<CedarFolder> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(List<CedarFolder> pathInfo) {
    this.pathInfo = pathInfo;
  }
}
