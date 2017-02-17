package org.metadatacenter.server.search.permission;

import org.metadatacenter.server.security.model.auth.NodePermission;

public class IdNodePermissionPair {

  private String id;
  private NodePermission permission;

  public IdNodePermissionPair(String id, NodePermission permission) {
    this.id = id;
    this.permission = permission;
  }

  public String getId() {
    return id;
  }

  public NodePermission getPermission() {
    return permission;
  }
}
