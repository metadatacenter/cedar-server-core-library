package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.provenance.ProvenanceTime;
import org.metadatacenter.provenance.ProvenanceUser;

import java.lang.String;

public abstract class CedarResource {

  protected String id;
  protected CedarResourceType type;
  protected String name;
  protected String path;
  protected String description;
  protected ProvenanceTime createdOn;
  protected ProvenanceTime lastUpdatedOn;
  protected ProvenanceUser createdBy;
  protected ProvenanceUser lastUpdatedBy;

  protected CedarResource(CedarResourceType type) {
    this.type = type;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public CedarResourceType getType() {
    return type;
  }

  public void setType(CedarResourceType type) {
    this.type = type;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
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

  @JsonProperty("pav_createdOn")
  public ProvenanceTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonProperty("pav_lastUpdatedOn")
  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  public void setLastUpdatedOn(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonProperty("pav_createdBy")
  public ProvenanceUser getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(ProvenanceUser createdBy) {
    this.createdBy = createdBy;
  }

  @JsonProperty("cedar_lastUpdatedBy")
  public ProvenanceUser getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(ProvenanceUser lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public static CedarResource forType(CedarResourceType t) {
    switch(t) {
      case FOLDER: return new CedarFolder();
      case FIELD: return new CedarField();
      case ELEMENT: return new CedarElement();
      case TEMPLATE: return new CedarTemplate();
      case INSTANCE: return new CedarInstance();
    }
    return null;
  }
}
