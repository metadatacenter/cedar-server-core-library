package org.metadatacenter.model.folderserver.basic;

import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithIsBasedOn;

public abstract class FolderServerInstanceArtifact extends FolderServerArtifact implements ResourceWithIsBasedOn {

  public FolderServerInstanceArtifact(CedarResourceType resourceType) {
    super(resourceType);
  }

  private ResourceUri isBasedOn;

  @Override
  public ResourceUri getIsBasedOn() {
    return isBasedOn;
  }

  @Override
  public void setIsBasedOn(String isBasedOn) {
    this.isBasedOn = ResourceUri.forValue(isBasedOn);
  }

}
