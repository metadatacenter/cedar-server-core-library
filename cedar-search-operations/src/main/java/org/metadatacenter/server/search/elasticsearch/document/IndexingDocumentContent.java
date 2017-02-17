package org.metadatacenter.server.search.elasticsearch.document;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.folderserver.FolderServerNode;

public class IndexingDocumentContent {

  private String cid;

  private FolderServerNode info;
  // Contains a summary of the resource content. There is no need to index the full JSON for each resource. Only the
  // information necessary to satisfy search and value recommendation use cases is kept.
  private JsonNode resourceSummarizedContent;
  // Only for template instances
  private String templateId;

  // Used by Jackson
  public IndexingDocumentContent() {
  }

  public IndexingDocumentContent(FolderServerNode info, JsonNode resourceSummarizedContent, String templateId) {
    this.cid = info.getId();
    this.info = info;
    this.resourceSummarizedContent = resourceSummarizedContent;
    this.templateId = templateId;
  }

  public void setCid(String cid) {
    this.cid = cid;
  }

  public String getCid() {
    return cid;
  }

  public FolderServerNode getInfo() {
    return info;
  }

  public void setInfo(FolderServerNode info) {
    this.info = info;
  }

  public JsonNode getResourceSummarizedContent() {
    return resourceSummarizedContent;
  }

  public void setResourceSummarizedContent(JsonNode resourceSummarizedContent) {
    this.resourceSummarizedContent = resourceSummarizedContent;
  }

  public String getTemplateId() {
    return templateId;
  }

  public void setTemplateId(String templateId) {
    this.templateId = templateId;
  }
}

