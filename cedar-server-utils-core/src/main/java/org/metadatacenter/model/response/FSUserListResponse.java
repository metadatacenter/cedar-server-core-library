package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.folderserver.CedarFSUser;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FSUserListResponse {

  private List<CedarFSUser> users;

  public FSUserListResponse() {
    this.users = new ArrayList<>();
  }

  public List<CedarFSUser> getUsers() {
    return users;
  }

  public void setUsers(List<CedarFSUser> users) {
    this.users = users;
  }
}
