package org.metadatacenter.model.folderserverextract;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.CedarNodeType;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerInstanceExtract extends FolderServerResourceExtract {

  public FolderServerInstanceExtract() {
    super(CedarNodeType.INSTANCE);
  }

}
