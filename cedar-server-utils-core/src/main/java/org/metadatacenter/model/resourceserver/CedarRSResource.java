package org.metadatacenter.model.resourceserver;

import org.metadatacenter.model.CedarResource;
import org.metadatacenter.model.CedarNodeType;

public abstract class CedarRSResource extends CedarRSNode implements CedarResource {
  public CedarRSResource(CedarNodeType resourceType) {
    super(resourceType);
  }
}
