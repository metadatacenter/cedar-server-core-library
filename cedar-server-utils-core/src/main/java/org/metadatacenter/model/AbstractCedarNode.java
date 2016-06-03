package org.metadatacenter.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCedarNode extends AbstractCedarSuperNode {

  protected String name;
  protected String description;
  protected String createdBy;
  protected String lastUpdatedBy;
  protected static Map<String, String> CONTEXT;
  protected String path;
  protected String parentPath;
  protected String parentFolderId;

  static {
    CONTEXT = new HashMap<>();
    CONTEXT.put("pav", "http://purl.org/pav/");
    CONTEXT.put("cedar", "https://schema.metadatacenter.org/core/");
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getParentPath() {
    return parentPath;
  }

  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }

  public String getParentFolderId() {
    return parentFolderId;
  }

  public void setParentFolderId(String parentFolderId) {
    this.parentFolderId = parentFolderId;
  }
}
