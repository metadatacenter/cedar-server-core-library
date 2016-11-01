package org.metadatacenter.model.index;

import com.fasterxml.jackson.annotation.JsonInclude;

// Ignores null values
@JsonInclude(JsonInclude.Include.NON_NULL)

/**
 * This class represents the field information that will be indexed for CEDAR schemas (i.e., templates and elements).
 */
public class CedarIndexFieldSchema {

  private String fieldName;
  // For instances based on controlled terms, it corresponds to the @type field. It is set to null otherwise
  private String fieldSemanticType;
  // Field value type, following to the Elasticsearch data types
  private String fieldValueType;
  private boolean useForValueRecommendation;

  // Used by Jackson
  public CedarIndexFieldSchema() {};

  public CedarIndexFieldSchema(String fieldName, String fieldSemanticType, String fieldValueType, boolean
      useForValueRecommendation) {
    this.fieldName = fieldName;
    this.fieldSemanticType = fieldSemanticType;
    this.fieldValueType = fieldValueType;
    this.useForValueRecommendation = useForValueRecommendation;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

  public String getFieldSemanticType() {
    return fieldSemanticType;
  }

  public void setFieldSemanticType(String fieldSemanticType) {
    this.fieldSemanticType = fieldSemanticType;
  }

  public String getFieldValueType() {
    return fieldValueType;
  }

  public void setFieldValueType(String fieldValueType) {
    this.fieldValueType = fieldValueType;
  }

  public boolean getUseForValueRecommendation() {
    return useForValueRecommendation;
  }

  public void setUseForValueRecommendation(boolean useForValueRecommendation) {
    this.useForValueRecommendation = useForValueRecommendation;
  }

  @Override
  public String toString() {
    return "CedarIndexFieldSchema{" +
        "fieldName='" + fieldName + '\'' +
        ", fieldSemanticType='" + fieldSemanticType + '\'' +
        ", fieldValueType='" + fieldValueType + '\'' +
        ", useForValueRecommendation=" + useForValueRecommendation +
        '}';
  }

  // This method has not been automatically generated - do not remove it
  public CedarIndexFieldValue toFieldValue() {
    CedarIndexFieldValue fv = new CedarIndexFieldValue();
    fv.setFieldName(getFieldName());
    fv.setFieldSemanticType(getFieldSemanticType());
    fv.setUseForValueRecommendation(getUseForValueRecommendation());
    return fv;
  }

}


