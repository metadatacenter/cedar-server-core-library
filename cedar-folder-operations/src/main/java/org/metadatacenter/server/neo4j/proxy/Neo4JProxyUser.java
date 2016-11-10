package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Neo4JProxyUser extends AbstractNeo4JProxy {

  Neo4JProxyUser(Neo4JProxies proxies) {
    super(proxies);
  }

  public FolderServerUser createUser(String userURL, String name, String displayName, String firstName, String
      lastName, String email, FolderServerGroup group) {
    return createUser(userURL, name, displayName, firstName, lastName, email, null, group);
  }

  FolderServerUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email) {
    return createUser(userURL, name, displayName, firstName, lastName, email, null, null);
  }

  FolderServerUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email, Map<String, Object> extraProperties) {
    String cypher = CypherQueryBuilder.createUser();
    Map<String, Object> params = CypherParamBuilder.createUser(userURL, name, displayName, firstName, lastName, email,
        extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return buildUser(userNode);
  }

  FolderServerUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email, Map<String, Object> extraProperties, FolderServerGroup group) {
    FolderServerUser newUser = createUser(userURL, name, displayName, firstName, lastName, email, extraProperties);
    if (group != null) {
      addGroupToUser(newUser, group);
    }
    return newUser;
  }

  boolean addGroupToUser(FolderServerUser user, FolderServerGroup group) {
    String cypher = CypherQueryBuilder.addGroupToUser();
    Map<String, Object> params = CypherParamBuilder.addGroupToUser(user.getId(), group.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while adding group to user:", error);
    }
    return errorsNode.size() == 0;
  }

  public List<FolderServerUser> findUsers() {
    List<FolderServerUser> userList = new ArrayList<>();
    String cypher = CypherQueryBuilder.findUsers();
    CypherQuery q = new CypherQueryLiteral(cypher);
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

  public FolderServerUser findUserById(String userURL) {
    String cypher = CypherQueryBuilder.getUserById();
    Map<String, Object> params = CypherParamBuilder.getUserById(userURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return buildUser(userNode);
  }

}
