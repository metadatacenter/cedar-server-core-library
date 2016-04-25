package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.AbstractCedarNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.provenance.ProvenanceTime;

import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "resourceType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CedarFSFolder.class, name = CedarNodeType.Types.FOLDER),
    @JsonSubTypes.Type(value = CedarFSField.class, name = CedarNodeType.Types.FIELD),
    @JsonSubTypes.Type(value = CedarFSElement.class, name = CedarNodeType.Types.ELEMENT),
    @JsonSubTypes.Type(value = CedarFSTemplate.class, name = CedarNodeType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = CedarFSInstance.class, name = CedarNodeType.Types.INSTANCE)
})
public abstract class CedarFSNode extends AbstractCedarNode {

  protected CedarFSNode(CedarNodeType resourceType) {
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
  public CedarNodeType getType() {
    return resourceType;
  }

  @JsonProperty("type")
  public void setType(CedarNodeType resourceType) {
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

  public long getLastUpdatedOnTS() {
    return lastUpdatedOnTS;
  }

  public void setLastUpdatedOnTS(long lastUpdatedOnTS) {
    this.lastUpdatedOnTS = lastUpdatedOnTS;
  }

  public long getCreatedOnTS() {
    return createdOnTS;
  }

  public void setCreatedOnTS(long createdOnTS) {
    this.createdOnTS = createdOnTS;
  }

  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return CONTEXT;
  }

  public static CedarFSNode forType(CedarNodeType t) {
    switch (t) {
      case FOLDER:
        return new CedarFSFolder();
      case FIELD:
        return new CedarFSField();
      case ELEMENT:
        return new CedarFSElement();
      case TEMPLATE:
        return new CedarFSTemplate();
      case INSTANCE:
        return new CedarFSInstance();
    }
    return null;
  }
}
