package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.metadatacenter.model.CedarNodeType;

import static org.metadatacenter.model.ModelNodeNames.SCHEMA_IS_BASED_ON;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstanceExtract extends FolderServerResourceExtract {

  public FolderServerInstanceExtract() {
    super(CedarNodeType.INSTANCE);
  }

  private String isBasedOn;

  @JsonGetter(SCHEMA_IS_BASED_ON)
  public String getIsBasedOn() {
    return isBasedOn;
  }

  @JsonSetter("isBasedOn")
  public void setIsBasedOn1(String isBasedOn) {
    this.isBasedOn = isBasedOn;
  }

  @JsonSetter(SCHEMA_IS_BASED_ON)
  public void setIsBasedOn2(String isBasedOn) {
    this.isBasedOn = isBasedOn;
  }

}
