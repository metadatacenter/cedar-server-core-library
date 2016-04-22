package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFSInstance extends CedarFSResource {


  public CedarFSInstance() {
    super(CedarResourceType.INSTANCE);
  }

}
