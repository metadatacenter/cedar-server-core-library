package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerUser;

import java.util.List;

/**
 * Created by egyedia on 10/24/16.
 */
public interface UserServiceSession {
  FolderServerFolder ensureUserHomeExists();

  FolderServerUser ensureUserExists();

  List<FolderServerUser> findUsers();

  FolderServerUser findUserById(String userURL);
}
