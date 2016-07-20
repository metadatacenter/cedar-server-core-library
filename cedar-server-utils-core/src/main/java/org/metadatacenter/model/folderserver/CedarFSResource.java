package org.metadatacenter.model.folderserver;

import org.metadatacenter.model.CedarNodeType;

public abstract class CedarFSResource extends CedarFSNode {
  public CedarFSResource(CedarNodeType nodeType) {
    super(nodeType);
  }
}
