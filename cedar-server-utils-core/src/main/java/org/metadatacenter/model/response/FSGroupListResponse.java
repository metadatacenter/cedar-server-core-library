package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.folderserver.CedarFSGroup;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FSGroupListResponse {

  private List<CedarFSGroup> groups;

  public FSGroupListResponse() {
    this.groups = new ArrayList<>();
  }

  public List<CedarFSGroup> getGroups() {
    return groups;
  }

  public void setGroups(List<CedarFSGroup> groups) {
    this.groups = groups;
  }
}
