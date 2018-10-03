package org.metadatacenter.model.folderserver.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerElement extends FolderServerResource {

  public FolderServerElement() {
    super(CedarNodeType.ELEMENT);
  }

}
