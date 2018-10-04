package org.metadatacenter.model.folderserver.extract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerElementExtract extends FolderServerResourceExtract {

  public FolderServerElementExtract() {
    super(CedarNodeType.ELEMENT);
  }

}
