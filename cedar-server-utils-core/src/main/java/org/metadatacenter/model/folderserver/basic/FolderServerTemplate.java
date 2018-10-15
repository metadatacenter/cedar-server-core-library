package org.metadatacenter.model.folderserver.basic;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerTemplate extends FolderServerResource {


  public FolderServerTemplate() {
    super(CedarNodeType.TEMPLATE);
  }

}