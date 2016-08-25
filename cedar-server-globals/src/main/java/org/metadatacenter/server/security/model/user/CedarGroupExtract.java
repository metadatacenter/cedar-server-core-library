package org.metadatacenter.server.security.model.user;

public class CedarGroupExtract {

  private String groupId;
  private String displayName;

  public CedarGroupExtract(String groupId, String displayName) {
    this.groupId = groupId;
    this.displayName = displayName;
  }

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}