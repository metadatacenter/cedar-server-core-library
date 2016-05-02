package org.metadatacenter.model;

import org.metadatacenter.provenance.ProvenanceTime;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCedarNode implements CedarNode {

  protected String id;
  protected CedarNodeType resourceType;
  protected String name;
  protected String description;
  protected ProvenanceTime createdOn;
  protected long createdOnTS;
  protected ProvenanceTime lastUpdatedOn;
  protected long lastUpdatedOnTS;
  protected String createdBy;
  protected String lastUpdatedBy;
  protected static Map<String, String> CONTEXT;
  protected String path;

  static {
    CONTEXT = new HashMap<>();
    CONTEXT.put("pav", "http://purl.org/pav/");
    CONTEXT.put("cedar", "https://schema.metadatacenter.org/core/");
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }
}
