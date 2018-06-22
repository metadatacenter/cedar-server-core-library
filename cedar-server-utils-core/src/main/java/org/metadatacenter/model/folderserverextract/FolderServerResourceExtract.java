package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.ResourceVersion;

import static org.metadatacenter.model.ModelNodeNames.*;

public abstract class FolderServerResourceExtract extends FolderServerNodeExtract {

  protected ResourceVersion version;
  protected ResourceUri previousVersion;
  protected BiboStatus publicationStatus;
  protected ResourceUri derivedFrom;
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

  @JsonGetter(PAV_PREVIOUS_VERSION)
  public ResourceUri getPreviousVersion() {
    return previousVersion;
  }

  @JsonSetter("previousVersion")
  public void setPreviousVersion1(String pv) {
    this.previousVersion = ResourceUri.forValue(pv);
  }

  @JsonSetter(PAV_PREVIOUS_VERSION)
  public void setPreviousVersion2(String pv) {
    this.previousVersion = ResourceUri.forValue(pv);
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

  @JsonGetter(PAV_DERIVED_FROM)
  public ResourceUri getDerivedFrom() {
    return derivedFrom;
  }

  @JsonSetter("derivedFrom")
  public void setDerivedFrom1(String df) {
    this.derivedFrom = ResourceUri.forValue(df);
  }

  @JsonSetter(PAV_DERIVED_FROM)
  public void setDerivedFrom2(String df) {
    this.derivedFrom = ResourceUri.forValue(df);
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
