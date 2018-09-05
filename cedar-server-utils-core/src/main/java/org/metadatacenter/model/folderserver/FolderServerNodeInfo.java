package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerNodeInfo {

  protected String id;
  protected CedarNodeType nodeType;
  protected ProvenanceTime createdOn;
  protected ProvenanceTime lastUpdatedOn;

  protected String name;
  protected String description;
  protected String createdBy;
  protected String lastUpdatedBy;
  protected String path;
  protected String ownedBy;

  protected ResourceVersion version;
  protected ResourceUri previousVersion;
  protected BiboStatus publicationStatus;
  protected ResourceUri derivedFrom;
  protected Boolean latestVersion;

  private ResourceUri isBasedOn;

  private FolderServerNodeInfo() {
  }

  protected FolderServerNodeInfo(CedarNodeType nodeType) {
    this.nodeType = nodeType;
  }

  public static FolderServerNodeInfo fromNode(FolderServerNode node) {
    try {
      FolderServerNodeInfo info =
          JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(node), FolderServerNodeInfo.class);
      info.setType(node.getType());
      return info;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
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

  @JsonProperty(NodeProperty.Label.VERSION)
  public ResourceVersion getVersion() {
    return version;
  }

  @JsonProperty(NodeProperty.Label.VERSION)
  public void setVersion(String v) {
    this.version = ResourceVersion.forValue(v);
  }

  @JsonProperty(NodeProperty.Label.PREVIOUS_VERSION)
  public ResourceUri getPreviousVersion() {
    return previousVersion;
  }

  @JsonProperty(NodeProperty.Label.PREVIOUS_VERSION)
  public void setPreviousVersion(String pv) {
    this.previousVersion = ResourceUri.forValue(pv);
  }

  @JsonProperty(NodeProperty.Label.PUBLICATION_STATUS)
  public BiboStatus getPublicationStatus() {
    return publicationStatus;
  }

  @JsonProperty(NodeProperty.Label.PUBLICATION_STATUS)
  public void setPublicationStatus(String s) {
    this.publicationStatus = BiboStatus.forValue(s);
  }

  @JsonProperty(NodeProperty.Label.DERIVED_FROM)
  public ResourceUri getDerivedFrom() {
    return derivedFrom;
  }

  @JsonProperty(NodeProperty.Label.DERIVED_FROM)
  public void setDerivedFrom(String df) {
    this.derivedFrom = ResourceUri.forValue(df);
  }

  @JsonProperty(NodeProperty.Label.IS_LATEST_VERSION)
  public Boolean isLatestVersion() {
    return latestVersion;
  }

  @JsonProperty(NodeProperty.Label.IS_LATEST_VERSION)
  public void setLatestVersion(Boolean latestVersion) {
    this.latestVersion = latestVersion;
  }

  @JsonProperty(NodeProperty.Label.IS_BASED_ON)
  public ResourceUri getIsBasedOn() {
    return isBasedOn;
  }

  @JsonProperty(NodeProperty.Label.IS_BASED_ON)
  public void setIsBasedOn(String isBasedOn) {
    this.isBasedOn = ResourceUri.forValue(isBasedOn);
  }

  public String getOwnedBy() {
    return ownedBy;
  }

  public void setOwnedBy(String ownedBy) {
    this.ownedBy = ownedBy;
  }

}
