package org.metadatacenter.model.folderserver.datagroup;

import org.metadatacenter.id.CedarArtifactId;

public class DerivedFromGroup {

  protected CedarArtifactId derivedFrom;

  public DerivedFromGroup() {
  }

  public CedarArtifactId getDerivedFrom() {
    return derivedFrom;
  }

  public void setDerivedFrom(CedarArtifactId derivedFrom) {
    this.derivedFrom = derivedFrom;
  }
}
