package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarField extends CedarResource {


  public CedarField() {
    super(CedarResourceType.FIELD);
  }

}
