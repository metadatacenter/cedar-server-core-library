package org.metadatacenter.model;

import org.metadatacenter.model.folderserver.basic.*;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;

public abstract class WorkspaceObjectBuilder {

  private static final Logger log = LoggerFactory.getLogger(WorkspaceObjectBuilder.class);

  public static FolderServerResource artifact(InputStream stream) throws IOException {
    FolderServerNode folderServerNode = JsonMapper.MAPPER.readValue(stream, FolderServerNode.class);
    return (FolderServerResource)folderServerNode;
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
                                                 String description, String identifier, ResourceVersion version,
                                                 BiboStatus publicationStatus) {
    FolderServerResource r = forNodeType(nodeType);
    r.setId(newId);
    r.setType(nodeType);
    r.setName(name);
    r.setDescription(description);
    r.setIdentifier(identifier);
    if (nodeType.isVersioned()) {
      r.setVersion(version.getValue());
      r.setPublicationStatus(publicationStatus.getValue());
    }
    return r;
  }
}
