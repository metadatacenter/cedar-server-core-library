package org.metadatacenter.server.neo4j;

import org.metadatacenter.server.security.model.auth.CedarGroupUser;
import org.metadatacenter.server.security.model.auth.CedarGroupUsers;
import org.metadatacenter.server.security.model.auth.NodePermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.auth.NodePermissionUserPermissionPair;
import org.metadatacenter.server.security.model.user.CedarUserId;

import java.util.HashSet;
import java.util.Set;

public final class Neo4JUserSessionGroupOperations {

  enum Filter {
    ADMINISTRATOR, MEMBER
  }

  private Neo4JUserSessionGroupOperations() {
  }

  static void updateGroupUsers(Neo4JProxy neo4JProxy, String groupURL, CedarGroupUsers currentGroupUsers,
                               CedarGroupUsers newGroupUsers, RelationLabel relation, Filter filter) {
    Set<CedarUserId> oldUsers = new HashSet<>();
    for (CedarGroupUser gu : currentGroupUsers.getUsers()) {
      if ((filter == Filter.ADMINISTRATOR && gu.isAdministrator()) || (filter == Filter.MEMBER && gu.isMember())) {
        oldUsers.add(gu.getUserId());
      }
    }
    Set<CedarUserId> newUsers = new HashSet<>();
    for (CedarGroupUser gu : newGroupUsers.getUsers()) {
      if ((filter == Filter.ADMINISTRATOR && gu.isAdministrator()) || (filter == Filter.MEMBER && gu.isMember())) {
        newUsers.add(gu.getUserId());
      }
    }

    Set<CedarUserId> toRemoveUsers = new HashSet<>();
    toRemoveUsers.addAll(oldUsers);
    toRemoveUsers.removeAll(newUsers);
    if (!toRemoveUsers.isEmpty()) {
      removeGroupUsers(neo4JProxy, groupURL, toRemoveUsers, relation);
    }

    Set<CedarUserId> toAddUsers = new HashSet<>();
    toAddUsers.addAll(newUsers);
    toAddUsers.removeAll(oldUsers);
    if (!toAddUsers.isEmpty()) {
      addGroupUsers(neo4JProxy, groupURL, toAddUsers, relation);
    }
  }

  private static void addGroupUsers(Neo4JProxy neo4JProxy, String groupURL, Set<CedarUserId> toAddUsers,
                                    RelationLabel relation) {
    for (CedarUserId cuid : toAddUsers) {
      neo4JProxy.addUserGroupRelation(cuid.getId(), groupURL, relation);
    }
  }

  private static void removeGroupUsers(Neo4JProxy neo4JProxy, String groupURL, Set<CedarUserId> toRemoveUsers,
                                       RelationLabel relation) {
    for (CedarUserId cuid : toRemoveUsers) {
      neo4JProxy.removeUserGroupRelation(cuid.getId(), groupURL, relation);
    }
  }

  static void addGroupPermissions(Neo4JProxy neo4JProxy, String nodeURL, Set<NodePermissionGroupPermissionPair>
      toAddGroupPermissions, boolean nodeIsFolder) {
    for (NodePermissionGroupPermissionPair pair : toAddGroupPermissions) {
      neo4JProxy.addPermissionToGroup(nodeURL, pair.getGroup().getId(), pair.getPermission(), nodeIsFolder);
    }
  }

  static void removeGroupPermissions(Neo4JProxy neo4JProxy, String nodeURL, Set<NodePermissionGroupPermissionPair>
      toRemoveGroupPermissions, boolean nodeIsFolder) {
    for (NodePermissionGroupPermissionPair pair : toRemoveGroupPermissions) {
      neo4JProxy.removePermissionFromGroup(nodeURL, pair.getGroup().getId(), pair.getPermission(), nodeIsFolder);
    }
  }

  static void addUserPermissions(Neo4JProxy neo4JProxy, String nodeURL, Set<NodePermissionUserPermissionPair>
      toAddUserPermissions, boolean nodeIsFolder) {
    for (NodePermissionUserPermissionPair pair : toAddUserPermissions) {
      neo4JProxy.addPermissionToUser(nodeURL, pair.getUser().getId(), pair.getPermission(), nodeIsFolder);
    }
  }

  static void removeUserPermissions(Neo4JProxy neo4JProxy, String nodeURL, Set<NodePermissionUserPermissionPair>
      toRemoveUserPermissions, boolean nodeIsFolder) {
    for (NodePermissionUserPermissionPair pair : toRemoveUserPermissions) {
      neo4JProxy.removePermissionFromUser(nodeURL, pair.getUser().getId(), pair.getPermission(), nodeIsFolder);
    }
  }

  static void updateNodeOwner(Neo4JProxy neo4JProxy, String nodeURL, String ownerURL, boolean nodeIsFolder) {
    neo4JProxy.updateNodeOwner(nodeURL, ownerURL, nodeIsFolder);
  }

}