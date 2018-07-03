package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.AbstractCedarNodeWithDates;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerGroup extends AbstractCedarNodeWithDates {

  private String name;
  private String description;
  private String specialGroup;
  private String createdBy;
  private String lastUpdatedBy;

  public FolderServerGroup() {
    this.nodeType = CedarNodeType.GROUP;
  }

  @JsonProperty(NodeProperty.Label.ID)
  public String getId() {
    return id;
  }

  @JsonProperty(NodeProperty.Label.ID)
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty(NodeProperty.Label.NODE_TYPE)
  public CedarNodeType getType() {
    return nodeType;
  }

  @JsonProperty(NodeProperty.Label.NODE_TYPE)
  public void setType(CedarNodeType nodeType) {
    this.nodeType = nodeType;
  }

  @JsonProperty(NodeProperty.Label.CREATED_ON)
  public ProvenanceTime getCreatedOn() {
    return createdOn;
  }

  @JsonProperty(NodeProperty.Label.CREATED_ON)
  public void setCreatedOn(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonProperty(NodeProperty.Label.LAST_UPDATED_ON)
  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  @JsonProperty(NodeProperty.Label.LAST_UPDATED_ON)
  public void setLastUpdatedOn(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonProperty(NodeProperty.Label.LAST_UPDATED_ON_TS)
  public long getLastUpdatedOnTS() {
    return lastUpdatedOnTS;
  }

  @JsonProperty(NodeProperty.Label.LAST_UPDATED_ON_TS)
  public void setLastUpdatedOnTS(long lastUpdatedOnTS) {
    this.lastUpdatedOnTS = lastUpdatedOnTS;
  }

  @JsonProperty(NodeProperty.Label.CREATED_ON_TS)
  public long getCreatedOnTS() {
    return createdOnTS;
  }

  @JsonProperty(NodeProperty.Label.CREATED_ON_TS)
  public void setCreatedOnTS(long createdOnTS) {
    this.createdOnTS = createdOnTS;
  }

  @JsonProperty(NodeProperty.Label.NAME)
  public String getName() {
    return name;
  }

  @JsonProperty(NodeProperty.Label.NAME)
  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty(NodeProperty.Label.DESCRIPTION)
  public String getDescription() {
    return description;
  }

  @JsonProperty(NodeProperty.Label.DESCRIPTION)
  public void setDescription(String description) {
    this.description = description;
  }

  @JsonProperty(NodeProperty.Label.SPECIAL_GROUP)
  public String getSpecialGroup() {
    return specialGroup;
  }

  @JsonProperty(NodeProperty.Label.SPECIAL_GROUP)
  public void setSpecialGroup(String specialGroup) {
    this.specialGroup = specialGroup;
  }

  @JsonProperty(NodeProperty.Label.CREATED_BY)
  public String getCreatedBy() {
    return createdBy;
  }

  @JsonProperty(NodeProperty.Label.CREATED_BY)
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonProperty(NodeProperty.Label.LAST_UPDATED_BY)
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  @JsonProperty(NodeProperty.Label.LAST_UPDATED_BY)
  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public CedarGroupExtract buildExtract() {
    return new CedarGroupExtract(getId(), getName());
  }
}
