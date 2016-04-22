package org.metadatacenter.model.folderserver;

import org.metadatacenter.model.CedarResource;
import org.metadatacenter.model.CedarResourceType;

public abstract class CedarFSResource extends CedarFSNode implements CedarResource {
  public CedarFSResource(CedarResourceType resourceType) {
    super(resourceType);
  }
}
