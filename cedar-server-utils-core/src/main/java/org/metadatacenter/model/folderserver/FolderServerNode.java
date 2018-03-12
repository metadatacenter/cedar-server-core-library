package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.model.AbstractCedarNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.security.model.auth.NodePermission;

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
public abstract class FolderServerNode extends AbstractCedarNode {

  private List<NodePermission> currentUserPermissions;

  protected String createdByUserName;
  protected String lastUpdatedByUserName;
  protected String ownedByUserName;
  protected String displayPath;
  protected String displayParentPath;

  protected FolderServerNode(CedarNodeType nodeType) {
    this.nodeType = nodeType;
    this.currentUserPermissions = new ArrayList<>();
  }

  @JsonGetter("@id")
  public String getId() {
    return id;
  }

  @JsonSetter("id")
  public void setId1(String id) {
    this.id = id;
  }

  @JsonSetter("@id")
  public void setId2(String id) {
    this.id = id;
  }

  @JsonIgnore
  public CedarNodeType getType() {
    return nodeType;
  }

  @JsonSetter("type")
  public void setType(CedarNodeType nodeType) {
    this.nodeType = nodeType;
  }

  @JsonGetter("schema:name")
  public String getName() {
    return name;
  }

  @JsonSetter("name")
  public void setName1(String name) {
    this.name = name;
  }

  @JsonSetter("schema:name")
  public void setName2(String name) {
    this.name = name;
  }

  @JsonGetter("schema:description")
  public String getDescription() {
    return description;
  }

  @JsonSetter("description")
  public void setDescription1(String description) {
    this.description = description;
  }

  @JsonSetter("schema:description")
  public void setDescription2(String description) {
    this.description = description;
  }

  @JsonGetter("pav:createdOn")
  public ProvenanceTime getCreatedOn() {
    return createdOn;
  }

  @JsonSetter("createdOn")
  public void setCreatedOn1(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonSetter("pav:createdOn")
  public void setCreatedOn2(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonGetter("pav:lastUpdatedOn")
  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  @JsonSetter("lastUpdatedOn")
  public void setLastUpdatedOn1(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonSetter("pav:lastUpdatedOn")
  public void setLastUpdatedOn2(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonGetter("pav:createdBy")
  public String getCreatedBy() {
    return createdBy;
  }

  @JsonSetter("createdBy")
  public void setCreatedBy1(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonSetter("pav:createdBy")
  public void setCreatedBy2(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonGetter("oslc:modifiedBy")
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  @JsonSetter("lastUpdatedBy")
  public void setLastUpdatedBy1(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  @JsonSetter("oslc:modifiedBy")
  public void setLastUpdatedBy2(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
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

  public void setCreatedByUserName(String createdByUserName) {
    this.createdByUserName = createdByUserName;
  }

  public void setLastUpdatedByUserName(String lastUpdatedByUserName) {
    this.lastUpdatedByUserName = lastUpdatedByUserName;
  }

  public String getCreatedByUserName() {
    return createdByUserName;
  }

  public String getLastUpdatedByUserName() {
    return lastUpdatedByUserName;
  }

  public String getOwnedByUserName() {
    return ownedByUserName;
  }

  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return CONTEXT;
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

  public List<NodePermission> getCurrentUserPermissions() {
    return currentUserPermissions;
  }

  public void setCurrentUserPermissions(List<NodePermission> currentUserPermissions) {
    this.currentUserPermissions = currentUserPermissions;
  }

  public void addCurrentUserPermission(NodePermission permission) {
    if (!currentUserPermissions.contains(permission)) {
      currentUserPermissions.add(permission);
    }
  }

  public boolean currentUserCan(NodePermission permission) {
    return currentUserPermissions.contains(permission);
  }
}
