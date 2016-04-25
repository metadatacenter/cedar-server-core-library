package org.metadatacenter.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;

import java.util.List;

public class NodeListRequest {
  
  @JsonProperty("resource_types")
  private List<CedarNodeType> resourceTypes;
  private long limit;
  private long offset;
  private List<String> sort;

  public List<CedarNodeType> getResourceTypes() {
    return resourceTypes;
  }

  public void setResourceTypes(List<CedarNodeType> resourceTypes) {
    this.resourceTypes = resourceTypes;
  }

  public long getLimit() {
    return limit;
  }

  public void setLimit(long limit) {
    this.limit = limit;
  }

  public long getOffset() {
    return offset;
  }

  public void setOffset(long offset) {
    this.offset = offset;
  }

  public List<String> getSort() {
    return sort;
  }

  public void setSort(List<String> sort) {
    this.sort = sort;
  }
}
