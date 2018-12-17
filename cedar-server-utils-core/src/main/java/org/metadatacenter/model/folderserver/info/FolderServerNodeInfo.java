package org.metadatacenter.model.folderserver.info;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithUsersData;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithVersionData;
import org.metadatacenter.model.folderserver.datagroup.UsersDataGroup;
import org.metadatacenter.model.folderserver.datagroup.VersionDataGroup;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerNodeInfo implements ResourceWithVersionData, ResourceWithUsersData {

  protected String id;
  protected CedarNodeType nodeType;
  protected ProvenanceTime createdOn;
  protected ProvenanceTime lastUpdatedOn;

  protected String name;
  protected String description;
  protected String identifier;
  protected String path;

  protected ResourceUri previousVersion;
  protected BiboStatus publicationStatus;
  protected ResourceUri derivedFrom;

  protected VersionDataGroup versionData;
  protected UsersDataGroup usersData;

  private ResourceUri isBasedOn;

  protected boolean isRoot;
  protected boolean isSystem;
  protected boolean isUserHome;

  protected boolean isOpen;

  private FolderServerNodeInfo() {
    this.versionData = new VersionDataGroup();
    this.usersData = new UsersDataGroup();
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

  @JsonProperty(NodeProperty.Label.IDENTIFIER)
  public String getIdentifier() {
    return identifier;
  }

  @JsonProperty(NodeProperty.Label.IDENTIFIER)
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
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

  @JsonProperty(NodeProperty.Label.IS_BASED_ON)
  public ResourceUri getIsBasedOn() {
    return isBasedOn;
  }

  @JsonProperty(NodeProperty.Label.IS_BASED_ON)
  public void setIsBasedOn(String isBasedOn) {
    this.isBasedOn = ResourceUri.forValue(isBasedOn);
  }

  @JsonProperty(NodeProperty.Label.IS_ROOT)
  public boolean getIsRoot() {
    return isRoot;
  }

  @JsonProperty(NodeProperty.Label.IS_ROOT)
  public void setIsRoot(boolean isRoot) {
    this.isRoot = isRoot;
  }

  @JsonProperty(NodeProperty.Label.IS_SYSTEM)
  public boolean getIsSystem() {
    return isSystem;
  }

  @JsonProperty(NodeProperty.Label.IS_SYSTEM)
  public void setIsSystem(boolean isSystem) {
    this.isSystem = isSystem;
  }

  @JsonProperty(NodeProperty.Label.IS_USER_HOME)
  public boolean getIsUserHome() {
    return isUserHome;
  }

  @JsonProperty(NodeProperty.Label.IS_USER_HOME)
  public void setIsUserHome(boolean isUserHome) {
    this.isUserHome = isUserHome;
  }

  @JsonProperty(NodeProperty.Label.IS_OPEN)
  public boolean getIsOpen() {
    return isOpen;
  }

  @JsonProperty(NodeProperty.Label.IS_OPEN)
  public void setIsOpen(boolean isOpen) {
    this.isOpen = isOpen;
  }

  @Override
  public ResourceVersion getVersion() {
    return versionData.getVersion();
  }

  @Override
  public void setVersion(String versionString) {
    versionData.setVersion(ResourceVersion.forValue(versionString));
  }

  @Override
  public Boolean isLatestVersion() {
    return versionData.isLatestVersion();
  }

  @Override
  public void setLatestVersion(Boolean latestVersion) {
    versionData.setLatestVersion(latestVersion);
  }

  @Override
  public Boolean isLatestDraftVersion() {
    return versionData.isLatestDraftVersion();
  }

  @Override
  public void setLatestDraftVersion(Boolean latestDraftVersion) {
    versionData.setLatestDraftVersion(latestDraftVersion);
  }

  @Override
  public Boolean isLatestPublishedVersion() {
    return versionData.isLatestPublishedVersion();
  }

  @Override
  public void setLatestPublishedVersion(Boolean latestPublishedVersion) {
    versionData.setLatestPublishedVersion(latestPublishedVersion);
  }

  @Override
  public String getOwnedBy() {
    return usersData.getOwnedBy();
  }

  @Override
  public void setOwnedBy(String ownedBy) {
    usersData.setOwnedBy(ownedBy);
  }

  @Override
  public String getCreatedBy() {
    return usersData.getCreatedBy();
  }

  @Override
  public void setCreatedBy(String createdBy) {
    usersData.setCreatedBy(createdBy);
  }

  @Override
  public String getLastUpdatedBy() {
    return usersData.getLastUpdatedBy();
  }

  @Override
  public void setLastUpdatedBy(String lastUpdatedBy) {
    usersData.setLastUpdatedBy(lastUpdatedBy);
  }

}
