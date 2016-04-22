package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.AbstractCedarNode;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.provenance.ProvenanceTime;

import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "resourceType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CedarRSFolder.class, name = CedarResourceType.Types.FOLDER),
    @JsonSubTypes.Type(value = CedarRSField.class, name = CedarResourceType.Types.FIELD),
    @JsonSubTypes.Type(value = CedarRSElement.class, name = CedarResourceType.Types.ELEMENT),
    @JsonSubTypes.Type(value = CedarRSTemplate.class, name = CedarResourceType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = CedarRSInstance.class, name = CedarResourceType.Types.INSTANCE)

})
public abstract class CedarRSNode extends AbstractCedarNode {

  protected String createdByUserName;
  protected String lastUpdatedByUserName;

  protected CedarRSNode(CedarResourceType resourceType) {
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

  public String getCreatedByUserName() {
    return createdByUserName;
  }

  public void setCreatedByUserName(String createdByUserName) {
    this.createdByUserName = createdByUserName;
  }

  public String getLastUpdatedByUserName() {
    return lastUpdatedByUserName;
  }

  public void setLastUpdatedByUserName(String lastUpdatedByUserName) {
    this.lastUpdatedByUserName = lastUpdatedByUserName;
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

  public static CedarRSNode forType(CedarResourceType t) {
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
