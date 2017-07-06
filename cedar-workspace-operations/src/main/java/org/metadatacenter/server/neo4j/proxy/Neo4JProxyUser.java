package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderUser;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderUser;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.List;
import java.util.Map;

public class Neo4JProxyUser extends AbstractNeo4JProxy {

  Neo4JProxyUser(Neo4JProxies proxies) {
    super(proxies);
  }

  FolderServerUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email) {
    return createUser(userURL, name, displayName, firstName, lastName, email, null, null);
  }

  FolderServerUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email, Map<NodeProperty, Object> extraProperties) {
    String cypher = CypherQueryBuilderUser.createUser();
    CypherParameters params = CypherParamBuilderUser.createUser(userURL, name, displayName, firstName, lastName,
        email, extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return buildUser(userNode);
  }

  FolderServerUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email, Map<NodeProperty, Object> extraProperties, FolderServerGroup group) {
    FolderServerUser newUser = createUser(userURL, name, displayName, firstName, lastName, email, extraProperties);
    if (group != null) {
      addGroupToUser(newUser, group);
    }
    return newUser;
  }

  boolean addGroupToUser(FolderServerUser user, FolderServerGroup group) {
    String cypher = CypherQueryBuilderUser.addGroupToUser();
    CypherParameters params = AbstractCypherParamBuilder.matchUserAndGroup(user.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while adding group to user:");
  }

  public List<FolderServerUser> findUsers() {
    String cypher = CypherQueryBuilderUser.findUsers();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return listUsers(jsonNode);
  }

  public FolderServerUser findUserById(String userURL) {
    String cypher = CypherQueryBuilderUser.getUserById();
    CypherParameters params = CypherParamBuilderUser.getUserById(userURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return buildUser(userNode);
  }

}
