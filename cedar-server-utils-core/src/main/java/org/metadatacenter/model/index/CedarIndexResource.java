package org.metadatacenter.model.index;

import org.metadatacenter.model.resourceserver.ResourceServerNode;

import java.util.List;

public class CedarIndexResource {

  private ResourceServerNode info;
  private List<String> fieldName;
  private List<String> fieldValue;
  // Only for template instances
  private String templateId;

  // Used by Jackson
  public CedarIndexResource() {}

  public CedarIndexResource(ResourceServerNode info, List<String> fieldName, List<String> fieldValue, String templateId) {
    this.info = info;
    this.fieldName = fieldName;
    this.fieldValue = fieldValue;
    this.templateId = templateId;
  }

  public ResourceServerNode getInfo() {
    return info;
  }

  public void setInfo(ResourceServerNode info) {
    this.info = info;
  }

  public List<String> getFieldName() {
    return fieldName;
  }

  public void setFieldName(List<String> fieldName) {
    this.fieldName = fieldName;
  }

  public List<String> getFieldValue() {
    return fieldValue;
  }

  public void setFieldValue(List<String> fieldValue) {
    this.fieldValue = fieldValue;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }

}

