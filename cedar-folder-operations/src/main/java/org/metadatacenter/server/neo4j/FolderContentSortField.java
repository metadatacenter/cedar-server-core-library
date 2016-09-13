package org.metadatacenter.server.neo4j;

public class FolderContentSortField {

  private String name;
  private String fieldName;
  private boolean textual;

  public FolderContentSortField(String name, String fieldName, boolean textual) {
    this.name = name;
    this.fieldName = fieldName;
    this.textual = textual;
  }

  public String getName() {
    return name;
  }

  public String getFieldName() {
    return fieldName;
  }

  public boolean isTextual() {
    return textual;
  }
}
