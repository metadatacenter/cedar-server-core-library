package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.AbstractCedarNodeWithDates;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
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

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty("nodeType")
  public CedarNodeType getType() {
    return nodeType;
  }

  @JsonProperty("type")
  public void setType(CedarNodeType nodeType) {
    this.nodeType = nodeType;
  }

  public ProvenanceTime getCreatedOn() {
    return createdOn;
  }

  public void setCreatedOn(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  public void setLastUpdatedOn(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
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

  public String getSpecialGroup() {
    return specialGroup;
  }

  public void setSpecialGroup(String specialGroup) {
    this.specialGroup = specialGroup;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public CedarGroupExtract buildExtract() {
    return new CedarGroupExtract(getId(), getName());
  }
}
