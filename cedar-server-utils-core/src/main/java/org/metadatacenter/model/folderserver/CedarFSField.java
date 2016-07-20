package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSField extends CedarFSResource {


  public CedarFSField() {
    super(CedarNodeType.FIELD);
  }

}
