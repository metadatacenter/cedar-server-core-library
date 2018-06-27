package org.metadatacenter.model.folderserverreport;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerTemplateReport extends FolderServerResourceReport {

  private long numberOfInstances;

  public FolderServerTemplateReport() {
    super(CedarNodeType.TEMPLATE);
  }

  @JsonGetter("numberOfInstances")
  public long getNumberOfInstances() {
    return numberOfInstances;
  }

  public void setNumberOfInstances(long numberOfInstances) {
    this.numberOfInstances = numberOfInstances;
  }
}
