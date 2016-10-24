package org.metadatacenter.model.resourceserver;

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
    @JsonSubTypes.Type(value = ResourceServerFolder.class, name = CedarNodeType.Types.FOLDER),
    @JsonSubTypes.Type(value = ResourceServerField.class, name = CedarNodeType.Types.FIELD),
    @JsonSubTypes.Type(value = ResourceServerElement.class, name = CedarNodeType.Types.ELEMENT),
    @JsonSubTypes.Type(value = ResourceServerTemplate.class, name = CedarNodeType.Types.TEMPLATE),
    @JsonSubTypes.Type(value = ResourceServerInstance.class, name = CedarNodeType.Types.INSTANCE)
})
public abstract class ResourceServerNode extends AbstractCedarNode {

  private List<NodePermission> currentUserPermissions;

  protected String createdByUserName;
  protected String lastUpdatedByUserName;
  protected String ownedByUserName;
  protected String displayPath;
  protected String displayParentPath;

  protected ResourceServerNode(CedarNodeType nodeType) {
    this.nodeType = nodeType;
    this.currentUserPermissions = new ArrayList<>();
  }

  @JsonProperty("@id")
  public String getId() {
    return id;
  }

  @JsonProperty("@id")
  public void setId(String id) {
    this.id = id;
  }

  public CedarNodeType getType() {
    return nodeType;
  }

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

  @JsonProperty("pav:createdOn")
  public void setCreatedOn(ProvenanceTime createdOn) {
    this.createdOn = createdOn;
  }

  @JsonProperty("pav:lastUpdatedOn")
  public ProvenanceTime getLastUpdatedOn() {
    return lastUpdatedOn;
  }

  @JsonProperty("pav:lastUpdatedOn")
  public void setLastUpdatedOn(ProvenanceTime lastUpdatedOn) {
    this.lastUpdatedOn = lastUpdatedOn;
  }

  @JsonProperty("pav:createdBy")
  public String getCreatedBy() {
    return createdBy;
  }

  @JsonProperty("pav:createdBy")
  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  @JsonProperty("oslc:modifiedBy")
  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  @JsonProperty("oslc:modifiedBy")
  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public String getCreatedByUserName() {
    return createdByUserName;
  }

  public void setCreatedByUserName(String createdByUserName) {
    this.createdByUserName = createdByUserName;
  }

  public String getLastUpdatedByUserName() {
    return lastUpdatedByUserName;
  }

  public void setLastUpdatedByUserName(String lastUpdatedByUserName) {
    this.lastUpdatedByUserName = lastUpdatedByUserName;
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

  public String getOwnedByUserName() {
    return ownedByUserName;
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

  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return CONTEXT;
  }

  public static ResourceServerNode forType(CedarNodeType t) {
    switch (t) {
      case FOLDER:
        return new ResourceServerFolder();
      case FIELD:
        return new ResourceServerField();
      case ELEMENT:
        return new ResourceServerElement();
      case TEMPLATE:
        return new ResourceServerTemplate();
      case INSTANCE:
        return new ResourceServerInstance();
    }
    return null;
  }

  public List<NodePermission> getCurrentUserPermissions() {
    return currentUserPermissions;
  }

  public void setCurrentUserPermissions(List<NodePermission> currentUserPermissions) {
    this.currentUserPermissions = currentUserPermissions;
  }

}
