package org.metadatacenter.search;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InfoField {

  protected String fieldName;
  protected Object fieldValue;
  protected String fieldValueUri;

  /**
   * Extracts the field names and values from a template instance
   * @param templateInstance A template instance in JSON
   * @return
   */
  public static List<InfoField> fromTemplateInstance(JsonNode templateInstance) {
    List<InfoField> fields = new ArrayList<>();
    InfoField field1 = new InfoField();
    field1.setFieldName("Title");
    field1.setFieldValue("A nice biosample");

    InfoField field2 = new InfoField();
    field2.setFieldName("Numeric");
    field2.setFieldValue(17);

    InfoField field3 = new InfoField();
    field3.setFieldName("Date");
    field3.setFieldValue(new Date(12/12/2012));

    InfoField field4 = new InfoField();
    field4.setFieldName("Ontology term");
    field4.setFieldValue("Melanoma");
    field4.setFieldValueUri("http://purl.bioontology.org/ontology/MEDDRA/10053571");

    fields.add(field1);
    fields.add(field2);
    fields.add(field3);
    fields.add(field4);

    return fields;
  }

  public String getFieldName() {
    return fieldName;
  }

  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }

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
