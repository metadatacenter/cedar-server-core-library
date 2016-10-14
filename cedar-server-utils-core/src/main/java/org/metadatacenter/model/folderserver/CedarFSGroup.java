package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.AbstractCedarSuperNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSGroup extends AbstractCedarSuperNode {

  private String name;
  private String displayName;
  private String description;
  private String specialGroup;

  public CedarFSGroup() {
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

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
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

  public CedarGroupExtract buildExtract() {
    CedarGroupExtract r = new CedarGroupExtract(getId(), getDisplayName());
    return r;
  }
}
