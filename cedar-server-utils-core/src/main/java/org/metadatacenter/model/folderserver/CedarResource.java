package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.provenance.ProvenanceTime;

import java.util.HashMap;
import java.util.Map;

public abstract class CedarResource {

  protected String id;
  protected CedarResourceType resourceType;
  protected String name;
  protected String description;
  protected ProvenanceTime createdOn;
  protected ProvenanceTime lastUpdatedOn;
  protected String createdBy;
  protected String lastUpdatedBy;
  protected static Map<String, String> CONTEXT;

  static {
    CONTEXT = new HashMap<>();
    CONTEXT.put("pav", "http://purl.org/pav/");
    CONTEXT.put("cedar", "https://schema.metadatacenter.org/core/");
  }

  protected CedarResource(CedarResourceType resourceType) {
    this.resourceType = resourceType;
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("resourceType")
  public CedarResourceType getType() {
    return resourceType;
  }

  @JsonProperty("type")
  public void setType(CedarResourceType resourceType) {
    this.resourceType = resourceType;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty("pav:createdOn")
  public ProvenanceTime getCreatedOn() {
    return createdOn;
  }

  @JsonProperty("createdOn")
  public void setCreatedOn(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonProperty("pav:lastUpdatedOn")
  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  @JsonProperty("lastUpdatedOn")
  public void setLastUpdatedOn(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonProperty("pav:createdBy")
  public String getCreatedBy() {
    return createdBy;
  }

  @JsonProperty("createdBy")
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonProperty("cedar:lastUpdatedBy")
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  @JsonProperty("lastUpdatedBy")
  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return CONTEXT;
  }

  public static CedarResource forType(CedarResourceType t) {
    switch (t) {
      case FOLDER:
        return new CedarFolder();
      case FIELD:
        return new CedarField();
      case ELEMENT:
        return new CedarElement();
      case TEMPLATE:
        return new CedarTemplate();
      case INSTANCE:
        return new CedarInstance();
    }
    return null;
  }
}
