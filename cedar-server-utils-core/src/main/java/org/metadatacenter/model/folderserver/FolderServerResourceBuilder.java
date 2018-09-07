package org.metadatacenter.model.folderserver;

import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceVersion;

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
        return new FolderServerInstance();
      default:
        return null;
    }
  }

  public static FolderServerResource forNodeType(CedarNodeType nodeType, String newId, String name,
                                                 String description, ResourceVersion version,
                                                 BiboStatus publicationStatus) {
    FolderServerResource r = forNodeType(nodeType);
    r.setId(newId);
    r.setType(nodeType);
    r.setName(name);
    r.setDescription(description);
    r.setVersion(version.getValue());
    r.setPublicationStatus(publicationStatus.getValue());
    return r;
  }
}
