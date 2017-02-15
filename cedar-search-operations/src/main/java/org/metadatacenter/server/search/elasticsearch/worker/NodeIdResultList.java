package org.metadatacenter.server.search.elasticsearch.worker;

import java.util.ArrayList;
import java.util.List;

public class NodeIdResultList {

  private List<String> ids;
  private long totalCount;

  public NodeIdResultList() {
    ids = new ArrayList<>();
  }

  public void addId(String id) {
    ids.add(id);
  }

  public List<String> getIds() {
    return ids;
  }

  public long getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(long totalCount) {
    this.totalCount = totalCount;
  }

}
