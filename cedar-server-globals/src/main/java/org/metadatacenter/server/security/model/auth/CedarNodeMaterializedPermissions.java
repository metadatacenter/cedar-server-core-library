package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.codec.digest.DigestUtils;
import org.metadatacenter.id.CedarFilesystemResourceId;
import org.metadatacenter.server.security.model.permission.resource.FilesystemResourcePermission;

import java.util.HashMap;
import java.util.Map;

public class CedarNodeMaterializedPermissions {

  private final String id;
  private final Map<String, FilesystemResourcePermission> userPermissions;
  private final Map<String, FilesystemResourcePermission> groupPermissions;


  public CedarNodeMaterializedPermissions(CedarFilesystemResourceId resourceId) {
    this.id = resourceId.getId();
    userPermissions = new HashMap<>();
    groupPermissions = new HashMap<>();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  public Map<String, FilesystemResourcePermission> getUserPermissions() {
    return userPermissions;
  }

  public void setUserPermission(String id, FilesystemResourcePermission userPermission) {
    userPermissions.put(id, userPermission);
  }

  public Map<String, FilesystemResourcePermission> getGroupPermissions() {
    return groupPermissions;
  }

  public void setGroupPermission(String id, FilesystemResourcePermission groupPermission) {
    groupPermissions.put(id, groupPermission);
  }

  public static String getKey(String userOrGroupId, FilesystemResourcePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(userOrGroupId);
    sb.append("|");
    sb.append(permission.getValue());
    return DigestUtils.md5Hex(sb.toString());
  }
}
