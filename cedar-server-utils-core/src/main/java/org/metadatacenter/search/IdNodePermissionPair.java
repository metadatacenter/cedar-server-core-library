package org.metadatacenter.search;

import org.metadatacenter.server.security.model.permission.resource.ResourcePermission;

public class IdNodePermissionPair {

  private String id;
  private ResourcePermission permission;

  public IdNodePermissionPair() {
  }

  public IdNodePermissionPair(String id, ResourcePermission permission) {
    this.id = id;
    this.permission = permission;
  }

  public String getId() {
    return id;
  }

  public ResourcePermission getPermission() {
    return permission;
  }
}
