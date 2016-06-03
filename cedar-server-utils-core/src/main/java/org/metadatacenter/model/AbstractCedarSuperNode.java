package org.metadatacenter.model;

import org.metadatacenter.provenance.ProvenanceTime;

public abstract class AbstractCedarSuperNode implements CedarNode {

  protected String id;
  protected CedarNodeType nodeType;
  protected ProvenanceTime createdOn;
  protected long createdOnTS;
  protected ProvenanceTime lastUpdatedOn;
  protected long lastUpdatedOnTS;

}