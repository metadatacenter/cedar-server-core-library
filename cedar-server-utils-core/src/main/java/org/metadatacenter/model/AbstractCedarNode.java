package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
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
  protected List<PathComponent> pathComponents;
  protected String ownedBy;
  protected boolean publicReadable;
  protected boolean publicWritable;

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

  @JsonProperty("ownedBy")
  public String getOwnedBy() {
    return ownedBy;
  }

  @JsonProperty("ownedBy")
  public void setOwnedBy(String ownedBy) {
    this.ownedBy = ownedBy;
  }

  @JsonProperty("isPublicReadable")
  public boolean isPublicReadable() {
    return publicReadable;
  }

  @JsonProperty("isPublicReadable")
  public void setPublicReadable(boolean publicReadable) {
    this.publicReadable = publicReadable;
  }

  @JsonProperty("isPublicWritable")
  public boolean isPublicWritable() {
    return publicWritable;
  }

  @JsonProperty("isPublicWritable")
  public void setPublicWritable(boolean publicWritable) {
    this.publicWritable = publicWritable;
  }

  @JsonProperty("pathComponents")
  public List<PathComponent> getPathComponents() {
    return pathComponents;
  }

  @JsonProperty("pathComponents")
  public void setPathComponents(List<PathComponent> pathComponents) {
    this.pathComponents = pathComponents;
  }
}
