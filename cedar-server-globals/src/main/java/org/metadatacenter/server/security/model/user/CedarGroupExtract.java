package org.metadatacenter.server.security.model.user;

public class CedarGroupExtract {

  private String id;
  private String displayName;

  public CedarGroupExtract(String id, String displayName) {
    this.id = id;
    this.displayName = displayName;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }
}