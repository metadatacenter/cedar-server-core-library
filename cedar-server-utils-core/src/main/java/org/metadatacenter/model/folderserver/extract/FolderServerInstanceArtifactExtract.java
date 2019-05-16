package org.metadatacenter.model.folderserver.extract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.InstanceArtifactWithIsBasedOn;

@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class FolderServerInstanceArtifactExtract extends FolderServerArtifactExtract
    implements InstanceArtifactWithIsBasedOn {

  private ResourceUri isBasedOn;

  public FolderServerInstanceArtifactExtract(CedarResourceType resourceType) {
    super(resourceType);
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
