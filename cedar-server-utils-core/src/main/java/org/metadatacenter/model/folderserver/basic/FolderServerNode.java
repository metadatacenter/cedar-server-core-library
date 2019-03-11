package org.metadatacenter.model.folderserver.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.AbstractCedarNodeWithDates;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.datagroup.*;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithIdAndType;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;
import org.metadatacenter.util.FolderServerNodeContext;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "nodeType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FolderServerFolder.class, name = CedarNodeType.Types.FOLDER),
    @JsonSubTypes.Type(value = FolderServerField.class, name = CedarNodeType.Types.FIELD),
    @JsonSubTypes.Type(value = FolderServerElement.class, name = CedarNodeType.Types.ELEMENT),
    @JsonSubTypes.Type(value = FolderServerTemplate.class, name = CedarNodeType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = FolderServerInstance.class, name = CedarNodeType.Types.INSTANCE)
})
public abstract class FolderServerNode extends AbstractCedarNodeWithDates
    implements NodeWithIdAndType, ResourceWithUserNamesData, ResourceWithUsersData, NodeWithEverybodyPermission {

  protected String name;
  protected String description;
  protected String identifier;
  protected String path;
  protected String parentPath;
  protected NodeSharePermission everybodyPermission;

  private List<FolderServerNodeExtract> pathInfo;

  protected UsersDataGroup usersData;
  protected UserNamesDataGroup userNamesData;

  protected FolderServerNode(CedarNodeType nodeType) {
    this.nodeType = nodeType;
    this.pathInfo = new ArrayList<>();
    this.usersData = new UsersDataGroup();
    this.userNamesData = new UserNamesDataGroup();
  }

  public static FolderServerNode fromNodeExtract(FolderServerNodeExtract node) {
    try {
      return JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(node), FolderServerNode.class);
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

  @JsonIgnore
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

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getParentPath() {
    return parentPath;
  }

  public void setParentPath(String parentPath) {
    this.parentPath = parentPath;
  }

  @JsonProperty(NodeProperty.OnTheFly.CONTEXT)
  public Map<String, String> getContext() {
    return FolderServerNodeContext.getContext();
  }

  @JsonProperty(NodeProperty.OnTheFly.PATH_INFO)
  public List<FolderServerNodeExtract> getPathInfo() {
    return pathInfo;
  }

  @JsonProperty(NodeProperty.OnTheFly.PATH_INFO)
  public void setPathInfo(List<FolderServerNodeExtract> pathInfo) {
    this.pathInfo = pathInfo;
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

  @Override
  public void setOwnedByUserName(String ownedByUserName) {
    userNamesData.setOwnedByUserName(ownedByUserName);
  }

  @Override
  public String getOwnedByUserName() {
    return userNamesData.getOwnedByUserName();
  }

  @Override
  public void setCreatedByUserName(String createdByUserName) {
    userNamesData.setCreatedByUserName(createdByUserName);
  }

  @Override
  public String getCreatedByUserName() {
    return userNamesData.getCreatedByUserName();
  }

  @Override
  public void setLastUpdatedByUserName(String lastUpdatedByUserName) {
    userNamesData.setLastUpdatedByUserName(lastUpdatedByUserName);
  }

  @Override
  public NodeSharePermission getEverybodyPermission() {
    return everybodyPermission;
  }

  @Override
  public void setEverybodyPermission(NodeSharePermission everybodyPermission) {
    this.everybodyPermission = everybodyPermission;
  }

  @Override
  public String getLastUpdatedByUserName() {
    return userNamesData.getLastUpdatedByUserName();
  }

  public static FolderServerNode forType(CedarNodeType t) {
    switch (t) {
      case FOLDER:
        return new FolderServerFolder();
      case FIELD:
        return new FolderServerField();
      case ELEMENT:
        return new FolderServerElement();
      case TEMPLATE:
        return new FolderServerTemplate();
      case INSTANCE:
        return new FolderServerInstance();
    }
    return null;
  }

  public void setCreatedByTotal(String createdBy) {
    setCreatedBy(createdBy);
    setOwnedBy(createdBy);
    setLastUpdatedBy(createdBy);
  }

  public FolderServerResource asResource() {
    return (FolderServerResource) this;
  }
}
