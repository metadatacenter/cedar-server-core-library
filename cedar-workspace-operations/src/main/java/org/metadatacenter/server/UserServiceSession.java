package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;
import java.util.List;

public interface UserServiceSession {

  //FolderServerUser ensureUserExists();

  List<FolderServerUser> findUsers();

  FolderServerUser getUser(String id);

  FolderServerUser addUserToEverybodyGroup(CedarUser user) throws IOException;
}
