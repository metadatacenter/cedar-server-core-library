package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.model.AbstractCedarNodeFull;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserverextract.FolderServerFolderExtract;
import org.metadatacenter.model.folderserverextract.FolderServerNodeExtract;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.security.model.auth.NodePermission;
import org.metadatacenter.util.FolderServerNodeContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.metadatacenter.model.ModelNodeNames.SCHEMA_DESCRIPTION;
import static org.metadatacenter.model.ModelNodeNames.SCHEMA_NAME;
import static org.metadatacenter.util.provenance.ProvenanceUtil.*;

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
public abstract class FolderServerNode extends AbstractCedarNodeFull {

  private List<NodePermission> currentUserPermissions;
  private List<FolderServerNodeExtract> pathInfo;

  protected String createdByUserName;
  protected String lastUpdatedByUserName;
  protected String ownedByUserName;

  protected FolderServerNode(CedarNodeType nodeType) {
    this.nodeType = nodeType;
    this.currentUserPermissions = new ArrayList<>();
    this.pathInfo = new ArrayList<>();
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

  @JsonGetter(SCHEMA_NAME)
  public String getName() {
    return name;
  }

  @JsonSetter("name")
  public void setName1(String name) {
    this.name = name;
  }

  @JsonSetter(SCHEMA_NAME)
  public void setName2(String name) {
    this.name = name;
  }

  @JsonGetter(SCHEMA_DESCRIPTION)
  public String getDescription() {
    return description;
  }

  @JsonSetter("description")
  public void setDescription1(String description) {
    this.description = description;
  }

  @JsonSetter(SCHEMA_DESCRIPTION)
  public void setDescription2(String description) {
    this.description = description;
  }

  @JsonGetter(PAV_CREATED_ON)
  public ProvenanceTime getCreatedOn() {
    return createdOn;
  }

  @JsonSetter("createdOn")
  public void setCreatedOn1(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonSetter(PAV_CREATED_ON)
  public void setCreatedOn2(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonGetter(PAV_LAST_UPDATED_ON)
  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  @JsonSetter("lastUpdatedOn")
  public void setLastUpdatedOn1(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonSetter(PAV_LAST_UPDATED_ON)
  public void setLastUpdatedOn2(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonGetter(PAV_CREATED_BY)
  public String getCreatedBy() {
    return createdBy;
  }

  @JsonSetter("createdBy")
  public void setCreatedBy1(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonSetter(PAV_CREATED_BY)
  public void setCreatedBy2(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonGetter(OSLC_MODIFIED_BY)
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  @JsonSetter("lastUpdatedBy")
  public void setLastUpdatedBy1(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  @JsonSetter(OSLC_MODIFIED_BY)
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
    return FolderServerNodeContext.getContext();
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

  public void setCreatedByTotal(String createdBy) {
    this.createdBy = createdBy;
    this.ownedBy = createdBy;
    this.lastUpdatedBy = createdBy;
  }

  public List<FolderServerNodeExtract> getPathInfo() {
    return pathInfo;
  }

  public void setPathInfo(List<FolderServerNodeExtract> pathInfo) {
    this.pathInfo = pathInfo;
  }
}
