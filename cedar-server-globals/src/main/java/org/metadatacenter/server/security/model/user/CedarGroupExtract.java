package org.metadatacenter.server.security.model.user;

public class CedarGroupExtract {

  private String id;
  private String name;

  public CedarGroupExtract(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}