package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstance extends FolderServerResource {


  public FolderServerInstance() {
    super(CedarNodeType.INSTANCE);
  }

}
