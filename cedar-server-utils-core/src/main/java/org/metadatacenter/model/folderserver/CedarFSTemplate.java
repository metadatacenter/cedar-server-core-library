package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSTemplate extends CedarFSResource {


  public CedarFSTemplate() {
    super(CedarResourceType.TEMPLATE);
  }

}
