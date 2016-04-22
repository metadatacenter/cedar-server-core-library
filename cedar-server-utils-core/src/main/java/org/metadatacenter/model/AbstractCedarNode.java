package org.metadatacenter.model;

import org.metadatacenter.provenance.ProvenanceTime;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCedarNode implements CedarNode {

  protected String id;
  protected CedarResourceType resourceType;
  protected String name;
  protected String description;
  protected ProvenanceTime createdOn;
  protected long createdOnTS;
  protected ProvenanceTime lastUpdatedOn;
  protected long lastUpdatedOnTS;
  protected String createdBy;
  protected String lastUpdatedBy;
  protected static Map<String, String> CONTEXT;

  static {
    CONTEXT = new HashMap<>();
    CONTEXT.put("pav", "http://purl.org/pav/");
    CONTEXT.put("cedar", "https://schema.metadatacenter.org/core/");
  }

}
