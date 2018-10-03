package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithIsBasedOn;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstanceExtract extends FolderServerResourceExtract implements NodeWithIsBasedOn {

  private ResourceUri isBasedOn;

  public FolderServerInstanceExtract() {
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
