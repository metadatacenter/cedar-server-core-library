package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public abstract class FolderServerResource extends FolderServerNode {

  protected ResourceVersion version;
  protected ResourceUri previousVersion;
  protected BiboStatus publicationStatus;
  protected ResourceUri derivedFrom;
  protected Boolean latestVersion;

  public FolderServerResource(CedarNodeType nodeType) {
    super(nodeType);
  }

  @JsonProperty(NodeProperty.Label.VERSION)
  public ResourceVersion getVersion() {
    return version;
  }

  @JsonProperty(NodeProperty.Label.VERSION)
  public void setVersion(String v) {
    this.version = ResourceVersion.forValue(v);
  }

  @JsonProperty(NodeProperty.Label.PREVIOUS_VERSION)
  public ResourceUri getPreviousVersion() {
    return previousVersion;
  }

  @JsonProperty(NodeProperty.Label.PREVIOUS_VERSION)
  public void setPreviousVersion(String pv) {
    this.previousVersion = ResourceUri.forValue(pv);
  }

  @JsonProperty(NodeProperty.Label.PUBLICATION_STATUS)
  public BiboStatus getPublicationStatus() {
    return publicationStatus;
  }

  @JsonProperty(NodeProperty.Label.PUBLICATION_STATUS)
  public void setPublicationStatus(String s) {
    this.publicationStatus = BiboStatus.forValue(s);
  }

  @JsonProperty(NodeProperty.Label.DERIVED_FROM)
  public ResourceUri getDerivedFrom() {
    return derivedFrom;
  }

  @JsonProperty(NodeProperty.Label.DERIVED_FROM)
  public void setDerivedFrom(String df) {
    this.derivedFrom = ResourceUri.forValue(df);
  }

  @JsonProperty(NodeProperty.Label.IS_LATEST_VERSION)
  public Boolean isLatestVersion() {
    return latestVersion;
  }

  @JsonProperty(NodeProperty.Label.IS_LATEST_VERSION)
  public void setLatestVersion(Boolean latestVersion) {
    this.latestVersion = latestVersion;
  }
}
