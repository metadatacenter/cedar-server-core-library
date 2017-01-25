package org.metadatacenter.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;

import java.util.List;

public class NodeListRequest {

  @JsonProperty("resource_types")
  private List<CedarNodeType> nodeTypes;
  private long limit;
  private long offset;
  private List<String> sort;
  private String q;
  @JsonProperty("derived_from_id")
  private String derivedFromId;

  public List<CedarNodeType> getNodeTypes() {
    return nodeTypes;
  }

  public void setNodeTypes(List<CedarNodeType> nodeTypes) {
    this.nodeTypes = nodeTypes;
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

  public String getQ() {
    return q;
  }

  public void setQ(String q) {
    this.q = q;
  }

  public String getDerivedFromId() {
    return derivedFromId;
  }

  public void setDerivedFromId(String derivedFromId) {
    this.derivedFromId = derivedFromId;
  }
}
