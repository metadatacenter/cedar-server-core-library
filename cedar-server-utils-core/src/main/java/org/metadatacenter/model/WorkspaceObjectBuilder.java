package org.metadatacenter.model;

import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.basic.FolderServerResource;
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

}
