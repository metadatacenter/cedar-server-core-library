package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarFolder extends CedarResource {


  public CedarFolder() {
    super(CedarResourceType.FOLDER);
  }

}
