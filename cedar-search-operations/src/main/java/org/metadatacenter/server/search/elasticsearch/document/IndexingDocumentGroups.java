package org.metadatacenter.server.search.elasticsearch.document;

import org.metadatacenter.server.search.permission.IdNodePermissionPair;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;

import java.util.ArrayList;
import java.util.List;

public class IndexingDocumentGroups {

  private String cid;
  private List<IdNodePermissionPair> groups;

  public IndexingDocumentGroups(CedarNodeMaterializedPermissions permissions) {
    this.cid = permissions.getId();
    groups = new ArrayList<>();
    for (String groupId : permissions.getGroupPermissions().keySet()) {
      groups.add(new IdNodePermissionPair(groupId, permissions.getGroupPermissions().get(groupId)));
    }
  }

  public String getCid() {
    return cid;
  }

  public List<IdNodePermissionPair> getGroups() {
    return groups;
  }
}
