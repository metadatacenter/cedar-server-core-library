package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSField extends CedarFSResource {


  public CedarFSField() {
    super(CedarResourceType.FIELD);
  }

}
