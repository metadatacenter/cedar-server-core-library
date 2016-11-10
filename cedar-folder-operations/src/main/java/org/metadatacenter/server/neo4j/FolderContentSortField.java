package org.metadatacenter.server.neo4j;

public class FolderContentSortField {

  private final String name;
  private final String fieldName;
  private final boolean textual;

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
