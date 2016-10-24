package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ResourceServerTemplate extends ResourceServerResource {


  public ResourceServerTemplate() {
    super(CedarNodeType.TEMPLATE);
  }

}
