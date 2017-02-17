package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Neo4JProxyGroup extends AbstractNeo4JProxy {

  Neo4JProxyGroup(Neo4JProxies proxies) {
    super(proxies);
  }

  FolderServerGroup createGroup(String groupURL, String name, String displayName, String description, String
      ownerURL, Map<String, Object> extraProperties) {
    String cypher = CypherQueryBuilder.createGroup(extraProperties);
    Map<String, Object> params = CypherParamBuilder.createGroup(groupURL, name, displayName, description, ownerURL,
        extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  List<FolderServerGroup> findGroups() {
    String cypher = CypherQueryBuilder.findGroups();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listGroups(jsonNode);
  }

  FolderServerGroup findGroupById(String groupURL) {
    String cypher = CypherQueryBuilder.getGroupById();
    Map<String, Object> params = CypherParamBuilder.getGroupById(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  FolderServerGroup findGroupByName(String groupName) {
    String cypher = CypherQueryBuilder.getGroupByName();
    Map<String, Object> params = CypherParamBuilder.getGroupByName(groupName);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  FolderServerGroup updateGroupById(String groupId, Map<String, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilder.updateGroupById(updateFields);
    Map<String, Object> params = CypherParamBuilder.updateGroupById(groupId, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode updatedNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(updatedNode);
  }

  boolean deleteGroupById(String groupURL) {
    String cypher = CypherQueryBuilder.deleteGroupById();
    Map<String, Object> params = CypherParamBuilder.deleteGroupById(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while deleting group:", error);
    }
    return errorsNode.size() == 0;
  }

  List<FolderServerUser> findGroupMembers(String groupURL) {
    String cypher = CypherQueryBuilder.getGroupUsersWithRelation(RelationLabel.MEMBEROF);
    Map<String, Object> params = CypherParamBuilder.matchGroupId(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listUsers(jsonNode);
  }

  List<FolderServerUser> findGroupAdministrators(String groupURL) {
    String cypher = CypherQueryBuilder.getGroupUsersWithRelation(RelationLabel.ADMINISTERS);
    Map<String, Object> params = CypherParamBuilder.matchGroupId(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listUsers(jsonNode);
  }

  void addUserGroupRelation(String userURL, String groupURL, RelationLabel relation) {
    FolderServerGroup group = findGroupById(groupURL);
    if (group != null) {
      FolderServerUser user = proxies.user().findUserById(userURL);
      if (user != null) {
        addRelation(user, group, relation);
      }
    }
  }

  void removeUserGroupRelation(String userURL, String groupURL, RelationLabel relation) {
    FolderServerGroup group = findGroupById(groupURL);
    if (group != null) {
      FolderServerUser user = proxies.user().findUserById(userURL);
      if (user != null) {
        removeRelation(user, group, relation);
      }
    }
  }

  FolderServerGroup findGroupBySpecialValue(String specialGroupName) {
    String cypher = CypherQueryBuilder.getGroupBySpecialValue();
    Map<String, Object> params = CypherParamBuilder.getGroupBySpecialValue(specialGroupName);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }


  boolean addRelation(FolderServerUser user, FolderServerGroup group, RelationLabel relation) {
    String cypher = CypherQueryBuilder.addRelation(NodeLabel.USER, NodeLabel.GROUP, relation);
    Map<String, Object> params = CypherParamBuilder.matchFromNodeToNode(user.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while adding relation:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean removeRelation(FolderServerUser user, FolderServerGroup group, RelationLabel relation) {
    String cypher = CypherQueryBuilder.removeRelation(NodeLabel.USER, NodeLabel.GROUP, relation);
    Map<String, Object> params = CypherParamBuilder.matchFromNodeToNode(user.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while removing relation:", error);
    }
    return errorsNode.size() == 0;
  }

}
