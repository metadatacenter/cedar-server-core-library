package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarRSTemplate extends CedarRSResource {


  public CedarRSTemplate() {
    super(CedarResourceType.TEMPLATE);
  }

}
