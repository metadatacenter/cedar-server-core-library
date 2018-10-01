package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.auth.ResourceWithCurrentUserPermissions;

public abstract class FolderServerResourceExtract extends FolderServerNodeExtract
    implements ResourceWithCurrentUserPermissions {

  protected ResourceVersion version;
  protected BiboStatus publicationStatus;
  protected Boolean latestVersion;

  public FolderServerResourceExtract(CedarNodeType nodeType) {
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

  @JsonProperty(NodeProperty.Label.PUBLICATION_STATUS)
  public BiboStatus getPublicationStatus() {
    return publicationStatus;
  }

  @JsonProperty(NodeProperty.Label.PUBLICATION_STATUS)
  public void setPublicationStatus(String s) {
    this.publicationStatus = BiboStatus.forValue(s);
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
