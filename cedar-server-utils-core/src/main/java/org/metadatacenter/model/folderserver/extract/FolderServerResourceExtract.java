package org.metadatacenter.model.folderserver.extract;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.model.folderserver.datagroup.VersionDataGroup;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithVersionData;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithPublicationStatus;

public abstract class FolderServerResourceExtract extends FolderServerNodeExtract
    implements NodeWithPublicationStatus, ResourceWithVersionData {

  protected BiboStatus publicationStatus;
  protected VersionDataGroup versionData;

  public FolderServerResourceExtract(CedarNodeType nodeType) {
    super(nodeType);
    versionData = new VersionDataGroup();
  }

  @JsonProperty(NodeProperty.Label.PUBLICATION_STATUS)
  public BiboStatus getPublicationStatus() {
    return publicationStatus;
  }

  @JsonProperty(NodeProperty.Label.PUBLICATION_STATUS)
  public void setPublicationStatus(String s) {
    this.publicationStatus = BiboStatus.forValue(s);
  }

  @Override
  public ResourceVersion getVersion() {
    return versionData.getVersion();
  }

  @Override
  public void setVersion(String versionString) {
    versionData.setVersion(ResourceVersion.forValue(versionString));
  }

  @Override
  public Boolean isLatestVersion() {
    return versionData.isLatestVersion();
  }

  @Override
  public void setLatestVersion(Boolean latestVersion) {
    versionData.setLatestVersion(latestVersion);
  }

  @Override
  public Boolean isLatestDraftVersion() {
    return versionData.isLatestDraftVersion();
  }

  @Override
  public void setLatestDraftVersion(Boolean latestDraftVersion) {
    versionData.setLatestDraftVersion(latestDraftVersion);
  }

  @Override
  public Boolean isLatestPublishedVersion() {
    return versionData.isLatestPublishedVersion();
  }

  @Override
  public void setLatestPublishedVersion(Boolean latestPublishedVersion) {
    versionData.setLatestPublishedVersion(latestPublishedVersion);
  }
}
