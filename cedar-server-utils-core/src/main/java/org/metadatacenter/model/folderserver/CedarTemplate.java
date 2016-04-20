package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarTemplate extends CedarResource {


  public CedarTemplate() {
    super(CedarResourceType.TEMPLATE);
  }

}
