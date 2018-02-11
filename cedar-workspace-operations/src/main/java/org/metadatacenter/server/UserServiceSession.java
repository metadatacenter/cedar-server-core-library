package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.FolderServerUser;

import java.util.List;

public interface UserServiceSession {

  FolderServerUser ensureUserExists();

  List<FolderServerUser> findUsers();

  FolderServerUser getUser(String id);
}
