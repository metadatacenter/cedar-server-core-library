package org.metadatacenter.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerUserListResponse {

  private List<FolderServerUser> users;

  public FolderServerUserListResponse() {
    this.users = new ArrayList<>();
  }

  public List<FolderServerUser> getUsers() {
    return users;
  }

  public void setUsers(List<FolderServerUser> users) {
    this.users = users;
  }
}
