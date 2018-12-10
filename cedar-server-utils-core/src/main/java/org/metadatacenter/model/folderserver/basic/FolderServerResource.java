package org.metadatacenter.model.folderserver.basic;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerResourceCurrentUserReport;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithPublicFlag;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithVersionData;
import org.metadatacenter.model.folderserver.datagroup.VersionDataGroup;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithPublicationStatus;
import org.metadatacenter.util.json.JsonMapper;

import java.io.IOException;

public abstract class FolderServerResource extends FolderServerNode
    implements NodeWithPublicationStatus, ResourceWithVersionData, ResourceWithPublicFlag {

  protected ResourceUri previousVersion;
  protected BiboStatus publicationStatus;
  protected ResourceUri derivedFrom;
  protected VersionDataGroup versionData;
  protected Boolean isPublic;

  public FolderServerResource(CedarNodeType nodeType) {
    super(nodeType);
    versionData = new VersionDataGroup();
  }

  public static FolderServerResource fromFolderServerResourceCurrentUserReport(FolderServerResourceCurrentUserReport cur) {
    try {
      String s = JsonMapper.MAPPER.writeValueAsString(cur);
      FolderServerNode folderServerNode = JsonMapper.MAPPER.readValue(s, FolderServerNode.class);
      return folderServerNode == null ? null : folderServerNode.asResource();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
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

  @Override
  public Boolean isPublic() {
    return isPublic;
  }

  @Override
  public void setPublic(Boolean isPublic) {
    this.isPublic = isPublic;
  }
}
