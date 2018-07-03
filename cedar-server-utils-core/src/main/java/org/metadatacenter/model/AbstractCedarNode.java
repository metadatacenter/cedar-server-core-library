package org.metadatacenter.model;

import org.metadatacenter.server.model.provenance.ProvenanceTime;

public abstract class AbstractCedarNode implements CedarNode {

  protected String id;
  protected CedarNodeType nodeType;
  protected ProvenanceTime createdOn;
  protected ProvenanceTime lastUpdatedOn;

}