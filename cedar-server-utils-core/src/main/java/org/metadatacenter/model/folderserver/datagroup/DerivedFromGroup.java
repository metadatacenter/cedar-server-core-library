package org.metadatacenter.model.folderserver.datagroup;

import org.metadatacenter.model.ResourceUri;

public class DerivedFromGroup {

  protected ResourceUri derivedFrom;

  public DerivedFromGroup() {
  }

  public ResourceUri getDerivedFrom() {
    return derivedFrom;
  }

  public void setDerivedFrom(ResourceUri derivedFrom) {
    this.derivedFrom = derivedFrom;
  }
}
