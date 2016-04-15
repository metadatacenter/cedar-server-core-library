package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarElement extends CedarResource {


  public CedarElement() {
    super(CedarResourceType.ELEMENT);
  }

}
