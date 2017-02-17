package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.GroupServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.neo4j.RelationLabel;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.CedarGroupUser;
import org.metadatacenter.server.security.model.auth.CedarGroupUsers;
import org.metadatacenter.server.security.model.auth.CedarGroupUsersRequest;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.*;

public class Neo4JUserSessionGroupService extends AbstractNeo4JUserSession implements GroupServiceSession {

  public Neo4JUserSessionGroupService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu) {
    super(cedarConfig, proxies, cu);
  }

  public static GroupServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser) {
    return new Neo4JUserSessionGroupService(cedarConfig, proxies, cedarUser);
  }

  @Override
  public List<FolderServerGroup> findGroups() {
    return proxies.group().findGroups();
  }

  @Override
  public FolderServerGroup findGroupById(String groupURL) {
    return proxies.group().findGroupById(groupURL);
  }

  @Override
  public FolderServerGroup findGroupByName(String groupName) {
    return proxies.group().findGroupByName(groupName);
  }

  @Override
  public FolderServerGroup createGroup(String groupName, String groupDisplayName, String groupDescription) {
    String groupURL = linkedDataUtil.buildNewLinkedDataId(CedarNodeType.GROUP);
    return proxies.group().createGroup(groupURL, groupName, groupDisplayName, groupDescription, getUserId(), null);
  }

  @Override
  public FolderServerGroup updateGroupById(String groupURL, Map<String, String> updateFields) {
    return proxies.group().updateGroupById(groupURL, updateFields, getUserId());
  }

  @Override
  public boolean deleteGroupById(String groupURL) {
    return proxies.group().deleteGroupById(groupURL);
  }

  @Override
  public BackendCallResult updateGroupUsers(String groupURL, CedarGroupUsersRequest request) {

    GroupUsersRequestValidator gurv = new GroupUsersRequestValidator(this, groupURL, request);
    BackendCallResult bcr = gurv.getCallResult();
    if (bcr.isError()) {
      return bcr;
    } else {
      CedarGroupUsers currentGroupUsers = findGroupUsers(groupURL);
      CedarGroupUsers newGroupUsers = gurv.getUsers();

      Neo4JUserSessionGroupOperations.updateGroupUsers(proxies.group(), groupURL, currentGroupUsers, newGroupUsers,
          RelationLabel.ADMINISTERS, Neo4JUserSessionGroupOperations.Filter.ADMINISTRATOR);
      Neo4JUserSessionGroupOperations.updateGroupUsers(proxies.group(), groupURL, currentGroupUsers, newGroupUsers,
          RelationLabel.MEMBEROF, Neo4JUserSessionGroupOperations.Filter.MEMBER);

      return new BackendCallResult();
    }
  }

  @Override
  public CedarGroupUsers findGroupUsers(String groupURL) {
    Set<String> memberIds = new HashSet<>();
    Set<String> administratorsIds = new HashSet<>();
    Map<String, FolderServerUser> users = new HashMap<>();
    List<FolderServerUser> groupMembers = proxies.group().findGroupMembers(groupURL);
    for (FolderServerUser member : groupMembers) {
      memberIds.add(member.getId());
      users.put(member.getId(), member);
    }
    List<FolderServerUser> groupAdministrators = proxies.group().findGroupAdministrators(groupURL);
    for (FolderServerUser administrator : groupAdministrators) {
      administratorsIds.add(administrator.getId());
      users.put(administrator.getId(), administrator);
    }
    CedarGroupUsers ret = new CedarGroupUsers();
    for (String userURL : users.keySet()) {
      ret.addUser(new CedarGroupUser(users.get(userURL).buildExtract(),
          administratorsIds.contains(userURL),
          memberIds.contains(userURL))
      );
    }
    return ret;
  }

  @Override
  public boolean userAdministersGroup(String groupURL) {
    CedarGroupUsers groupUsers = findGroupUsers(groupURL);
    if (groupUsers != null) {
      String currentUserId = getUserId();
      for (CedarGroupUser user : groupUsers.getUsers()) {
        if (currentUserId.equals(user.getUser().getId()) && user.isAdministrator()) {
          return true;
        }
      }
    }
    return false;
  }
}
