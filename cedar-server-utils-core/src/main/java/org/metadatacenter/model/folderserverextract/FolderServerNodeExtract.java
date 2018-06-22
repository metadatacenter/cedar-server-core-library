package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.*;
import org.metadatacenter.model.AbstractCedarNodeExtract;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;
import java.util.Map;

import static org.metadatacenter.model.ModelNodeNames.SCHEMA_DESCRIPTION;
import static org.metadatacenter.model.ModelNodeNames.SCHEMA_NAME;
import static org.metadatacenter.util.provenance.ProvenanceUtil.*;

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

  @JsonProperty("@context")
  public Map<String, String> getContext() {
    return CONTEXT;
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

  public void setCreatedByTotal(String createdBy) {
    this.createdBy = createdBy;
    this.ownedBy = createdBy;
    this.lastUpdatedBy = createdBy;
  }

}
