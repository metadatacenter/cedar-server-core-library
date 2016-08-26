package org.metadatacenter.server.security.model.auth;

public class NodePermissionGroupIdPair {

  private String groupId;
  private NodePermission permission;

  public NodePermissionGroupIdPair() {
  }

  public NodePermissionGroupIdPair(String groupId, NodePermission permission) {
    this.groupId = groupId;
    this.permission = permission;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
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

    NodePermissionGroupIdPair that = (NodePermissionGroupIdPair) o;

    if (getGroupId() != null ? !getGroupId().equals(that.getGroupId()) : that.getGroupId() != null) {
      return false;
    }
    return getPermission() == that.getPermission();

  }

  @Override
  public int hashCode() {
    int result = getGroupId() != null ? getGroupId().hashCode() : 0;
    result = 31 * result + (getPermission() != null ? getPermission().hashCode() : 0);
    return result;
  }
}
