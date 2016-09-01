package org.metadatacenter.server.security.model.auth;

public class NodePermissionGroupPermissionPair {

  private NodePermissionGroup group;
  private NodePermission permission;

  public NodePermissionGroupPermissionPair() {
  }

  public NodePermissionGroupPermissionPair(NodePermissionGroup group, NodePermission permission) {
    this.group = group;
    this.permission = permission;
  }

  public NodePermissionGroup getGroup() {
    return group;
  }

  public void setGroup(NodePermissionGroup group) {
    this.group = group;
  }

  public NodePermission getPermission() {
    return permission;
  }

  public void setPermission(NodePermission permission) {
    this.permission = permission;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    NodePermissionGroupPermissionPair that = (NodePermissionGroupPermissionPair) o;

    if (getGroup() != null ? !getGroup().equals(that.getGroup()) : that.getGroup() != null) {
      return false;
    }
    return getPermission() == that.getPermission();

  }

  @Override
  public int hashCode() {
    int result = getGroup() != null ? getGroup().hashCode() : 0;
    result = 31 * result + (getPermission() != null ? getPermission().hashCode() : 0);
    return result;
  }
}
