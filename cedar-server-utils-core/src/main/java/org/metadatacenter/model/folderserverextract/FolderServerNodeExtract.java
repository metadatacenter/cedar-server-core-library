package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.metadatacenter.model.AbstractCedarNodeExtract;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
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
public abstract class FolderServerNodeExtract extends AbstractCedarNodeExtract {

  protected String createdBy;
  protected String lastUpdatedBy;
  protected String ownedBy;
  protected String createdByUserName;
  protected String lastUpdatedByUserName;
  protected String ownedByUserName;
  protected boolean activeUserCanRead = true;


  protected FolderServerNodeExtract(CedarNodeType nodeType) {
    this.nodeType = nodeType;
  }

  public static FolderServerNodeExtract fromNode(FolderServerNode node) {
    try {
      return JsonMapper.MAPPER.readValue(JsonMapper.MAPPER.writeValueAsString(node), FolderServerNodeExtract.class);
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

  public String getOwnedBy() {
    return ownedBy;
  }

  public void setOwnedBy(String ownedBy) {
    this.ownedBy = ownedBy;
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

  @JsonProperty(NodeProperty.OnTheFly.ACTIVE_USER_CAN_READ)
  public boolean isActiveUserCanRead() {
    return activeUserCanRead;
  }

  @JsonProperty(NodeProperty.OnTheFly.ACTIVE_USER_CAN_READ)
  public void setActiveUserCanRead(boolean activeUserCanRead) {
    this.activeUserCanRead = activeUserCanRead;
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
    return anon;
  }

}
