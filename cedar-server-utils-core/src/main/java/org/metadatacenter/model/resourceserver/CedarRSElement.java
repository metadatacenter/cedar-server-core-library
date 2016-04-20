package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarRSElement extends CedarRSResource {


  public CedarRSElement() {
    super(CedarResourceType.ELEMENT);
  }

}
