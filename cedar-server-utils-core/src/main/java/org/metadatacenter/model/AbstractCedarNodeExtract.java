package org.metadatacenter.model;

public abstract class AbstractCedarNodeExtract extends AbstractCedarSuperNode {

  protected String name;
  protected String description;
  protected String createdBy;
  protected String lastUpdatedBy;
  protected String ownedBy;

  public String getOwnedBy() {
    return ownedBy;
  }

  public void setOwnedBy(String ownedBy) {
    this.ownedBy = ownedBy;
  }

}
