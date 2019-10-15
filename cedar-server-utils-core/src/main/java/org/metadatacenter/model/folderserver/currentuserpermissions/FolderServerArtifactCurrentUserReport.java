package org.metadatacenter.model.folderserver.currentuserpermissions;

import org.metadatacenter.id.CedarArtifactId;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.datagroup.DerivedFromGroup;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithDerivedFromData;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithOpenFlag;
import org.metadatacenter.server.security.model.permission.resource.ResourceWithCurrentUserPermissions;

public abstract class FolderServerArtifactCurrentUserReport extends FolderServerResourceCurrentUserReport implements ResourceWithCurrentUserPermissions, ResourceWithOpenFlag, ResourceWithDerivedFromData {

  protected DerivedFromGroup derivedFromGroup;
  protected Boolean isOpen;

  public FolderServerArtifactCurrentUserReport(CedarResourceType resourceType) {
    super(resourceType);
    derivedFromGroup = new DerivedFromGroup();
  }

  @Override
  public CedarArtifactId getDerivedFrom() {
    return derivedFromGroup.getDerivedFrom();
  }

  @Override
  public void setDerivedFrom(CedarArtifactId df) {
    derivedFromGroup.setDerivedFrom(df);
  }

  @Override
  public Boolean isOpen() {
    return isOpen;
  }

  @Override
  public void setOpen(Boolean isOpen) {
    this.isOpen = isOpen;
  }

  public CedarArtifactId getResourceId() {
    return CedarArtifactId.buildSafe(getId());
  }

}
