package org.metadatacenter.model.folderserver;

import org.metadatacenter.model.CedarNodeType;

public class FolderServerResourceBuilder {

  private FolderServerResourceBuilder() {

  }

  public static FolderServerResource forNodeType(CedarNodeType nodeType) {
    switch (nodeType) {
      case TEMPLATE:
        return new FolderServerTemplate();
      case ELEMENT:
        return new FolderServerElement();
      case FIELD:
        return new FolderServerField();
      case INSTANCE:
        return new FolderServerElement();
      default:
        return null;
    }
  }
}
