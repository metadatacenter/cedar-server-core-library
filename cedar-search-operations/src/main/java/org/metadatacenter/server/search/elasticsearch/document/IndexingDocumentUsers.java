package org.metadatacenter.server.search.elasticsearch.document;

import org.metadatacenter.server.search.permission.IdNodePermissionPair;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;

import java.util.ArrayList;
import java.util.List;

public class IndexingDocumentUsers {

  private final String cid;
  private final List<IdNodePermissionPair> users;

  public IndexingDocumentUsers(CedarNodeMaterializedPermissions permissions) {
    this.cid = permissions.getId();
    users = new ArrayList<>();
    for (String userId : permissions.getUserPermissions().keySet()) {
      users.add(new IdNodePermissionPair(userId, permissions.getUserPermissions().get(userId)));
    }
  }

  public String getCid() {
    return cid;
  }

  public List<IdNodePermissionPair> getUsers() {
    return users;
  }
}
