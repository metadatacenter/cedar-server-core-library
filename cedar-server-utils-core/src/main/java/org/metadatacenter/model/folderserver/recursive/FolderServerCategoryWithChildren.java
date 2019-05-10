package org.metadatacenter.model.folderserver.recursive;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FolderServerCategoryWithChildren extends FolderServerCategory {

  public FolderServerCategoryWithChildren() {
    super();
  }
}
