package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithIsBasedOn;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstance extends FolderServerResource implements NodeWithIsBasedOn {

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
