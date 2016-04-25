package org.metadatacenter.model.folderserver;

import org.metadatacenter.model.CedarResource;
import org.metadatacenter.model.CedarNodeType;

public abstract class CedarFSResource extends CedarFSNode implements CedarResource {
  public CedarFSResource(CedarNodeType resourceType) {
    super(resourceType);
  }
}
