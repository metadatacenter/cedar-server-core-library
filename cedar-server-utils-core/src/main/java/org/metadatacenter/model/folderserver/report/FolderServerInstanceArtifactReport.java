package org.metadatacenter.model.folderserver.report;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.id.CedarTemplateId;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.datagroup.IsBasedOnGroup;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithIsBasedOn;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithOpenFlag;
import org.metadatacenter.model.folderserver.extract.FolderServerTemplateExtract;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public abstract class FolderServerInstanceArtifactReport extends FolderServerArtifactReport
    implements ResourceWithIsBasedOn, ResourceWithOpenFlag {

  private IsBasedOnGroup isBasedOnGroup;
  private FolderServerTemplateExtract isBasedOnExtract;

  public FolderServerInstanceArtifactReport(CedarResourceType resourceType) {
    super(resourceType);
    isBasedOnGroup = new IsBasedOnGroup();
  }

  @Override
  public CedarTemplateId getIsBasedOn() {
    return isBasedOnGroup.getIsBasedOn();
  }

  @Override
  public void setIsBasedOn(CedarTemplateId isBasedOn) {
    isBasedOnGroup.setIsBasedOn(isBasedOn);
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
