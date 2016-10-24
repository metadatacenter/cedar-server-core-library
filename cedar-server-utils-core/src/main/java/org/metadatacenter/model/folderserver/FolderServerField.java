package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerField extends FolderServerResource {


  public FolderServerField() {
    super(CedarNodeType.FIELD);
  }

}
