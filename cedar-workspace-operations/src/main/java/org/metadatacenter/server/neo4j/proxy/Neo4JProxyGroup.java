package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderGroup;
import org.metadatacenter.server.neo4j.cypher.query.AbstractCypherQueryBuilder;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderGroup;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

import java.util.List;
import java.util.Map;

public class Neo4JProxyGroup extends AbstractNeo4JProxy {

  Neo4JProxyGroup(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
  }

  FolderServerGroup createGroup(String groupURL, String name, String displayName, String description, String
      ownerURL, String specialGroup) {
    String cypher = CypherQueryBuilderGroup.createGroupWithAdministrator();
    CypherParameters params = CypherParamBuilderGroup.createGroup(groupURL, name, displayName, description, ownerURL,
        specialGroup);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerGroup.class);
  }

  List<FolderServerGroup> findGroups() {
    String cypher = CypherQueryBuilderGroup.findGroups();
    CypherQuery q = new CypherQueryLiteral(cypher);
    return executeReadGetList(q, FolderServerGroup.class);
  }

  FolderServerGroup findGroupById(String groupURL) {
    String cypher = CypherQueryBuilderGroup.getGroupById();
    CypherParameters params = CypherParamBuilderGroup.getGroupById(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerGroup.class);
  }

  FolderServerGroup findGroupByName(String groupName) {
    String cypher = CypherQueryBuilderGroup.getGroupByName();
    CypherParameters params = CypherParamBuilderGroup.getGroupByName(groupName);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerGroup.class);
  }

  FolderServerGroup updateGroupById(String groupId, Map<NodeProperty, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilderGroup.updateGroupById(updateFields);
    CypherParameters params = CypherParamBuilderGroup.updateGroupById(groupId, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerGroup.class);
  }

  boolean deleteGroupById(String groupURL) {
    String cypher = CypherQueryBuilderGroup.deleteGroupById();
    CypherParameters params = CypherParamBuilderGroup.deleteGroupById(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "deleting group");
  }

  List<FolderServerUser> findGroupMembers(String groupURL) {
    String cypher = CypherQueryBuilderGroup.getGroupUsersWithRelation(RelationLabel.MEMBEROF);
    CypherParameters params = CypherParamBuilderGroup.matchGroupId(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerUser.class);
  }

  List<FolderServerUser> findGroupAdministrators(String groupURL) {
    String cypher = CypherQueryBuilderGroup.getGroupUsersWithRelation(RelationLabel.ADMINISTERS);
    CypherParameters params = CypherParamBuilderGroup.matchGroupId(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerUser.class);
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
    return executeReadGetOne(q, FolderServerGroup.class);
  }

  boolean addRelation(FolderServerUser user, FolderServerGroup group, RelationLabel relation) {
    String cypher = AbstractCypherQueryBuilder.addRelation(NodeLabel.USER, NodeLabel.GROUP, relation);
    CypherParameters params = AbstractCypherParamBuilder.matchFromNodeToNode(user.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "adding relation");
  }

  boolean removeRelation(FolderServerUser user, FolderServerGroup group, RelationLabel relation) {
    String cypher = AbstractCypherQueryBuilder.removeRelation(NodeLabel.USER, NodeLabel.GROUP, relation);
    CypherParameters params = AbstractCypherParamBuilder.matchFromNodeToNode(user.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing relation");
  }

}
