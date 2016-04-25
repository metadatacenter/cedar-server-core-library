package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSElement extends CedarFSResource {


  public CedarFSElement() {
    super(CedarNodeType.ELEMENT);
  }

}
