package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.ResourceVersion;

public abstract class FolderServerResource extends FolderServerNode {

  protected ResourceVersion version;
  protected ResourceUri previousVersion;
  protected BiboStatus publicationStatus;
  protected ResourceUri derivedFrom;

  public FolderServerResource(CedarNodeType nodeType) {
    super(nodeType);
  }

  @JsonGetter("pav:version")
  public ResourceVersion getVersion() {
    return version;
  }

  @JsonSetter("version")
  public void setVersion1(String v) {
    this.version = ResourceVersion.forValue(v);
  }

  @JsonSetter("pav:version")
  public void setVersion2(String v) {
    this.version = ResourceVersion.forValue(v);
  }

  @JsonGetter("pav:previousVersion")
  public ResourceUri getPreviousVersion() {
    return previousVersion;
  }

  @JsonSetter("previousVersion")
  public void setPreviousVersion1(String pv) {
    this.previousVersion = ResourceUri.forValue(pv);
  }

  @JsonSetter("pav:previousVersion")
  public void setPreviousVersion2(String pv) {
    this.previousVersion = ResourceUri.forValue(pv);
  }

  @JsonGetter("bibo:status")
  public BiboStatus getPublicationStatus() {
    return publicationStatus;
  }

  @JsonSetter("publicationStatus")
  public void setPublicationStatus1(String s) {
    this.publicationStatus = BiboStatus.forValue(s);
  }

  @JsonSetter("bibo:status")
  public void setPublicationStatus2(String s) {
    this.publicationStatus = BiboStatus.forValue(s);
  }

  @JsonGetter("pav:derivedFrom")
  public ResourceUri getDerivedFrom() {
    return derivedFrom;
  }

  @JsonSetter("derivedFrom")
  public void setDerivedFrom1(String df) {
    this.derivedFrom = ResourceUri.forValue(df);
  }

  @JsonSetter("pav:derivedFrom")
  public void setDerivedFrom2(String df) {
    this.derivedFrom = ResourceUri.forValue(df);
  }
}
