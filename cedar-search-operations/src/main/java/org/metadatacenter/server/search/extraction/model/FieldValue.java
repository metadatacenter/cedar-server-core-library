package org.metadatacenter.server.search.extraction.model;

import org.metadatacenter.model.CedarNodeType;

import java.util.List;
import java.util.Optional;

/**
 * Stores path and some basic characteristics of template elements and fields
 */
public class FieldValue {

  private String fieldKey; // field json key
  private String fieldValue; // field value (@value). For ontology terms, it stores the value from rdfs:label
  private String fieldValueUri; // ontology term URI (@id)
  private List<String> fieldPath; // List of json keys from the root (it includes the key of the current node)

  public FieldValue() {}

  public FieldValue(String fieldKey, String fieldValue, String fieldValueUri, List<String> fieldPath) {
    this.fieldKey = fieldKey;
    this.fieldValue = fieldValue;
    this.fieldValueUri = fieldValueUri;
    this.fieldPath = fieldPath;
  }

  public String getFieldKey() {
    return fieldKey;
  }

  public void setFieldKey(String fieldKey) {
    this.fieldKey = fieldKey;
  }

  public String getFieldValue() {
    return fieldValue;
  }

  public void setFieldValue(String fieldValue) {
    this.fieldValue = fieldValue;
  }

  public String getFieldValueUri() {
    return fieldValueUri;
  }

  public void setFieldValueUri(String fieldValueUri) {
    this.fieldValueUri = fieldValueUri;
  }

  public List<String> getFieldPath() {
    return fieldPath;
  }

  public void setFieldPath(List<String> fieldPath) {
    this.fieldPath = fieldPath;
  }

  public String generatePathDotNotation() {
    return String.join(".", fieldPath);
  }

}
