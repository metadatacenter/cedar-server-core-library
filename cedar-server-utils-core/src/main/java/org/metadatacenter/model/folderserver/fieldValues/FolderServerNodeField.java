package org.metadatacenter.model.folderserver.fieldValues;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerNodeField {

  protected String fieldName;

  public static List<FolderServerNodeField> fromNode(FolderServerNode node) {
    List<FolderServerNodeField> fields = new ArrayList<>();
    FolderServerNodeField field = new FolderServerNodeField();
    field.setFieldName("TestFieldName");
    fields.add(field);
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

  @JsonProperty("fieldName")
  public String getFieldName() {
    return fieldName;
  }

  @JsonProperty("fieldName")
  public void setFieldName(String fieldName) {
    this.fieldName = fieldName;
  }


}
