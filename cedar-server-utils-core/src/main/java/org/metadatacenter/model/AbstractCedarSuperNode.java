package org.metadatacenter.model;

import org.metadatacenter.server.model.provenance.ProvenanceTime;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCedarSuperNode implements CedarNode {

  protected String id;
  protected CedarNodeType nodeType;
  protected ProvenanceTime createdOn;
  protected long createdOnTS;
  protected ProvenanceTime lastUpdatedOn;
  protected long lastUpdatedOnTS;
  protected static final Map<String, String> CONTEXT;

  static {
    CONTEXT = new HashMap<>();
    CONTEXT.put("pav", "http://purl.org/pav/");
    CONTEXT.put("oslc", "http://open-services.net/ns/core#");
    CONTEXT.put("bibo", "http://purl.org/ontology/bibo/");
    CONTEXT.put("schema", "http://schema.org/");
  }

}