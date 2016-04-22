package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarFolder;
import org.metadatacenter.model.CedarResourceType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarRSFolder extends CedarRSNode implements CedarFolder {
  
  public CedarRSFolder() {
    super(CedarResourceType.FOLDER);
  }

}
