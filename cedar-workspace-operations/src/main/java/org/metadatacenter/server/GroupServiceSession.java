package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.CedarGroupUsers;
import org.metadatacenter.server.security.model.auth.CedarGroupUsersRequest;

import java.util.List;
import java.util.Map;

public interface GroupServiceSession {

  List<FolderServerGroup> findGroups();

  FolderServerGroup findGroupById(String groupURL);

  FolderServerGroup findGroupByName(String groupName);

  FolderServerGroup createGroup(String groupName, String groupDisplayName, String groupDescription);

  FolderServerGroup updateGroupById(String groupURL, Map<NodeProperty, String> updateFields);

  boolean deleteGroupById(String groupURL);

  CedarGroupUsers findGroupUsers(String groupURL);

  BackendCallResult updateGroupUsers(String groupURL, CedarGroupUsersRequest request);

  boolean userAdministersGroup(String groupURL);
}
