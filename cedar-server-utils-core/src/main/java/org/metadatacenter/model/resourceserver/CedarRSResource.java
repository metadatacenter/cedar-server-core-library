package org.metadatacenter.model.resourceserver;

import org.metadatacenter.model.CedarResource;
import org.metadatacenter.model.CedarResourceType;

public abstract class CedarRSResource extends CedarRSNode implements CedarResource {
  public CedarRSResource(CedarResourceType resourceType) {
    super(resourceType);
  }
}
