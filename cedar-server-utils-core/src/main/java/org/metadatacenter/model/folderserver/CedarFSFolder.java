package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarFolder;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSFolder extends CedarFSNode implements CedarFolder {


  public CedarFSFolder() {
    super(CedarResourceType.FOLDER);
  }

}
