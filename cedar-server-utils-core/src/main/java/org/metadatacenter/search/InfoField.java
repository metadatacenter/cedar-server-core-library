package org.metadatacenter.search;

public class InfoField {

  private String fieldName;
  private String fieldPrefLabel;
  private String fieldPath;
  private Object fieldValue;
  private String fieldValueUri;

  public InfoField(String fieldName, String fieldPrefLabel, String fieldPath, Object fieldValue, String fieldValueUri) {
    this.fieldName = fieldName;
    this.fieldPrefLabel = fieldPrefLabel;
    this.fieldPath = fieldPath;
    this.fieldValue = fieldValue;
    this.fieldValueUri = fieldValueUri;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldPrefLabel() { return fieldPrefLabel; }

  public void setFieldPrefLabel(String fieldPrefLabel) { this.fieldPrefLabel = fieldPrefLabel; }

  public String getFieldPath() { return fieldPath; }

  public void setFieldPath(String fieldPath) { this.fieldPath = fieldPath; }

  public Object getFieldValue() {
    return fieldValue;
  }

  public void setFieldValue(Object fieldValue) {
    this.fieldValue = fieldValue;
  }

  public String getFieldValueUri() {
    return fieldValueUri;
  }

  public void setFieldValueUri(String fieldValueUri) {
    this.fieldValueUri = fieldValueUri;
  }

}
