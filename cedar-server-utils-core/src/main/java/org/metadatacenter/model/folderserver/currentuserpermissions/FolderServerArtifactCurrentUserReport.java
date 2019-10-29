package org.metadatacenter.model.folderserver.currentuserpermissions;

import org.metadatacenter.id.CedarArtifactId;
import org.metadatacenter.id.CedarUntypedArtifactId;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.datagroup.DerivedFromGroup;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithDerivedFromData;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithOpenFlag;
import org.metadatacenter.server.security.model.auth.FilesystemResourceWithCurrentUserPermissions;

public abstract class FolderServerArtifactCurrentUserReport extends FolderServerResourceCurrentUserReport implements FilesystemResourceWithCurrentUserPermissions, ResourceWithOpenFlag, ResourceWithDerivedFromData {

  protected DerivedFromGroup derivedFromGroup;
  protected Boolean isOpen;

  public FolderServerArtifactCurrentUserReport(CedarResourceType resourceType) {
    super(resourceType);
    derivedFromGroup = new DerivedFromGroup();
  }

  @Override
  public CedarUntypedArtifactId getDerivedFrom() {
    return derivedFromGroup.getDerivedFrom();
  }

  @Override
  public void setDerivedFrom(CedarUntypedArtifactId df) {
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
    return CedarArtifactId.build(getId(), getType());
  }

}
