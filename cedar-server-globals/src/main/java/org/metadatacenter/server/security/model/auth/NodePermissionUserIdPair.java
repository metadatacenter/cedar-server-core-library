package org.metadatacenter.server.security.model.auth;

public class NodePermissionUserIdPair {

  private String userId;
  private NodePermission permission;

  public NodePermissionUserIdPair() {
  }

  public NodePermissionUserIdPair(String userId, NodePermission permission) {
    this.userId = userId;
    this.permission = permission;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
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

    NodePermissionUserIdPair that = (NodePermissionUserIdPair) o;

    if (getUserId() != null ? !getUserId().equals(that.getUserId()) : that.getUserId() != null) {
      return false;
    }
    return getPermission() == that.getPermission();

  }

  @Override
  public int hashCode() {
    int result = getUserId() != null ? getUserId().hashCode() : 0;
    result = 31 * result + (getPermission() != null ? getPermission().hashCode() : 0);
    return result;
  }
}
