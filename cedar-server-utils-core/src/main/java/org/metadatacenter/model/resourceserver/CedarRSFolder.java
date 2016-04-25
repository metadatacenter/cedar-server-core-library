package org.metadatacenter.model.resourceserver;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarFolder;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CedarRSFolder extends CedarRSNode implements CedarFolder {
  
  public CedarRSFolder() {
    super(CedarNodeType.FOLDER);
  }

}
