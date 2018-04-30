package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.FolderServerResource;

public interface VersionServiceSession {

  boolean userCanPerformVersioning(FolderServerResource resource);

  boolean resourceCanBePublished(FolderServerResource resource);

  boolean resourceCanBeDrafted(FolderServerResource resource);
}