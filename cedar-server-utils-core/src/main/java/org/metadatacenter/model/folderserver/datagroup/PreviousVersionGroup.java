package org.metadatacenter.model.folderserver.datagroup;

import org.metadatacenter.id.CedarSchemaArtifactId;

public class PreviousVersionGroup {

  protected CedarSchemaArtifactId previousVersion;

  public PreviousVersionGroup() {
  }

  public CedarSchemaArtifactId getPreviousVersion() {
    return previousVersion;
  }

  public void setPreviousVersion(CedarSchemaArtifactId previousVersion) {
    this.previousVersion = previousVersion;
  }

}
