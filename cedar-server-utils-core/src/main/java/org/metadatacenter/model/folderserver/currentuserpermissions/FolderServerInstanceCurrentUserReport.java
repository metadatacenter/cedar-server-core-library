package org.metadatacenter.model.folderserver.currentuserpermissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.extract.FolderServerTemplateExtract;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.NodeWithIsBasedOn;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstanceCurrentUserReport extends FolderServerResourceCurrentUserReport
    implements NodeWithIsBasedOn {

  private ResourceUri isBasedOn;
  private FolderServerTemplateExtract isBasedOnExtract;

  public FolderServerInstanceCurrentUserReport() {
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

  @JsonProperty(NodeProperty.OnTheFly.IS_BASED_ON)
  public FolderServerTemplateExtract getIsBasedOnExtract() {
    return isBasedOnExtract;
  }

  @JsonProperty(NodeProperty.OnTheFly.IS_BASED_ON)
  public void setIsBasedOnExtract(FolderServerTemplateExtract isBasedOnExtract) {
    this.isBasedOnExtract = isBasedOnExtract;
  }

}
