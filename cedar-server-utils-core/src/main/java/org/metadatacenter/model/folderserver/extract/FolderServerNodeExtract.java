package org.metadatacenter.model.folderserver.extract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.AbstractCedarNodeExtract;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.datagroup.*;
import org.metadatacenter.model.folderserver.info.FolderServerNodeInfo;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithIdAndType;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "nodeType")
@JsonSubTypes({
    @JsonSubTypes.Type(value = FolderServerFolderExtract.class, name = CedarNodeType.Types.FOLDER),
    @JsonSubTypes.Type(value = FolderServerFieldExtract.class, name = CedarNodeType.Types.FIELD),
    @JsonSubTypes.Type(value = FolderServerElementExtract.class, name = CedarNodeType.Types.ELEMENT),
    @JsonSubTypes.Type(value = FolderServerTemplateExtract.class, name = CedarNodeType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = FolderServerInstanceExtract.class, name = CedarNodeType.Types.INSTANCE)
})
public abstract class FolderServerNodeExtract extends AbstractCedarNodeExtract
    implements NodeWithIdAndType, ResourceWithUserNamesData, ResourceWithUsersData, NodeWithEverybodyPermission {

  protected UsersDataGroup usersData;
  protected UserNamesDataGroup userNamesData;
  protected boolean activeUserCanRead = true;

  protected NodeSharePermission everybodyPermission;

  protected FolderServerNodeExtract(CedarNodeType nodeType) {
    this.nodeType = nodeType;
    this.usersData = new UsersDataGroup();
    this.userNamesData = new UserNamesDataGroup();
  }

  public static FolderServerNodeExtract fromNode(FolderServerNode node) {
    try {
      return JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(node), FolderServerNodeExtract.class);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static FolderServerNodeExtract fromNodeInfo(FolderServerNodeInfo info) {
    try {
      return JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(info), FolderServerNodeExtract.class);
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
  public String getLastUpdatedByUserName() {
    return userNamesData.getLastUpdatedByUserName();
  }

  @JsonProperty(NodeProperty.OnTheFly.ACTIVE_USER_CAN_READ)
  public boolean isActiveUserCanRead() {
    return activeUserCanRead;
  }

  @JsonProperty(NodeProperty.OnTheFly.ACTIVE_USER_CAN_READ)
  public void setActiveUserCanRead(boolean activeUserCanRead) {
    this.activeUserCanRead = activeUserCanRead;
  }


  @Override
  public NodeSharePermission getEverybodyPermission() {
    return everybodyPermission;
  }

  @Override
  public void setEverybodyPermission(NodeSharePermission everybodyPermission) {
    this.everybodyPermission = everybodyPermission;
  }

  public static FolderServerNodeExtract forType(CedarNodeType t) {
    switch (t) {
      case FOLDER:
        return new FolderServerFolderExtract();
      case FIELD:
        return new FolderServerFieldExtract();
      case ELEMENT:
        return new FolderServerElementExtract();
      case TEMPLATE:
        return new FolderServerTemplateExtract();
      case INSTANCE:
        return new FolderServerInstanceExtract();
    }
    return null;
  }

  public static FolderServerTemplateExtract anonymous(FolderServerTemplateExtract resource) {
    FolderServerTemplateExtract anon = new FolderServerTemplateExtract();
    anon.setId(resource.getId());
    anon.setActiveUserCanRead(false);
    return anon;
  }

  public static FolderServerResourceExtract anonymous(FolderServerResourceExtract resource) {
    FolderServerResourceExtract anon =
        (FolderServerResourceExtract) FolderServerNodeExtract.forType(resource.getType());
    anon.setId(resource.getId());
    anon.setActiveUserCanRead(false);
    return anon;
  }

}
