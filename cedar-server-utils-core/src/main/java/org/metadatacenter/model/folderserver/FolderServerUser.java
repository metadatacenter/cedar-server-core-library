package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.AbstractCedarNodeWithDates;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.model.provenance.ProvenanceTime;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.user.CedarUserExtract;
import org.metadatacenter.server.security.model.user.CedarUserRepresentation;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerUser extends AbstractCedarNodeWithDates implements CedarUserRepresentation {

  private String firstName;
  private String lastName;
  private String email;
  private String name;

  public FolderServerUser() {
    this.nodeType = CedarNodeType.USER;
  }

  @JsonProperty(NodeProperty.Label.ID)
  public String getId() {
    return id;
  }

  @JsonProperty(NodeProperty.Label.ID)
  public void setId(String id) {
    this.id = id;
  }

  @JsonProperty(NodeProperty.Label.NODE_TYPE)
  public CedarNodeType getType() {
    return nodeType;
  }

  @JsonProperty(NodeProperty.Label.NODE_TYPE)
  public void setType(CedarNodeType nodeType) {
    this.nodeType = nodeType;
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

  @JsonProperty(NodeProperty.Label.FIRST_NAME)
  public String getFirstName() {
    return firstName;
  }

  @JsonProperty(NodeProperty.Label.FIRST_NAME)
  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  @JsonProperty(NodeProperty.Label.LAST_NAME)
  public String getLastName() {
    return lastName;
  }

  @JsonProperty(NodeProperty.Label.LAST_NAME)
  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  @JsonProperty(NodeProperty.Label.EMAIL)
  public String getEmail() {
    return email;
  }

  @JsonProperty(NodeProperty.Label.EMAIL)
  public void setEmail(String email) {
    this.email = email;
  }

  @JsonProperty(NodeProperty.Label.NAME)
  public String getName() {
    return name;
  }

  @JsonProperty(NodeProperty.Label.NAME)
  public void setName(String name) {
    this.name = name;
  }

  public CedarUserExtract buildExtract() {
    return new CedarUserExtract(getId(), getFirstName(), getLastName(), getEmail());
  }
}
