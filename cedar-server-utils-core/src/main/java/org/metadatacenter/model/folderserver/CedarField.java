package org.metadatacenter.model.folderserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarField extends CedarResource {


  public CedarField() {
    super(CedarResourceType.FIELD);
  }

}
