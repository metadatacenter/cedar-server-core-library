package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarRSElement extends CedarRSResource {


  public CedarRSElement() {
    super(CedarNodeType.ELEMENT);
  }

}
