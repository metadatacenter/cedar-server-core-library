package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.*;
import org.metadatacenter.server.neo4j.cypher.query.AbstractCypherQueryBuilder;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderGroup;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderGroup;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.List;
import java.util.Map;

public class Neo4JProxyGroup extends AbstractNeo4JProxy {

  Neo4JProxyGroup(Neo4JProxies proxies) {
    super(proxies);
  }

  FolderServerGroup createGroup(String groupURL, String name, String displayName, String description, String
      ownerURL, Map<NodeProperty, Object> extraProperties) {
    String cypher = CypherQueryBuilderGroup.createGroup(extraProperties);
    CypherParameters params = CypherParamBuilderGroup.createGroup(groupURL, name, displayName, description, ownerURL,
        extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  List<FolderServerGroup> findGroups() {
    String cypher = CypherQueryBuilderGroup.findGroups();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listGroups(jsonNode);
  }

  FolderServerGroup findGroupById(String groupURL) {
    String cypher = CypherQueryBuilderGroup.getGroupById();
    CypherParameters params = CypherParamBuilderGroup.getGroupById(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  FolderServerGroup findGroupByName(String groupName) {
    String cypher = CypherQueryBuilderGroup.getGroupByName();
    CypherParameters params = CypherParamBuilderGroup.getGroupByName(groupName);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  FolderServerGroup updateGroupById(String groupId, Map<NodeProperty, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilderGroup.updateGroupById(updateFields);
    CypherParameters params = CypherParamBuilderGroup.updateGroupById(groupId, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode updatedNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(updatedNode);
  }

  boolean deleteGroupById(String groupURL) {
    String cypher = CypherQueryBuilderGroup.deleteGroupById();
    CypherParameters params = CypherParamBuilderGroup.deleteGroupById(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while deleting group:");
  }

  List<FolderServerUser> findGroupMembers(String groupURL) {
    String cypher = CypherQueryBuilderGroup.getGroupUsersWithRelation(RelationLabel.MEMBEROF);
    CypherParameters params = CypherParamBuilderGroup.matchGroupId(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listUsers(jsonNode);
  }

  List<FolderServerUser> findGroupAdministrators(String groupURL) {
    String cypher = CypherQueryBuilderGroup.getGroupUsersWithRelation(RelationLabel.ADMINISTERS);
    CypherParameters params = CypherParamBuilderGroup.matchGroupId(groupURL);
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
    String cypher = CypherQueryBuilderGroup.getGroupBySpecialValue();
    CypherParameters params = CypherParamBuilderGroup.getGroupBySpecialValue(specialGroupName);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  boolean addRelation(FolderServerUser user, FolderServerGroup group, RelationLabel relation) {
    String cypher = AbstractCypherQueryBuilder.addRelation(NodeLabel.USER, NodeLabel.GROUP, relation);
    CypherParameters params = AbstractCypherParamBuilder.matchFromNodeToNode(user.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while adding relation:");
  }

  boolean removeRelation(FolderServerUser user, FolderServerGroup group, RelationLabel relation) {
    String cypher = AbstractCypherQueryBuilder.removeRelation(NodeLabel.USER, NodeLabel.GROUP, relation);
    CypherParameters params = AbstractCypherParamBuilder.matchFromNodeToNode(user.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while removing relation:");
  }

}
