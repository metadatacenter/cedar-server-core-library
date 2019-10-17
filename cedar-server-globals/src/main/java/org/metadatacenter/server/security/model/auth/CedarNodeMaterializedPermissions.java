package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermission;

import java.util.HashMap;
import java.util.Map;

public class CedarNodeMaterializedPermissions {

  private final String id;
  private final Map<String, ResourcePermission> userPermissions;
  private final Map<String, ResourcePermission> groupPermissions;


  public CedarNodeMaterializedPermissions(String id) {
    this.id = id;
    userPermissions = new HashMap<>();
    groupPermissions = new HashMap<>();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  public Map<String, ResourcePermission> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermission(String id, ResourcePermission userPermission) {
    userPermissions.put(id, userPermission);
  }

  public Map<String, ResourcePermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void setGroupPermission(String id, ResourcePermission groupPermission) {
    groupPermissions.put(id, groupPermission);
  }

  public static String getKey(String userOrGroupId, ResourcePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(userOrGroupId);
    sb.append("|");
    sb.append(permission.getValue());
    return DigestUtils.md5Hex(sb.toString());
  }
}
