package org.metadatacenter.model.folderserver.datagroup;

import org.metadatacenter.model.ResourceUri;

public class PreviousVersionGroup {

  protected ResourceUri previousVersion;


  public PreviousVersionGroup() {
  }

  public ResourceUri getPreviousVersion() {
    return previousVersion;
  }

  public void setPreviousVersion(ResourceUri previousVersion) {
    this.previousVersion = previousVersion;
  }

}
