package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarInstance extends CedarResource {


  public CedarInstance() {
    super(CedarResourceType.INSTANCE);
  }

}
