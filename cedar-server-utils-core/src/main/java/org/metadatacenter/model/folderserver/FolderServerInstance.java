package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

import static org.metadatacenter.model.ModelNodeNames.SCHEMA_IS_BASED_ON;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstance extends FolderServerResource {

  private ResourceUri isBasedOn;

  public FolderServerInstance() {
    super(CedarNodeType.INSTANCE);
  }

  @JsonProperty(NodeProperty.Label.IS_BASED_ON)
  public ResourceUri getIsBasedOn() {
    return isBasedOn;
  }

  @JsonProperty(NodeProperty.Label.IS_BASED_ON)
  public void setIsBasedOn(String isBasedOn) {
    this.isBasedOn = ResourceUri.forValue(isBasedOn);
  }

}
