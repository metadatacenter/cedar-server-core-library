package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.neo4j.*;
import org.metadatacenter.server.security.model.auth.NodePermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neo4JProxyPermission extends AbstractNeo4JProxy {

  Neo4JProxyPermission(Neo4JProxies proxies) {
    super(proxies);
  }

  boolean addPermission(FolderServerFolder folder, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilder.addPermissionToFolderForGroup(permission);
    Map<String, Object> params = CypherParamBuilder.matchFolderAndGroup(folder.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while adding permission:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean addPermission(FolderServerFolder folder, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilder.addPermissionToFolderForUser(permission);
    Map<String, Object> params = CypherParamBuilder.matchFolderAndUser(folder.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while adding permission:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean addPermission(FolderServerResource resource, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilder.addPermissionToResourceForGroup(permission);
    Map<String, Object> params = CypherParamBuilder.matchResourceAndGroup(resource.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while adding permission:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean addPermission(FolderServerResource resource, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilder.addPermissionToResourceForUser(permission);
    Map<String, Object> params = CypherParamBuilder.matchResourceAndUser(resource.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while adding permission:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean removePermission(FolderServerFolder folder, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilder.removePermissionForFolderFromUser(permission);
    Map<String, Object> params = CypherParamBuilder.matchFolderAndUser(folder.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while removing permission:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean removePermission(FolderServerFolder folder, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilder.removePermissionForFolderFromGroup(permission);
    Map<String, Object> params = CypherParamBuilder.matchFolderAndGroup(folder.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while removing permission:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean removePermission(FolderServerResource resource, FolderServerUser user, NodePermission permission) {
    String cypher = CypherQueryBuilder.removePermissionForResourceFromUser(permission);
    Map<String, Object> params = CypherParamBuilder.matchResourceAndUser(resource.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while removing permission:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean removePermission(FolderServerResource resource, FolderServerGroup group, NodePermission permission) {
    String cypher = CypherQueryBuilder.removePermissionForResourceFromGroup(permission);
    Map<String, Object> params = CypherParamBuilder.matchResourceAndGroup(resource.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while removing permission:", error);
    }
    return errorsNode.size() == 0;
  }

  void addPermissionToUser(String nodeURL, String userURL, NodePermission permission, boolean nodeIsFolder) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      if (nodeIsFolder) {
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

  void removePermissionFromUser(String nodeURL, String userURL, NodePermission permission, boolean
      nodeIsFolder) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      if (nodeIsFolder) {
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

  void addPermissionToGroup(String nodeURL, String groupURL, NodePermission permission, boolean nodeIsFolder) {
    FolderServerGroup group = proxies.group().findGroupById(groupURL);
    if (group != null) {
      if (nodeIsFolder) {
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

  void removePermissionFromGroup(String nodeURL, String groupURL, NodePermission permission, boolean
      nodeIsFolder) {
    FolderServerGroup group = proxies.group().findGroupById(groupURL);
    if (group != null) {
      if (nodeIsFolder) {
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
    String cypher = CypherQueryBuilder.userCanReadNode(CypherQueryBuilder.FolderOrResource.FOLDER);
    Map<String, Object> params = CypherParamBuilder.matchUserIdAndNodeId(userURL, folderURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  boolean userHasWriteAccessToFolder(String userURL, String folderURL) {
    String cypher = CypherQueryBuilder.userCanWriteNode(CypherQueryBuilder.FolderOrResource.FOLDER);
    Map<String, Object> params = CypherParamBuilder.matchUserIdAndNodeId(userURL, folderURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  boolean userHasReadAccessToResource(String userURL, String resourceURL) {
    String cypher = CypherQueryBuilder.userCanReadNode(CypherQueryBuilder.FolderOrResource.RESOURCE);
    Map<String, Object> params = CypherParamBuilder.matchUserIdAndNodeId(userURL, resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  boolean userHasWriteAccessToResource(String userURL, String resourceURL) {
    String cypher = CypherQueryBuilder.userCanWriteNode(CypherQueryBuilder.FolderOrResource.RESOURCE);
    Map<String, Object> params = CypherParamBuilder.matchUserIdAndNodeId(userURL, resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  List<FolderServerUser> getUsersWithDirectPermissionOnNode(String nodeURL, NodePermission permission) {
    List<FolderServerUser> userList = new ArrayList<>();
    RelationLabel relationLabel = null;
    switch (permission) {
      case READ:
        relationLabel = RelationLabel.CANREAD;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilder.getUsersWithDirectPermissionOnNode(relationLabel);
    Map<String, Object> params = CypherParamBuilder.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode userNode = f.at("/row/0");
        if (userNode != null && !userNode.isMissingNode()) {
          FolderServerUser cu = buildUser(userNode);
          userList.add(cu);
        }
      });
    }
    return userList;
  }

  List<FolderServerGroup> getGroupsWithDirectPermissionOnNode(String nodeURL, NodePermission permission) {
    List<FolderServerGroup> groupList = new ArrayList<>();
    RelationLabel relationLabel = null;
    switch (permission) {
      case READ:
        relationLabel = RelationLabel.CANREAD;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilder.getGroupsWithDirectPermissionOnNode(relationLabel);
    Map<String, Object> params = CypherParamBuilder.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupListJsonNode = jsonNode.at("/results/0/data");
    if (groupListJsonNode != null && !groupListJsonNode.isMissingNode()) {
      groupListJsonNode.forEach(f -> {
        JsonNode groupNode = f.at("/row/0");
        if (groupNode != null && !groupNode.isMissingNode()) {
          FolderServerGroup g = buildGroup(groupNode);
          groupList.add(g);
        }
      });
    }
    return groupList;
  }

  List<FolderServerUser> getUsersWithTransitivePermissionOnNode(String nodeURL, NodePermission permission,
                                                                CypherQueryBuilder.FolderOrResource folderOrResource) {
    List<FolderServerUser> userList = new ArrayList<>();
    RelationLabel relationLabel = null;
    switch (permission) {
      case READ:
        relationLabel = RelationLabel.CANREAD;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilder.getUsersWithTransitivePermissionOnNode(relationLabel, folderOrResource);
    Map<String, Object> params = CypherParamBuilder.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode userNode = f.at("/row/0");
        if (userNode != null && !userNode.isMissingNode()) {
          FolderServerUser cu = buildUser(userNode);
          userList.add(cu);
        }
      });
    }
    return userList;
  }

  List<FolderServerGroup> getGroupsWithTransitivePermissionOnNode(String nodeURL, NodePermission permission,
                                                                  CypherQueryBuilder.FolderOrResource
                                                                      folderOrResource) {
    List<FolderServerGroup> groupList = new ArrayList<>();
    RelationLabel relationLabel = null;
    switch (permission) {
      case READ:
        relationLabel = RelationLabel.CANREAD;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilder.getGroupsWithTransitivePermissionOnNode(relationLabel);
    Map<String, Object> params = CypherParamBuilder.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupListJsonNode = jsonNode.at("/results/0/data");
    if (groupListJsonNode != null && !groupListJsonNode.isMissingNode()) {
      groupListJsonNode.forEach(f -> {
        JsonNode groupNode = f.at("/row/0");
        if (groupNode != null && !groupNode.isMissingNode()) {
          FolderServerGroup g = buildGroup(groupNode);
          groupList.add(g);
        }
      });
    }
    return groupList;
  }

  public Map<String, String> findAccessibleNodeIds(String userURL) {
    Map<String, String> map = new HashMap<>();

    String cypherOwned = CypherQueryBuilder.findOwnedNodes();
    Map<String, Object> params = CypherParamBuilder.matchUserId(userURL);
    CypherQuery qOwned = new CypherQueryWithParameters(cypherOwned, params);
    JsonNode jsonNodeOwned = executeCypherQueryAndCommit(qOwned);
    mergeAccessibleNodesIntoMap(map, jsonNodeOwned, RelationLabel.OWNS);

    String cypherWrite = CypherQueryBuilder.findWritableNodes();
    CypherQuery qWrite = new CypherQueryWithParameters(cypherWrite, params);
    JsonNode jsonNodeWritable = executeCypherQueryAndCommit(qWrite);
    mergeAccessibleNodesIntoMap(map, jsonNodeWritable, RelationLabel.CANWRITE);

    String cypherRead = CypherQueryBuilder.findReadableNodes();
    CypherQuery qRead = new CypherQueryWithParameters(cypherRead, params);
    JsonNode jsonNodeReadable = executeCypherQueryAndCommit(qRead);
    mergeAccessibleNodesIntoMap(map, jsonNodeReadable, RelationLabel.CANREAD);

    return map;
  }

  private void mergeAccessibleNodesIntoMap(Map<String, String> map, JsonNode jsonNode, RelationLabel label) {
    JsonNode nodeListJsonNode = jsonNode.at("/results/0/data");
    if (nodeListJsonNode != null && !nodeListJsonNode.isMissingNode()) {
      nodeListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row/0");
        if (nodeNode != null && !nodeNode.isMissingNode()) {
          FolderServerNode node = buildNode(nodeNode);
          if (node != null) {
            String key = node.getId();
            if (!map.containsKey(key)) {
              map.put(key, label.getValue());
            }
          }
        }
      });
    }
  }

}
