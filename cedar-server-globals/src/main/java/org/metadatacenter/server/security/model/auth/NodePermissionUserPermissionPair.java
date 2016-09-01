package org.metadatacenter.server.security.model.auth;

public class NodePermissionUserPermissionPair {

  private NodePermissionUser user;
  private NodePermission permission;

  public NodePermissionUserPermissionPair() {
  }

  public NodePermissionUserPermissionPair(NodePermissionUser user, NodePermission permission) {
    this.user = user;
    this.permission = permission;
  }

  public NodePermissionUser getUser() {
    return user;
  }

  public void setUser(NodePermissionUser user) {
    this.user = user;
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

    NodePermissionUserPermissionPair that = (NodePermissionUserPermissionPair) o;

    if (getUser() != null ? !getUser().equals(that.getUser()) : that.getUser() != null) {
      return false;
    }
    return getPermission() == that.getPermission();

  }

  @Override
  public int hashCode() {
    int result = getUser() != null ? getUser().hashCode() : 0;
    result = 31 * result + (getPermission() != null ? getPermission().hashCode() : 0);
    return result;
  }
}
