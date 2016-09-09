package org.metadatacenter.server.neo4j;

public class FolderContentSortField {

  private String name;
  private boolean textual;

  public FolderContentSortField(String name, boolean textual) {
    this.name = name;
    this.textual = textual;
  }

  public String getName() {
    return name;
  }

  public boolean isTextual() {
    return textual;
  }
}
