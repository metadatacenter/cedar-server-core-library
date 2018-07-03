package org.metadatacenter.model.folderserverreport;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserverextract.FolderServerTemplateExtract;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstanceReport extends FolderServerResourceReport {

  private ResourceUri isBasedOn;
  private FolderServerTemplateExtract isBasedOnExtract;

  public FolderServerInstanceReport() {
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
