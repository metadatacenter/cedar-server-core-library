package org.metadatacenter.model.folderserver.extract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;
import org.metadatacenter.model.folderserver.datagroup.NameDescriptionIdentifierGroup;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerCategoryExtractWithChildren {

  protected String id;
  protected CedarResourceType resourceType;
  protected NameDescriptionIdentifierGroup nameDescriptionIdentifierGroup;
  protected String parentCategoryId;
  protected List<FolderServerCategoryExtractWithChildren> children;

  public FolderServerCategoryExtractWithChildren() {
    this.nameDescriptionIdentifierGroup = new NameDescriptionIdentifierGroup();
    this.children = new ArrayList<>();
  }

  public static FolderServerCategoryExtractWithChildren fromCategory(FolderServerCategory category) {
    FolderServerCategoryExtractWithChildren c = new FolderServerCategoryExtractWithChildren();
    c.setId(category.getId());
    c.setType(category.getType());
    c.setName(category.getName());
    c.setDescription(category.getDescription());
    c.setIdentifier(category.getIdentifier());
    c.setParentCategoryId(category.getParentCategoryId());
    return c;
  }

  public String getParentCategoryId() {
    return parentCategoryId;
  }

  public void setParentCategoryId(String parentCategoryId) {
    this.parentCategoryId = parentCategoryId;
  }

  @JsonProperty(NodeProperty.Label.ID)
  public String getId() {
    return id;
  }

  @JsonProperty(NodeProperty.Label.ID)
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty(NodeProperty.Label.RESOURCE_TYPE)
  public CedarResourceType getType() {
    return resourceType;
  }

  @JsonProperty(NodeProperty.Label.RESOURCE_TYPE)
  public void setType(CedarResourceType resourceType) {
    this.resourceType = resourceType;
  }

  @JsonProperty(NodeProperty.Label.NAME)
  public String getName() {
    return this.nameDescriptionIdentifierGroup.getName();
  }

  @JsonProperty(NodeProperty.Label.NAME)
  public void setName(String name) {
    this.nameDescriptionIdentifierGroup.setName(name);
  }

  @JsonProperty(NodeProperty.Label.DESCRIPTION)
  public String getDescription() {
    return this.nameDescriptionIdentifierGroup.getDescription();
  }

  @JsonProperty(NodeProperty.Label.DESCRIPTION)
  public void setDescription(String description) {
    this.nameDescriptionIdentifierGroup.setDescription(description);
  }

  @JsonProperty(NodeProperty.Label.IDENTIFIER)
  public String getIdentifier() {
    return this.nameDescriptionIdentifierGroup.getIdentifier();
  }

  @JsonProperty(NodeProperty.Label.IDENTIFIER)
  public void setIdentifier(String identifier) {
    this.nameDescriptionIdentifierGroup.setIdentifier(identifier);
  }

  public List<FolderServerCategoryExtractWithChildren> getChildren() {
    return this.children;
  }
}
