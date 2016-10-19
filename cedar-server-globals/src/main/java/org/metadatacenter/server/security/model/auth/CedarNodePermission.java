package org.metadatacenter.server.security.model.auth;

public abstract class CedarNodePermission {

  protected NodePermission permission;
  protected static final String KEY_SEPARATOR = "|";

  public CedarNodePermission() {
  }

  protected abstract String getObjectId();

  public NodePermission getPermission() {
    return permission;
  }

  public void setPermission(NodePermission permission) {
    this.permission = permission;
  }

  public String getKey() {
    return getKey(getObjectId(), permission);
  }

  public static String getKey(String objectId, NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(objectId);
    sb.append(KEY_SEPARATOR);
    sb.append(permission.getValue());
    return sb.toString();
  }

  public static String getId(String key) {
    if (key != null) {
      int pos = key.indexOf(KEY_SEPARATOR);
      if (pos != -1) {
        return key.substring(0, pos);
      }
    }
    return null;
  }
}
