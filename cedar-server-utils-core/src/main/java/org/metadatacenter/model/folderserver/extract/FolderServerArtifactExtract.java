package org.metadatacenter.model.folderserver.extract;

import org.metadatacenter.model.CedarResourceType;

public abstract class FolderServerArtifactExtract extends FolderServerResourceExtract {

  protected Boolean isOpen;

  public FolderServerArtifactExtract(CedarResourceType resourceType) {
    super(resourceType);
  }

  public Boolean getIsOpen() {
    return isOpen;
  }

  public void setIsOpen(Boolean isOpen) {
    this.isOpen = isOpen;
  }

}
