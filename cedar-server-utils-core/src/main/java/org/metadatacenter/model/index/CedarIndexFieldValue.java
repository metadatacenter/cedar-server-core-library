package org.metadatacenter.model.index;

import com.fasterxml.jackson.annotation.JsonInclude;

// Ignores null values
@JsonInclude(JsonInclude.Include.NON_NULL)

/**
 * This class represents the field information that will be indexed for CEDAR fields.
 */
public class CedarIndexFieldValue {

  private String fieldName;
  // For instances based on controlled terms, it corresponds to the @type field. It is set to null otherwise
  private String fieldSemanticType;
  // Elasticsearch data types
  private String fieldValue_string;
  private String fieldValue_long;
  private String fieldValue_integer;
  private String fieldValue_short;
  private String fieldValue_double;
  private String fieldValue_float;
  private String fieldValue_date;
  private String fieldValue_boolean;

  // For instances based on controlled terms, it corresponds to the @value field. It is set to null otherwise
  private String fieldValueSemanticType;
  private boolean useForValueRecommendation;

  // Used by Jackson
  public CedarIndexFieldValue() {};

  public CedarIndexFieldValue(String fieldName, String fieldSemanticType, String fieldValue_string, String
      fieldValue_long, String fieldValue_integer, String fieldValue_short, String fieldValue_double,
                         String fieldValue_float, String fieldValue_date, String fieldValue_boolean,
                         String fieldValueSemanticType, boolean useForValueRecommendation) {
    this.fieldName = fieldName;
    this.fieldSemanticType = fieldSemanticType;
    this.fieldValue_string = fieldValue_string;
    this.fieldValue_long = fieldValue_long;
    this.fieldValue_integer = fieldValue_integer;
    this.fieldValue_short = fieldValue_short;
    this.fieldValue_double = fieldValue_double;
    this.fieldValue_float = fieldValue_float;
    this.fieldValue_date = fieldValue_date;
    this.fieldValue_boolean = fieldValue_boolean;
    this.fieldValueSemanticType = fieldValueSemanticType;
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

  public String getFieldValue_string() {
    return fieldValue_string;
  }

  public void setFieldValue_string(String fieldValue_string) {
    this.fieldValue_string = fieldValue_string;
  }

  public String getFieldValue_long() {
    return fieldValue_long;
  }

  public void setFieldValue_long(String fieldValue_long) {
    this.fieldValue_long = fieldValue_long;
  }

  public String getFieldValue_integer() {
    return fieldValue_integer;
  }

  public void setFieldValue_integer(String fieldValue_integer) {
    this.fieldValue_integer = fieldValue_integer;
  }

  public String getFieldValue_short() {
    return fieldValue_short;
  }

  public void setFieldValue_short(String fieldValue_short) {
    this.fieldValue_short = fieldValue_short;
  }

  public String getFieldValue_double() {
    return fieldValue_double;
  }

  public void setFieldValue_double(String fieldValue_double) {
    this.fieldValue_double = fieldValue_double;
  }

  public String getFieldValue_float() {
    return fieldValue_float;
  }

  public void setFieldValue_float(String fieldValue_float) {
    this.fieldValue_float = fieldValue_float;
  }

  public String getFieldValue_date() {
    return fieldValue_date;
  }

  public void setFieldValue_date(String fieldValue_date) {
    this.fieldValue_date = fieldValue_date;
  }

  public String getFieldValue_boolean() {
    return fieldValue_boolean;
  }

  public void setFieldValue_boolean(String fieldValue_boolean) {
    this.fieldValue_boolean = fieldValue_boolean;
  }

  public String getFieldValueSemanticType() {
    return fieldValueSemanticType;
  }

  public void setFieldValueSemanticType(String fieldValueSemanticType) {
    this.fieldValueSemanticType = fieldValueSemanticType;
  }

  public boolean getUseForValueRecommendation() {
    return useForValueRecommendation;
  }

  public void setUseForValueRecommendation(boolean useForValueRecommendation) {
    this.useForValueRecommendation = useForValueRecommendation;
  }

  @Override
  public String toString() {
    return "CedarIndexField{" +
        "fieldName='" + fieldName + '\'' +
        ", fieldSemanticType='" + fieldSemanticType + '\'' +
        ", fieldValue_string='" + fieldValue_string + '\'' +
        ", fieldValue_long='" + fieldValue_long + '\'' +
        ", fieldValue_integer='" + fieldValue_integer + '\'' +
        ", fieldValue_short='" + fieldValue_short + '\'' +
        ", fieldValue_double='" + fieldValue_double + '\'' +
        ", fieldValue_float='" + fieldValue_float + '\'' +
        ", fieldValue_date='" + fieldValue_date + '\'' +
        ", fieldValue_boolean='" + fieldValue_boolean + '\'' +
        ", fieldValueSemanticType='" + fieldValueSemanticType + '\'' +
        ", useForValueRecommendation=" + useForValueRecommendation +
        '}';
  }

}


