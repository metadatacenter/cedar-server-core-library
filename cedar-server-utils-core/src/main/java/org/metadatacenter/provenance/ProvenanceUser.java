package org.metadatacenter.provenance;

import java.lang.String;

public class ProvenanceUser {
  private String id;
  private String name;

  public ProvenanceUser() {
  }

  public ProvenanceUser(String id) {
    this.id = id;
  }

  public ProvenanceUser(String id, String name) {
    this.id = id;
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
