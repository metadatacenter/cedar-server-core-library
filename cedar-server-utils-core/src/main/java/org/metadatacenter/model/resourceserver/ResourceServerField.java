package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceServerField extends ResourceServerResource {


  public ResourceServerField() {
    super(CedarNodeType.FIELD);
  }

}
