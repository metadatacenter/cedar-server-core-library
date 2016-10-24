package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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

  protected FolderServerNode(CedarNodeType nodeType) {
    this.nodeType = nodeType;
    this.currentUserPermissions = new ArrayList<>();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("id")
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

  @JsonProperty("pav:createdOn")
  public ProvenanceTime getCreatedOn() {
    return createdOn;
  }

  @JsonProperty("createdOn")
  public void setCreatedOn(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonProperty("pav:lastUpdatedOn")
  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  @JsonProperty("lastUpdatedOn")
  public void setLastUpdatedOn(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonProperty("pav:createdBy")
  public String getCreatedBy() {
    return createdBy;
  }

  @JsonProperty("createdBy")
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonProperty("oslc:modifiedBy")
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  @JsonProperty("lastUpdatedBy")
  public void setLastUpdatedBy(String lastUpdatedBy) {
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
