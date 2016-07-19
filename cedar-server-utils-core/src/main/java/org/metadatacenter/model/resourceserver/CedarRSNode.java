package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.AbstractCedarNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.model.provenance.ProvenanceTime;

import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "nodeType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = CedarRSFolder.class, name = CedarNodeType.Types.FOLDER),
    @JsonSubTypes.Type(value = CedarRSField.class, name = CedarNodeType.Types.FIELD),
    @JsonSubTypes.Type(value = CedarRSElement.class, name = CedarNodeType.Types.ELEMENT),
    @JsonSubTypes.Type(value = CedarRSTemplate.class, name = CedarNodeType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = CedarRSInstance.class, name = CedarNodeType.Types.INSTANCE)
})
public abstract class CedarRSNode extends AbstractCedarNode {

  protected String createdByUserName;
  protected String lastUpdatedByUserName;
  protected String ownedByUserName;
  protected String displayPath;
  protected String displayParentPath;

  protected CedarRSNode(CedarNodeType nodeType) {
    this.nodeType = nodeType;
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  public CedarNodeType getType() {
    return nodeType;
  }

  public void setType(CedarNodeType nodeType) {
    this.nodeType = nodeType;
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

  public String getOwnedByUserName() {
    return ownedByUserName;
  }

  public void setOwnedByUserName(String ownedByUserName) {
    this.ownedByUserName = ownedByUserName;
  }

  public String getDisplayPath() {
    return displayPath;
  }

  public void setDisplayPath(String displayPath) {
    this.displayPath = displayPath;
  }

  public String getDisplayParentPath() {
    return displayParentPath;
  }

  public void setDisplayParentPath(String displayParentPath) {
    this.displayParentPath = displayParentPath;
  }

  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return CONTEXT;
  }

  public static CedarRSNode forType(CedarNodeType t) {
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
