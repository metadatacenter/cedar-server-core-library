package org.metadatacenter.model.folderserver.fieldValues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FolderServerNodeField {

  protected String fieldName;
  protected Object fieldValue;
  protected String fieldValueUri;

  public static List<FolderServerNodeField> fromNode(FolderServerNode node) {
    List<FolderServerNodeField> fields = new ArrayList<>();
    FolderServerNodeField field1 = new FolderServerNodeField();
    field1.setFieldName("Title");
    field1.setFieldValue("A nice biosample");

    FolderServerNodeField field2 = new FolderServerNodeField();
    field2.setFieldName("Numeric");
    field2.setFieldValue(17);

    FolderServerNodeField field3 = new FolderServerNodeField();
    field3.setFieldName("Date");
    field3.setFieldValue(new Date(12/12/2012));

    FolderServerNodeField field4 = new FolderServerNodeField();
    field4.setFieldName("Ontology term");
    field4.setFieldValue("Melanoma");
    field4.setFieldValueUri("http://purl.bioontology.org/ontology/MEDDRA/10053571");

    fields.add(field1);
    fields.add(field2);
    fields.add(field3);
    fields.add(field4);

    return fields;
  }

//  @JsonProperty(NodeProperty.Label.ID)
//  public String getId() {
//    return id;
//  }
//
//  @JsonProperty(NodeProperty.Label.ID)
//  public void setId(String id) {
//    this.id = id;
//  }


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
