package org.metadatacenter.model;

public abstract class AbstractCedarNodeFull extends AbstractCedarNodeWithDates {

  protected String name;
  protected String description;
  protected String identifier;
  protected String createdBy;
  protected String lastUpdatedBy;
  protected String path;
  protected String parentPath;
  protected String ownedBy;

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

  public String getOwnedBy() {
    return ownedBy;
  }

  public void setOwnedBy(String ownedBy) {
    this.ownedBy = ownedBy;
  }

}
