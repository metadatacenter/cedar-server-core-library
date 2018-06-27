package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceVersion;

import static org.metadatacenter.model.ModelNodeNames.BIBO_STATUS;
import static org.metadatacenter.model.ModelNodeNames.PAV_VERSION;

public abstract class FolderServerResourceExtract extends FolderServerNodeExtract {

  protected ResourceVersion version;
  protected BiboStatus publicationStatus;
  protected Boolean latestVersion;

  public FolderServerResourceExtract(CedarNodeType nodeType) {
    super(nodeType);
  }

  @JsonGetter(PAV_VERSION)
  public ResourceVersion getVersion() {
    return version;
  }

  @JsonSetter("version")
  public void setVersion1(String v) {
    this.version = ResourceVersion.forValue(v);
  }

  @JsonSetter(PAV_VERSION)
  public void setVersion2(String v) {
    this.version = ResourceVersion.forValue(v);
  }

  @JsonGetter(BIBO_STATUS)
  public BiboStatus getPublicationStatus() {
    return publicationStatus;
  }

  @JsonSetter("publicationStatus")
  public void setPublicationStatus1(String s) {
    this.publicationStatus = BiboStatus.forValue(s);
  }

  @JsonSetter(BIBO_STATUS)
  public void setPublicationStatus2(String s) {
    this.publicationStatus = BiboStatus.forValue(s);
  }

  @JsonProperty("isLatestVersion")
  public Boolean isLatestVersion() {
    return latestVersion;
  }

  @JsonProperty("isLatestVersion")
  public void setLatestVersion(Boolean latestVersion) {
    this.latestVersion = latestVersion;
  }
}
