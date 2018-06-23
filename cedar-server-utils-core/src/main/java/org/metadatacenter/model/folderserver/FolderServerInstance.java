package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;

import static org.metadatacenter.model.ModelNodeNames.SCHEMA_IS_BASED_ON;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstance extends FolderServerResource {

  private ResourceUri isBasedOn;

  public FolderServerInstance() {
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

}
