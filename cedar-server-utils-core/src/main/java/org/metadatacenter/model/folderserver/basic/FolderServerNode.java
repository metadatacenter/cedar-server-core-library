package org.metadatacenter.model.folderserver.basic;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.AbstractCedarNodeFull;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithIdAndType;
import org.metadatacenter.util.FolderServerNodeContext;

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
public abstract class FolderServerNode extends AbstractCedarNodeFull implements NodeWithIdAndType {

  private List<FolderServerNodeExtract> pathInfo;

  protected String createdByUserName;
  protected String lastUpdatedByUserName;
  protected String ownedByUserName;

  protected FolderServerNode(CedarNodeType nodeType) {
    this.nodeType = nodeType;
    this.pathInfo = new ArrayList<>();
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

  @JsonProperty(NodeProperty.OnTheFly.OWNED_BY_USER_NAME)
  public void setOwnedByUserName(String ownedByUserName) {
    this.ownedByUserName = ownedByUserName;
  }

  @JsonProperty(NodeProperty.OnTheFly.OWNED_BY_USER_NAME)
  public String getOwnedByUserName() {
    return ownedByUserName;
  }

  @JsonProperty(NodeProperty.OnTheFly.CREATED_BY_USER_NAME)
  public void setCreatedByUserName(String createdByUserName) {
    this.createdByUserName = createdByUserName;
  }

  @JsonProperty(NodeProperty.OnTheFly.CREATED_BY_USER_NAME)
  public String getCreatedByUserName() {
    return createdByUserName;
  }

  @JsonProperty(NodeProperty.OnTheFly.LAST_UPDATED_BY_USER_NAME)
  public void setLastUpdatedByUserName(String lastUpdatedByUserName) {
    this.lastUpdatedByUserName = lastUpdatedByUserName;
  }

  @JsonProperty(NodeProperty.OnTheFly.LAST_UPDATED_BY_USER_NAME)
  public String getLastUpdatedByUserName() {
    return lastUpdatedByUserName;
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
    this.createdBy = createdBy;
    this.ownedBy = createdBy;
    this.lastUpdatedBy = createdBy;
  }

}
