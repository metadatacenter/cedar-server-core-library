package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarRSField extends CedarRSResource {


  public CedarRSField() {
    super(CedarResourceType.FIELD);
  }

}
