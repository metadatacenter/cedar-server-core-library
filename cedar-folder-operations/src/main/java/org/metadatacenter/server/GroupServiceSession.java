package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.CedarGroupUsers;
import org.metadatacenter.server.security.model.auth.CedarGroupUsersRequest;

import java.util.List;
import java.util.Map;

public interface GroupServiceSession extends ServiceSession {
  List<FolderServerGroup> findGroups();

  FolderServerGroup findGroupById(String groupURL);

  FolderServerGroup findGroupByName(String groupName);

  FolderServerGroup createGroup(String groupName, String groupDisplayName, String groupDescription);

  FolderServerGroup updateGroupById(String groupURL, Map<String, String> updateFields);

  boolean deleteGroupById(String groupURL);

  CedarGroupUsers findGroupUsers(String groupURL);

  BackendCallResult updateGroupUsers(String groupURL, CedarGroupUsersRequest request);
}
