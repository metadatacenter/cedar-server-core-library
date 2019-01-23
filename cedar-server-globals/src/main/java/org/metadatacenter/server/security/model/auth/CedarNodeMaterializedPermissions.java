package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;

import java.util.HashMap;
import java.util.Map;

public class CedarNodeMaterializedPermissions {

  private final String id;
  private final Map<String, NodePermission> userPermissions;
  private final Map<String, NodePermission> groupPermissions;


  public CedarNodeMaterializedPermissions(String id) {
    this.id = id;
    userPermissions = new HashMap<>();
    groupPermissions = new HashMap<>();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  public Map<String, NodePermission> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermission(String id, NodePermission userPermission) {
    userPermissions.put(id, userPermission);
  }

  public Map<String, NodePermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void setGroupPermission(String id, NodePermission groupPermission) {
    groupPermissions.put(id, groupPermission);
  }

  public static String getKey(String userOrGroupId, NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(userOrGroupId);
    sb.append("|");
    sb.append(permission.getValue());
    return DigestUtils.md5Hex(sb.toString());
  }
}
