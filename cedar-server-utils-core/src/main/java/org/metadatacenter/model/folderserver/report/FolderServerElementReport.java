package org.metadatacenter.model.folderserver.report;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerElementReport extends FolderServerResourceReport {

  public FolderServerElementReport() {
    super(CedarNodeType.ELEMENT);
  }
}
