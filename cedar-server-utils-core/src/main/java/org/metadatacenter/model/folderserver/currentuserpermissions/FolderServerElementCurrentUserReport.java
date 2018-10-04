package org.metadatacenter.model.folderserver.currentuserpermissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.report.FolderServerResourceReport;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerElementCurrentUserReport extends FolderServerResourceCurrentUserReport {

  public FolderServerElementCurrentUserReport() {
    super(CedarNodeType.ELEMENT);
  }
}
