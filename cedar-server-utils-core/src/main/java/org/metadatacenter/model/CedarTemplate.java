package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarTemplate extends CedarResource {


  public CedarTemplate() {
    super(CedarResourceType.TEMPLATE);
  }

}
