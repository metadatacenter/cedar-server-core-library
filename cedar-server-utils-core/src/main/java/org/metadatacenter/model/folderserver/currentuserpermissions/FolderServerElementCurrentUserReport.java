package org.metadatacenter.model.folderserver.currentuserpermissions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerElementCurrentUserReport extends FolderServerResourceCurrentUserReport {

  public FolderServerElementCurrentUserReport() {
    super(CedarNodeType.ELEMENT);
  }
}
