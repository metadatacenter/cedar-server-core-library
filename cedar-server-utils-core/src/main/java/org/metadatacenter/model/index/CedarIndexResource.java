package org.metadatacenter.model.index;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.metadatacenter.model.resourceserver.CedarRSNode;

public class CedarIndexResource {

  @JsonProperty("info")
  private CedarRSNode info;
  // Additional properties here...

  // Used by Jackson
  public CedarIndexResource() {};

  public CedarIndexResource(CedarRSNode info) {
    this.info = info;
  }

  public CedarRSNode getInfo() {
    return info;
  }

  public void setInfo(CedarRSNode info) {
    this.info = info;
  }
}

