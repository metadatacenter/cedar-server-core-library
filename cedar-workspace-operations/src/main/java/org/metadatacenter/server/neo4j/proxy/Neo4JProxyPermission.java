package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderPermission;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.security.model.auth.NodePermission;

import java.util.List;

public class Neo4JProxyPermission extends AbstractNeo4JProxy {

  Neo4JProxyPermission(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
  }

  boolean addPermission(FileSystemResource node, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.addPermissionToNodeForGroup(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchNodeAndGroup(node.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "adding permission");
  }

  boolean addPermission(FileSystemResource node, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.addPermissionToNodeForUser(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchNodeAndUser(node.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "adding permission");
  }

  boolean removePermission(FileSystemResource node, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.removePermissionForNodeFromUser(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchNodeAndUser(node.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing permission");
  }

  boolean removePermission(FileSystemResource node, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.removePermissionForNodeFromGroup(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchNodeAndGroup(node.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing permission");
  }

  void addPermissionToUser(String nodeURL, String userURL, NodePermission permission) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      FileSystemResource node = proxies.resource().findNodeById(nodeURL);
      if (node != null) {
        addPermission(node, user, permission);
      }
    }
  }

  void removePermissionFromUser(String nodeURL, String userURL, NodePermission permission) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      FileSystemResource node = proxies.resource().findNodeById(nodeURL);
      if (node != null) {
        removePermission(node, user, permission);
      }
    }
  }

  void addPermissionToGroup(String nodeURL, String groupURL, NodePermission permission) {
    FolderServerGroup group = proxies.group().findGroupById(groupURL);
    if (group != null) {
      FileSystemResource node = proxies.resource().findNodeById(nodeURL);
      if (node != null) {
        proxies.permission().addPermission(node, group, permission);
      }
    }
  }

  void removePermissionFromGroup(String nodeURL, String groupURL, NodePermission permission) {
    FolderServerGroup group = proxies.group().findGroupById(groupURL);
    if (group != null) {
      FileSystemResource node = proxies.resource().findNodeById(nodeURL);
      if (node != null) {
        proxies.permission().removePermission(node, group, permission);
      }
    }
  }

  boolean userHasReadAccessToNode(String userURL, String nodeURL) {
    String cypher = CypherQueryBuilderPermission.userCanReadNode();
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndNodeId(userURL, nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    FolderServerUser cedarFSUser = executeReadGetOne(q, FolderServerUser.class);
    return cedarFSUser != null;
  }

  boolean userHasWriteAccessToNode(String userURL, String nodeURL) {
    String cypher = CypherQueryBuilderPermission.userCanWriteNode();
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndNodeId(userURL, nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    FolderServerUser cedarFSUser = executeReadGetOne(q, FolderServerUser.class);
    return cedarFSUser != null;
  }

  List<FolderServerUser> getUsersWithDirectPermissionOnNode(String nodeURL, NodePermission permission) {
    RelationLabel relationLabel = null;
    switch (permission) {
      case READ:
        relationLabel = RelationLabel.CANREAD;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilderPermission.getUsersWithDirectPermissionOnNode(relationLabel);
    CypherParameters params = CypherParamBuilderNode.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerUser.class);
  }

  List<FolderServerGroup> getGroupsWithDirectPermissionOnNode(String nodeURL, NodePermission permission) {
    RelationLabel relationLabel = null;
    switch (permission) {
      case READ:
        relationLabel = RelationLabel.CANREAD;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilderPermission.getGroupsWithDirectPermissionOnNode(relationLabel);
    CypherParameters params = CypherParamBuilderNode.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerGroup.class);
  }

  List<FolderServerUser> getUsersWithTransitivePermissionOnNode(String nodeURL, NodePermission permission) {
    String cypher = null;
    switch (permission) {
      case READ:
        cypher = CypherQueryBuilderPermission.getUsersWithTransitiveReadOnNode();
        break;
      case WRITE:
        cypher = CypherQueryBuilderPermission.getUsersWithTransitiveWriteOnNode();
        break;
    }

    CypherParameters params = CypherParamBuilderNode.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerUser.class);
  }

  List<FolderServerGroup> getGroupsWithTransitivePermissionOnNode(String nodeURL, NodePermission permission) {
    String cypher = null;
    switch (permission) {
      case READ:
        cypher = CypherQueryBuilderPermission.getGroupsWithTransitiveReadOnNode();
        break;
      case WRITE:
        cypher = CypherQueryBuilderPermission.getGroupsWithTransitiveWriteOnNode();
        break;
    }

    CypherParameters params = CypherParamBuilderNode.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerGroup.class);
  }

}
