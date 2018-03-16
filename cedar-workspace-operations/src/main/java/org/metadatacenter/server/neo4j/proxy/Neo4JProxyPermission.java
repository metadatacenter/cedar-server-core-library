package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderPermission;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.security.model.auth.NodePermission;

import java.util.List;

public class Neo4JProxyPermission extends AbstractNeo4JProxy {

  Neo4JProxyPermission(Neo4JProxies proxies) {
    super(proxies);
  }

  boolean addPermission(FolderServerFolder folder, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.addPermissionToFolderForGroup(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchFolderAndGroup(folder.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while adding permission:");
  }

  boolean addPermission(FolderServerFolder folder, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.addPermissionToFolderForUser(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchFolderAndUser(folder.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while adding permission:");
  }

  boolean addPermission(FolderServerResource resource, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.addPermissionToResourceForGroup(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchResourceAndGroup(resource.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while adding permission:");
  }

  boolean addPermission(FolderServerResource resource, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.addPermissionToResourceForUser(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchResourceAndUser(resource.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while adding permission:");
  }

  boolean removePermission(FolderServerFolder folder, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.removePermissionForFolderFromUser(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchFolderAndUser(folder.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while removing permission:");
  }

  boolean removePermission(FolderServerFolder folder, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.removePermissionForFolderFromGroup(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchFolderAndGroup(folder.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while removing permission:");
  }

  boolean removePermission(FolderServerResource resource, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.removePermissionForResourceFromUser(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchResourceAndUser(resource.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while removing permission:");
  }

  boolean removePermission(FolderServerResource resource, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilderPermission.removePermissionForResourceFromGroup(permission);
    CypherParameters params = AbstractCypherParamBuilder.matchResourceAndGroup(resource.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while removing permission:");
  }

  void addPermissionToUser(String nodeURL, String userURL, NodePermission permission, FolderOrResource
      folderOrResource) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      if (folderOrResource == FolderOrResource.FOLDER) {
        FolderServerFolder folder = proxies.folder().findFolderById(nodeURL);
        if (folder != null) {
          addPermission(folder, user, permission);
        }
      } else {
        FolderServerResource resource = proxies.resource().findResourceById(nodeURL);
        if (resource != null) {
          addPermission(resource, user, permission);
        }
      }
    }
  }

  void removePermissionFromUser(String nodeURL, String userURL, NodePermission permission, FolderOrResource
      folderOrResource) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      if (folderOrResource == FolderOrResource.FOLDER) {
        FolderServerFolder folder = proxies.folder().findFolderById(nodeURL);
        if (folder != null) {
          removePermission(folder, user, permission);
        }
      } else {
        FolderServerResource resource = proxies.resource().findResourceById(nodeURL);
        if (resource != null) {
          removePermission(resource, user, permission);
        }
      }
    }
  }

  void addPermissionToGroup(String nodeURL, String groupURL, NodePermission permission, FolderOrResource
      folderOrResource) {
    FolderServerGroup group = proxies.group().findGroupById(groupURL);
    if (group != null) {
      if (folderOrResource == FolderOrResource.FOLDER) {
        FolderServerFolder folder = proxies.folder().findFolderById(nodeURL);
        if (folder != null) {
          proxies.permission().addPermission(folder, group, permission);
        }
      } else {
        FolderServerResource resource = proxies.resource().findResourceById(nodeURL);
        if (resource != null) {
          proxies.permission().addPermission(resource, group, permission);
        }
      }
    }
  }

  void removePermissionFromGroup(String nodeURL, String groupURL, NodePermission permission, FolderOrResource
      folderOrResource) {
    FolderServerGroup group = proxies.group().findGroupById(groupURL);
    if (group != null) {
      if (folderOrResource == FolderOrResource.FOLDER) {
        FolderServerFolder folder = proxies.folder().findFolderById(nodeURL);
        if (folder != null) {
          proxies.permission().removePermission(folder, group, permission);
        }
      } else {
        FolderServerResource resource = proxies.resource().findResourceById(nodeURL);
        if (resource != null) {
          proxies.permission().removePermission(resource, group, permission);
        }
      }
    }
  }

  boolean userHasReadAccessToFolder(String userURL, String folderURL) {
    String cypher = CypherQueryBuilderPermission.userCanReadNode(FolderOrResource.FOLDER);
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndNodeId(userURL, folderURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  boolean userHasWriteAccessToFolder(String userURL, String folderURL) {
    String cypher = CypherQueryBuilderPermission.userCanWriteNode(FolderOrResource.FOLDER);
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndNodeId(userURL, folderURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  boolean userHasReadAccessToResource(String userURL, String resourceURL) {
    String cypher = CypherQueryBuilderPermission.userCanReadNode(FolderOrResource.RESOURCE);
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndNodeId(userURL, resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  boolean userHasWriteAccessToResource(String userURL, String resourceURL) {
    String cypher = CypherQueryBuilderPermission.userCanWriteNode(FolderOrResource.RESOURCE);
    CypherParameters params = AbstractCypherParamBuilder.matchUserIdAndNodeId(userURL, resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerUser cedarFSUser = buildUser(userNode);
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
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listUsers(jsonNode);
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
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listGroups(jsonNode);
  }

  List<FolderServerUser> getUsersWithTransitivePermissionOnNode(String nodeURL, NodePermission permission,
                                                                FolderOrResource folderOrResource) {
    String cypher = null;
    switch (permission) {
      case READ:
        cypher = CypherQueryBuilderPermission.getUsersWithTransitiveReadOnNode(folderOrResource);
        break;
      case WRITE:
        cypher = CypherQueryBuilderPermission.getUsersWithTransitiveWriteOnNode(folderOrResource);
        break;
    }

    CypherParameters params = CypherParamBuilderNode.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listUsers(jsonNode);
  }

  List<FolderServerGroup> getGroupsWithTransitivePermissionOnNode(String nodeURL, NodePermission permission,
                                                                  FolderOrResource folderOrResource) {
    String cypher = null;
    switch (permission) {
      case READ:
        cypher = CypherQueryBuilderPermission.getGroupsWithTransitiveReadOnNode(folderOrResource);
        break;
      case WRITE:
        cypher = CypherQueryBuilderPermission.getGroupsWithTransitiveWriteOnNode(folderOrResource);
        break;
    }

    CypherParameters params = CypherParamBuilderNode.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listGroups(jsonNode);
  }

}
