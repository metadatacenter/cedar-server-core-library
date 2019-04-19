package org.metadatacenter.search;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.folderserver.fieldValues.FolderServerNodeField;
import org.metadatacenter.model.folderserver.info.FolderServerNodeInfo;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class IndexedDocumentDocument {

  protected String cid;

  protected String summaryText;

  protected FolderServerNodeInfo info;

  protected List<FolderServerNodeField> fields;

  protected List<String> users;

  public IndexedDocumentDocument() {
  }

  public IndexedDocumentDocument(String cid) {
    this.cid = cid;
  }

  public String getCid() {
    return cid;
  }

  public String getSummaryText() {
    return summaryText;
  }

  public void setSummaryText(String summaryText) {
    this.summaryText = summaryText;
  }

  public FolderServerNodeInfo getInfo() {
    return info;
  }

  @JsonIgnore
  public FolderServerNodeExtract getInfoExtract() {
    return FolderServerNodeExtract.fromNodeInfo(info);
  }

  public void setInfo(FolderServerNodeInfo info) {
    this.info = info;
  }

  public List<FolderServerNodeField> getFields() { return fields; }

  public void setFields(List<FolderServerNodeField> fields) { this.fields = fields; }

  public List<String> getUsers() {
    return users;
  }
}
