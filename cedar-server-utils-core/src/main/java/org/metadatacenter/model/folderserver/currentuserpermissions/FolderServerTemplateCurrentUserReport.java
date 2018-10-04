package org.metadatacenter.model.folderserver.currentuserpermissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.report.FolderServerResourceReport;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerTemplateCurrentUserReport extends FolderServerResourceCurrentUserReport {

  private long numberOfInstances;

  public FolderServerTemplateCurrentUserReport() {
    super(CedarNodeType.TEMPLATE);
  }

  @JsonProperty(NodeProperty.OnTheFly.NUMBER_OF_INSTANCES)
  public long getNumberOfInstances() {
    return numberOfInstances;
  }

  @JsonProperty(NodeProperty.OnTheFly.NUMBER_OF_INSTANCES)
  public void setNumberOfInstances(long numberOfInstances) {
    this.numberOfInstances = numberOfInstances;
  }
}
