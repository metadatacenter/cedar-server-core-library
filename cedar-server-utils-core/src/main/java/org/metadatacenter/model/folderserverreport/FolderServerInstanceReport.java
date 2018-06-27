package org.metadatacenter.model.folderserverreport;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserverextract.FolderServerTemplateExtract;

import static org.metadatacenter.model.ModelNodeNames.SCHEMA_IS_BASED_ON;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstanceReport extends FolderServerResourceReport {

  private ResourceUri isBasedOn;
  private FolderServerTemplateExtract isBasedOn_;

  public FolderServerInstanceReport() {
    super(CedarNodeType.INSTANCE);
  }

  @JsonGetter(SCHEMA_IS_BASED_ON)
  public ResourceUri getIsBasedOn() {
    return isBasedOn;
  }

  @JsonSetter("isBasedOn")
  public void setIsBasedOn1(String isBasedOn) {
    this.isBasedOn = ResourceUri.forValue(isBasedOn);
  }

  @JsonSetter(SCHEMA_IS_BASED_ON)
  public void setIsBasedOn2(String isBasedOn) {
    this.isBasedOn = ResourceUri.forValue(isBasedOn);
  }

  @JsonGetter("isBasedOn_")
  public FolderServerTemplateExtract getIsBasedOn_() {
    return isBasedOn_;
  }

  @JsonSetter("isBasedOn_")
  public void setIsBasedOn_(FolderServerTemplateExtract isBasedOn_) {
    this.isBasedOn_ = isBasedOn_;
  }

}
