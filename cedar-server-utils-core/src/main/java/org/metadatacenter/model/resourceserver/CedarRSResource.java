package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.provenance.ProvenanceTime;

import java.util.HashMap;
import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "resourceType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CedarRSFolder.class, name = "folder")
})
public abstract class CedarRSResource {

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

  protected CedarRSResource(CedarResourceType resourceType) {
    this.resourceType = resourceType;
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  public CedarResourceType getType() {
    return resourceType;
  }

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

  @JsonProperty("pav:createdOn")
  public void setCreatedOn(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonProperty("pav:lastUpdatedOn")
  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  @JsonProperty("pav:lastUpdatedOn")
  public void setLastUpdatedOn(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonProperty("pav:createdBy")
  public String getCreatedBy() {
    return createdBy;
  }

  @JsonProperty("pav:createdBy")
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonProperty("cedar:lastUpdatedBy")
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  @JsonProperty("cedar:lastUpdatedBy")
  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return CONTEXT;
  }

  public static CedarRSResource forType(CedarResourceType t) {
    switch (t) {
      case FOLDER:
        return new CedarRSFolder();
      case FIELD:
        return new CedarRSField();
      case ELEMENT:
        return new CedarRSElement();
      case TEMPLATE:
        return new CedarRSTemplate();
      case INSTANCE:
        return new CedarRSInstance();
    }
    return null;
  }
}
