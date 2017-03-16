package org.metadatacenter.model;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCedarNode extends AbstractCedarSuperNode {

  protected String name;
  protected String description;
  protected String displayName;
  protected String createdBy;
  protected String lastUpdatedBy;
  protected static final Map<String, String> CONTEXT;
  protected String path;
  protected String parentPath;
  protected String displayPath;
  protected String displayParentPath;
  protected String ownedBy;

  static {
    CONTEXT = new HashMap<>();
    CONTEXT.put("pav", "http://purl.org/pav/");
    CONTEXT.put("oslc", "http://open-services.net/ns/core#");
    CONTEXT.put("schema", "http://schema.org/");
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

  public String getDisplayPath() {
    return displayPath;
  }

  public void setDisplayPath(String displayPath) {
    this.displayPath = displayPath;
  }

  public String getDisplayParentPath() {
    return displayParentPath;
  }

  public void setDisplayParentPath(String displayParentPath) {
    this.displayParentPath = displayParentPath;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getOwnedBy() {
    return ownedBy;
  }

  public void setOwnedBy(String ownedBy) {
    this.ownedBy = ownedBy;
  }

}
