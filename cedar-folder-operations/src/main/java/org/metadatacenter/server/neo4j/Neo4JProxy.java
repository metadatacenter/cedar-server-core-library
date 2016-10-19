package org.metadatacenter.server.neo4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.HttpResponse;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.constant.HttpConnectionConstants;
import org.metadatacenter.constant.HttpConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.security.model.auth.NodePermission;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

public class Neo4JProxy {

  private final Neo4jConfig config;
  private final String genericIdPrefix;
  private final String folderIdPrefix;
  private final String userIdPrefix;
  private final String groupIdPrefix;
  private final PathUtil pathUtil;

  private static final Logger log = LoggerFactory.getLogger(Neo4JProxy.class);

  public Neo4JProxy(Neo4jConfig config, String genericIdPrefix, String userIdPrefix) {
    this.config = config;
    this.genericIdPrefix = genericIdPrefix;
    this.userIdPrefix = userIdPrefix;
    this.folderIdPrefix = getNodeTypeFullPrefix(CedarNodeType.FOLDER);
    this.groupIdPrefix = getNodeTypeFullPrefix(CedarNodeType.GROUP);
    this.pathUtil = new Neo4JPathUtil(config);
  }

  PathUtil getPathUtil() {
    return pathUtil;
  }

  Neo4jConfig getConfig() {
    return config;
  }

  public String getUserIdPrefix() {
    return userIdPrefix;
  }

  public String getGroupIdPrefix() {
    return groupIdPrefix;
  }

  private JsonNode executeCypherQueryAndCommit(CypherQuery query) {
    return executeCypherQueriesAndCommit(Collections.singletonList(query));
  }

  private JsonNode executeCypherQueriesAndCommit(List<CypherQuery> queries) {
    System.out.println("\nExecute cypher queries --------------------------:");
    List<Map<String, Object>> statements = new ArrayList<>();
    for (CypherQuery q : queries) {
      if (q instanceof CypherQueryWithParameters) {
        CypherQueryWithParameters qp = (CypherQueryWithParameters) q;
        System.out.println("q: " + qp.getQuery());
        System.out.println("p: " + qp.getParameters());
        System.out.println("i: " + qp.getLiteralCypher());
        Map<String, Object> statement = new HashMap<>();
        statement.put("statement", qp.getQuery());
        statement.put("parameters", qp.getParameters());
        statements.add(statement);
      } else if (q instanceof CypherQueryLiteral) {
        System.out.println("s: " + q.getQuery());
        CypherQueryLiteral qp = (CypherQueryLiteral) q;
        Map<String, Object> statement = new HashMap<>();
        statement.put("statement", qp.getQuery());
        statements.add(statement);
      }
    }
    System.out.println();

    Map<String, Object> body = new HashMap<>();
    body.put("statements", statements);

    String requestBody;
    try {
      requestBody = JsonMapper.MAPPER.writeValueAsString(body);
    } catch (JsonProcessingException e) {
      log.error("Error serializing cypher queries", e);
      return null;
    }

    try {
      HttpResponse response = Request.Post(config.getTransactionUrl())
          .addHeader(HttpConstants.HTTP_HEADER_AUTHORIZATION, config.getAuthString())
          .addHeader(HttpConstants.HTTP_HEADER_ACCEPT, HttpConstants.CONTENT_TYPE_APPLICATION_JSON)
          .addHeader("X-stream", "true")
          .bodyString(requestBody, ContentType.APPLICATION_JSON)
          .connectTimeout(HttpConnectionConstants.CONNECTION_TIMEOUT)
          .socketTimeout(HttpConnectionConstants.SOCKET_TIMEOUT)
          .execute()
          .returnResponse();

      int statusCode = response.getStatusLine().getStatusCode();
      String responseAsString = EntityUtils.toString(response.getEntity());
      if (statusCode == HttpConstants.OK) {
        return JsonMapper.MAPPER.readTree(responseAsString);
      } else {
        return null;
      }

    } catch (IOException ex) {
      log.error("Error while reading user details from Keycloak", ex);
      ex.printStackTrace();
    }
    return null;
  }

  List<CedarFSFolder> findFolderPathByPath(String path) {
    List<CedarFSFolder> pathList = new ArrayList<>();
    int cnt = getPathUtil().getPathDepth(path);
    String cypher = CypherQueryBuilder.getFolderLookupQueryByDepth(cnt);
    Map<String, Object> params = CypherParamBuilder.getFolderLookupByDepthParameters(pathUtil, path);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode pathListJsonNode = jsonNode.at("/results/0/data/0/row");
    if (pathListJsonNode != null && !pathListJsonNode.isMissingNode()) {
      pathListJsonNode.forEach(f -> {
        CedarFSFolder cf = buildFolder(f);
        if (cf != null) {
          pathList.add(cf);
        }
      });
    }
    return pathList;
  }

  List<CedarFSFolder> findFolderPathById(String id) {
    List<CedarFSFolder> pathList = new ArrayList<>();
    String cypher = CypherQueryBuilder.getFolderLookupQueryById();
    Map<String, Object> params = CypherParamBuilder.getFolderLookupByIDParameters(pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode pathListJsonNode = jsonNode.at("/results/0/data/0/row/0");
    if (pathListJsonNode != null && !pathListJsonNode.isMissingNode()) {
      pathListJsonNode.forEach(f -> {
        // relationships are also included, filter them out
        Map pathElement = buildMap(f);
        if (pathElement != null && !pathElement.isEmpty()) {
          CedarFSFolder cf = buildFolder(f);
          if (cf != null) {
            pathList.add(cf);
          }
        }
      });
    }
    return pathList;
  }

  List<CedarFSNode> findNodePathById(String id) {
    List<CedarFSNode> pathList = new ArrayList<>();
    String cypher = CypherQueryBuilder.getNodeLookupQueryById();
    Map<String, Object> params = CypherParamBuilder.getNodeLookupByIDParameters(pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode pathListJsonNode = jsonNode.at("/results/0/data/0/row/0");
    if (pathListJsonNode != null && !pathListJsonNode.isMissingNode()) {
      pathListJsonNode.forEach(f -> {
        // relationships are also included, filter them out
        Map pathElement = buildMap(f);
        if (pathElement != null && !pathElement.isEmpty()) {
          CedarFSNode cf = buildNode(f);
          if (cf != null) {
            pathList.add(cf);
          }
        }
      });
    }
    return pathList;
  }

  CedarFSFolder findFolderByPath(String path) {
    List<CedarFSFolder> folderPath = findFolderPathByPath(path);
    if (folderPath != null && folderPath.size() > 0) {
      return folderPath.get(folderPath.size() - 1);
    }
    return null;
  }

  CedarFSFolder findFolderByParentIdAndName(String parentId, String name) {
    String cypher = CypherQueryBuilder.getFolderByParentIdAndName();
    Map<String, Object> params = CypherParamBuilder.getFolderByParentIdAndName(parentId, name);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode folderNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(folderNode);
  }

  CedarFSNode findNodeByParentIdAndName(String parentId, String name) {
    String cypher = CypherQueryBuilder.getNodeByParentIdAndName();
    Map<String, Object> params = CypherParamBuilder.getNodeByParentIdAndName(parentId, name);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode node = jsonNode.at("/results/0/data/0/row/0");
    return buildNode(node);
  }

  CedarFSFolder findFolderById(String folderUUID) {
    String cypher = CypherQueryBuilder.getFolderById();
    Map<String, Object> params = CypherParamBuilder.getFolderById(folderUUID);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode folderNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(folderNode);
  }

  CedarFSResource findResourceById(String resourceURL) {
    String cypher = CypherQueryBuilder.getResourceById();
    Map<String, Object> params = CypherParamBuilder.getResourceById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode resourceNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(resourceNode);
  }

  List<CedarFSNode> findAllNodes(int limit, int offset, List<String> sortList) {
    List<CedarFSNode> resources = new ArrayList<>();
    String cypher = CypherQueryBuilder.getAllNodesLookupQuery(sortList);
    Map<String, Object> params = CypherParamBuilder.getAllNodesLookupParameters(limit, offset);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);

    JsonNode resourceListJsonNode = jsonNode.at("/results/0/data");
    if (resourceListJsonNode != null && !resourceListJsonNode.isMissingNode()) {
      resourceListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row/0");
        CedarFSNode cf = buildNode(nodeNode);
        if (cf != null) {
          resources.add(cf);
        }
      });
    }
    return resources;
  }

  public long findAllNodesCount() {
    String cypher = CypherQueryBuilder.getAllNodesCountQuery();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode countNode = jsonNode.at("/results/0/data/0/row/0");
    if (countNode != null && !countNode.isMissingNode()) {
      return countNode.asLong();
    } else {
      return -1;
    }
  }

  CedarFSFolder createRootFolder(String creatorId) {
    Map<String, Object> extraParams = new HashMap<>();
    extraParams.put(Neo4JFields.IS_ROOT, true);
    extraParams.put(Neo4JFields.IS_SYSTEM, true);
    String cypher = CypherQueryBuilder.createRootFolder(extraParams);
    Map<String, Object> params = CypherParamBuilder.createFolder(folderIdPrefix, null, config.getRootFolderPath(),
        config.getRootFolderPath(), config.getRootFolderDescription(), creatorId, extraParams);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode rootNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(rootNode);
  }

  CedarFSFolder createFolderAsChildOfId(String parentId, String name, String displayName, String description, String
      creatorId, NodeLabel label, Map<String, Object> extraProperties) {
    String cypher = CypherQueryBuilder.createFolderAsChildOfId(label, extraProperties);
    Map<String, Object> params = CypherParamBuilder.createFolder(folderIdPrefix, parentId, name, displayName,
        description, creatorId, extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode newNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(newNode);
  }

  CedarFSResource createResourceAsChildOfId(String parentId, String childURL, CedarNodeType nodeType, String
      name, String description, String creatorId, NodeLabel label, Map<String, Object> extraProperties) {
    String cypher = CypherQueryBuilder.createResourceAsChildOfId(label, extraProperties);
    Map<String, Object> params = CypherParamBuilder.createResource(parentId, childURL, nodeType, name, description,
        creatorId, extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode newNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(newNode);
  }

  List<CedarFSNode> findFolderContents(String folderId, Collection<CedarNodeType> nodeTypes, int
      limit, int offset, List<String> sortList, CedarUser cu) {
    List<CedarFSNode> resources = new ArrayList<>();

    boolean addPermissionConditions = true;
    String cypher = CypherQueryBuilder.getFolderContentsLookupQuery(sortList, addPermissionConditions);
    String ownerId = userIdPrefix + cu.getId();
    Map<String, Object> params = CypherParamBuilder.getFolderContentsLookupParameters(folderId, nodeTypes, limit,
        offset, ownerId, addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode resourceListJsonNode = jsonNode.at("/results/0/data");
    if (resourceListJsonNode != null && !resourceListJsonNode.isMissingNode()) {
      resourceListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row/0");
        CedarFSNode cf = buildNode(nodeNode);
        if (cf != null) {
          resources.add(cf);
        }
      });
    }
    return resources;
  }

  public long findFolderContentsFilteredCount(String folderId, List<CedarNodeType> nodeTypeList) {
    String cypher = CypherQueryBuilder.getFolderContentsFilteredCountQuery();
    Map<String, Object> params = CypherParamBuilder.getFolderContentsFilteredCountParameters(folderId,
        nodeTypeList);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode countNode = jsonNode.at("/results/0/data/0/row/0");
    if (countNode != null && !countNode.isMissingNode()) {
      return countNode.asLong();
    } else {
      return -1;
    }
  }

  public long findFolderContentsCount(String folderId) {
    String cypher = CypherQueryBuilder.getFolderContentsCountQuery();
    Map<String, Object> params = CypherParamBuilder.getFolderContentsCountParameters(folderId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode countNode = jsonNode.at("/results/0/data/0/row/0");
    if (countNode != null && !countNode.isMissingNode()) {
      return countNode.asLong();
    } else {
      return -1;
    }
  }

  boolean deleteFolderById(String folderId) {
    String cypher = CypherQueryBuilder.deleteFolderContentsRecursivelyById();
    Map<String, Object> params = CypherParamBuilder.deleteFolderById(folderId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while deleting folder:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean deleteResourceById(String resourceURL) {
    String cypher = CypherQueryBuilder.deleteResourceById();
    Map<String, Object> params = CypherParamBuilder.deleteResourceById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while deleting resource:", error);
    }
    return errorsNode.size() == 0;
  }

  CedarFSFolder updateFolderById(String folderId, Map<String, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilder.updateFolderById(updateFields);
    Map<String, Object> params = CypherParamBuilder.updateFolderById(folderId, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode updatedNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(updatedNode);
  }

  CedarFSResource updateResourceById(String resourceURL, Map<String, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilder.updateResourceById(updateFields);
    Map<String, Object> params = CypherParamBuilder.updateResourceById(resourceURL, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode updatedNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(updatedNode);
  }

  String getFolderUUID(String folderId) {
    if (folderId != null && folderId.startsWith(folderIdPrefix)) {
      return folderId.substring(folderIdPrefix.length());
    } else {
      return null;
    }
  }

  //TODO: fix this, we need to handle the prefixes based on the type
  //TODO: REALLY IMPORTANT
  String getResourceUUID(String resourceId, CedarNodeType nodeType) {
    String prefix = getNodeTypeFullPrefix(nodeType);
    if (resourceId != null && resourceId.startsWith(prefix)) {
      return resourceId.substring(prefix.length());
    } else {
      return null;
    }
  }

  private String getNodeTypeFullPrefix(CedarNodeType nodeType) {
    return genericIdPrefix + nodeType.getPrefix() + "/";
  }

  private CedarFSFolder buildFolder(JsonNode f) {
    CedarFSFolder cf = null;
    if (f != null && !f.isMissingNode()) {
      try {
        cf = JsonMapper.MAPPER.treeToValue(f, CedarFSFolder.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing folder", e);
      }
    }
    return cf;
  }

  private CedarFSNode buildNode(JsonNode f) {
    CedarFSNode cf = null;
    if (f != null && !f.isMissingNode()) {
      try {
        cf = JsonMapper.MAPPER.treeToValue(f, CedarFSNode.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing node", e);
      }
    }
    return cf;
  }

  private CedarFSResource buildResource(JsonNode r) {
    CedarFSResource cr = null;
    if (r != null && !r.isMissingNode()) {
      try {
        cr = JsonMapper.MAPPER.treeToValue(r, CedarFSResource.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing resource", e);
      }
    }
    return cr;
  }

  private Map buildMap(JsonNode f) {
    Map map = null;
    if (f != null && !f.isMissingNode()) {
      try {
        map = JsonMapper.MAPPER.treeToValue(f, Map.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing map", e);
      }
    }
    return map;
  }

  private CedarFSUser buildUser(JsonNode u) {
    CedarFSUser cu = null;
    if (u != null && !u.isMissingNode()) {
      try {
        cu = JsonMapper.MAPPER.treeToValue(u, CedarFSUser.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing user", e);
      }
    }
    return cu;
  }

  private CedarFSGroup buildGroup(JsonNode g) {
    CedarFSGroup cg = null;
    if (g != null && !g.isMissingNode()) {
      try {
        cg = JsonMapper.MAPPER.treeToValue(g, CedarFSGroup.class);
      } catch (JsonProcessingException e) {
        log.error("Error deserializing group", e);
      }
    }
    return cg;
  }

  CedarFSUser findUserById(String userURL) {
    String cypher = CypherQueryBuilder.getUserById();
    Map<String, Object> params = CypherParamBuilder.getUserById(userURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return buildUser(userNode);
  }

  CedarFSUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email, CedarFSGroup group) {
    return createUser(userURL, name, displayName, firstName, lastName, email, null, group);
  }

  CedarFSUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email) {
    return createUser(userURL, name, displayName, firstName, lastName, email, null, null);
  }

  CedarFSUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email, Map<String, Object> extraProperties) {
    String cypher = CypherQueryBuilder.createUser();
    Map<String, Object> params = CypherParamBuilder.createUser(userURL, name, displayName, firstName, lastName, email,
        extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return buildUser(userNode);
  }

  CedarFSUser createUser(String userURL, String name, String displayName, String firstName, String lastName, String
      email, Map<String, Object> extraProperties, CedarFSGroup group) {
    CedarFSUser newUser = createUser(userURL, name, displayName, firstName, lastName, email, extraProperties);
    if (group != null) {
      addGroupToUser(newUser, group);
    }
    return newUser;
  }

  CedarFSGroup createGroup(String groupURL, String name, String displayName, String description, String ownerURL,
                           Map<String, Object> extraProperties) {
    String cypher = CypherQueryBuilder.createGroup(extraProperties);
    Map<String, Object> params = CypherParamBuilder.createGroup(groupURL, name, displayName, description, ownerURL,
        extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  boolean wipeAllData() {
    String cypher = CypherQueryBuilder.wipeAllData();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while deleting all data:", error);
    }
    return errorsNode.size() == 0;
  }

  public CedarFSGroup findGroupBySpecialValue(String specialGroupName) {
    String cypher = CypherQueryBuilder.getGroupBySpecialValue();
    Map<String, Object> params = CypherParamBuilder.getGroupBySpecialValue(specialGroupName);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  CedarFSGroup findGroupById(String groupURL) {
    String cypher = CypherQueryBuilder.getGroupById();
    Map<String, Object> params = CypherParamBuilder.getGroupById(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  CedarFSGroup findGroupByName(String groupName) {
    String cypher = CypherQueryBuilder.getGroupByName();
    Map<String, Object> params = CypherParamBuilder.getGroupByName(groupName);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return buildGroup(groupNode);
  }

  boolean addGroupToUser(CedarFSUser user, CedarFSGroup group) {
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

  boolean addPermission(CedarFSFolder folder, CedarFSGroup group, NodePermission permission) {
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

  boolean addPermission(CedarFSFolder folder, CedarFSUser user, NodePermission permission) {
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

  boolean addPermission(CedarFSResource resource, CedarFSGroup group, NodePermission permission) {
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

  boolean addPermission(CedarFSResource resource, CedarFSUser user, NodePermission permission) {
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

  boolean removePermission(CedarFSFolder folder, CedarFSUser user, NodePermission permission) {
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

  boolean removePermission(CedarFSFolder folder, CedarFSGroup group, NodePermission permission) {
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

  boolean removePermission(CedarFSResource resource, CedarFSUser user, NodePermission permission) {
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

  boolean removePermission(CedarFSResource resource, CedarFSGroup group, NodePermission permission) {
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

  boolean updateOwner(CedarFSFolder folder, CedarFSUser user) {
    boolean removed = removeOwner(folder);
    if (removed) {
      return setOwner(folder, user);
    }
    return false;
  }

  boolean updateOwner(CedarFSResource resource, CedarFSUser user) {
    boolean removed = removeOwner(resource);
    if (removed) {
      return setOwner(resource, user);
    }
    return false;
  }

  boolean removeOwner(CedarFSResource resource) {
    String cypher = CypherQueryBuilder.removeResourceOwner();
    Map<String, Object> params = CypherParamBuilder.matchResourceId(resource.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while removing owner:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean removeOwner(CedarFSFolder folder) {
    String cypher = CypherQueryBuilder.removeFolderOwner();
    Map<String, Object> params = CypherParamBuilder.matchFolderId(folder.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while removing owner:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean setOwner(CedarFSResource resource, CedarFSUser user) {
    String cypher = CypherQueryBuilder.setResourceOwner();
    Map<String, Object> params = CypherParamBuilder.matchResourceAndUser(resource.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while setting owner:", error);
    }
    return errorsNode.size() == 0;
  }

  boolean setOwner(CedarFSFolder folder, CedarFSUser user) {
    String cypher = CypherQueryBuilder.setFolderOwner();
    Map<String, Object> params = CypherParamBuilder.matchFolderAndUser(folder.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while setting owner:", error);
    }
    return errorsNode.size() == 0;
  }

  CedarFSUser getNodeOwner(String nodeURL) {
    String cypher = CypherQueryBuilder.getNodeOwner();
    Map<String, Object> params = CypherParamBuilder.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return buildUser(userNode);
  }

  List<CedarFSUser> getUsersWithPermissionOnNode(String nodeURL, NodePermission permission) {
    List<CedarFSUser> userList = new ArrayList<>();
    RelationLabel relationLabel = null;
    switch (permission) {
      case READ:
        relationLabel = RelationLabel.CANREAD;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilder.getUsersWithPermissionOnNode(relationLabel);
    Map<String, Object> params = CypherParamBuilder.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode userNode = f.at("/row/0");
        if (userNode != null && !userNode.isMissingNode()) {
          CedarFSUser cu = buildUser(userNode);
          userList.add(cu);
        }
      });
    }
    return userList;
  }

  List<CedarFSGroup> getGroupsWithPermissionOnNode(String nodeURL, NodePermission permission) {
    List<CedarFSGroup> groupList = new ArrayList<>();
    RelationLabel relationLabel = null;
    switch (permission) {
      case READ:
        relationLabel = RelationLabel.CANREAD;
        break;
      case WRITE:
        relationLabel = RelationLabel.CANWRITE;
        break;
    }
    String cypher = CypherQueryBuilder.getGroupsWithPermissionOnNode(relationLabel);
    Map<String, Object> params = CypherParamBuilder.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupListJsonNode = jsonNode.at("/results/0/data");
    if (groupListJsonNode != null && !groupListJsonNode.isMissingNode()) {
      groupListJsonNode.forEach(f -> {
        JsonNode groupNode = f.at("/row/0");
        if (groupNode != null && !groupNode.isMissingNode()) {
          CedarFSGroup g = buildGroup(groupNode);
          groupList.add(g);
        }
      });
    }
    return groupList;
  }

  public void addPermissionToGroup(String nodeURL, String groupURL, NodePermission permission, boolean nodeIsFolder) {
    CedarFSGroup group = findGroupById(groupURL);
    if (group != null) {
      if (nodeIsFolder) {
        CedarFSFolder folder = findFolderById(nodeURL);
        if (folder != null) {
          addPermission(folder, group, permission);
        }
      } else {
        CedarFSResource resource = findResourceById(nodeURL);
        if (resource != null) {
          addPermission(resource, group, permission);
        }
      }
    }
  }

  public void removePermissionFromGroup(String nodeURL, String groupURL, NodePermission permission, boolean
      nodeIsFolder) {
    CedarFSGroup group = findGroupById(groupURL);
    if (group != null) {
      if (nodeIsFolder) {
        CedarFSFolder folder = findFolderById(nodeURL);
        if (folder != null) {
          removePermission(folder, group, permission);
        }
      } else {
        CedarFSResource resource = findResourceById(nodeURL);
        if (resource != null) {
          removePermission(resource, group, permission);
        }
      }
    }
  }

  public void addPermissionToUser(String nodeURL, String userURL, NodePermission permission, boolean nodeIsFolder) {
    CedarFSUser user = findUserById(userURL);
    if (user != null) {
      if (nodeIsFolder) {
        CedarFSFolder folder = findFolderById(nodeURL);
        if (folder != null) {
          addPermission(folder, user, permission);
        }
      } else {
        CedarFSResource resource = findResourceById(nodeURL);
        if (resource != null) {
          addPermission(resource, user, permission);
        }
      }
    }
  }

  public void removePermissionFromUser(String nodeURL, String userURL, NodePermission permission, boolean
      nodeIsFolder) {
    CedarFSUser user = findUserById(userURL);
    if (user != null) {
      if (nodeIsFolder) {
        CedarFSFolder folder = findFolderById(nodeURL);
        if (folder != null) {
          removePermission(folder, user, permission);
        }
      } else {
        CedarFSResource resource = findResourceById(nodeURL);
        if (resource != null) {
          removePermission(resource, user, permission);
        }
      }
    }
  }

  public void updateNodeOwner(String nodeURL, String userURL, boolean nodeIsFolder) {
    CedarFSUser user = findUserById(userURL);
    if (user != null) {
      if (nodeIsFolder) {
        CedarFSFolder folder = findFolderById(nodeURL);
        if (folder != null) {
          updateOwner(folder, user);
        }
      } else {
        CedarFSResource resource = findResourceById(nodeURL);
        if (resource != null) {
          updateOwner(resource, user);
        }
      }
    }
  }

  public boolean userHasReadAccessToFolder(String userURL, String folderURL) {
    String cypher = CypherQueryBuilder.userCanReadNode(CypherQueryBuilder.FolderOrResource.FOLDER);
    Map<String, Object> params = CypherParamBuilder.matchUserIdAndNodeId(userURL, folderURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    CedarFSUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  public boolean userHasWriteAccessToFolder(String userURL, String folderURL) {
    String cypher = CypherQueryBuilder.userCanWriteNode(CypherQueryBuilder.FolderOrResource.FOLDER);
    Map<String, Object> params = CypherParamBuilder.matchUserIdAndNodeId(userURL, folderURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    CedarFSUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  public boolean userHasReadAccessToResource(String userURL, String resourceURL) {
    String cypher = CypherQueryBuilder.userCanReadNode(CypherQueryBuilder.FolderOrResource.RESOURCE);
    Map<String, Object> params = CypherParamBuilder.matchUserIdAndNodeId(userURL, resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    CedarFSUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  public boolean userHasWriteAccessToResource(String userURL, String resourceURL) {
    String cypher = CypherQueryBuilder.userCanWriteNode(CypherQueryBuilder.FolderOrResource.RESOURCE);
    Map<String, Object> params = CypherParamBuilder.matchUserIdAndNodeId(userURL, resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    CedarFSUser cedarFSUser = buildUser(userNode);
    return cedarFSUser != null;
  }

  List<CedarFSUser> findUsers() {
    List<CedarFSUser> userList = new ArrayList<>();
    String cypher = CypherQueryBuilder.findUsers();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode userNode = f.at("/row/0");
        if (userNode != null && !userNode.isMissingNode()) {
          CedarFSUser cu = buildUser(userNode);
          userList.add(cu);
        }
      });
    }
    return userList;
  }

  List<CedarFSGroup> findGroups() {
    List<CedarFSGroup> groupList = new ArrayList<>();
    String cypher = CypherQueryBuilder.findGroups();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode groupNode = f.at("/row/0");
        if (groupNode != null && !groupNode.isMissingNode()) {
          CedarFSGroup cg = buildGroup(groupNode);
          groupList.add(cg);
        }
      });
    }
    return groupList;
  }

  public boolean moveResource(CedarFSResource sourceResource, CedarFSFolder targetFolder) {
    boolean unlink = unlinkResourceFromParent(sourceResource);
    if (unlink) {
      return linkResourceUnderFolder(sourceResource, targetFolder);
    }
    return false;
  }

  public boolean moveFolder(CedarFSFolder sourceFolder, CedarFSFolder targetFolder) {
    if (sourceFolder.getId().equals(targetFolder.getId())) {
      return false;
    }
    if (folderIsAncestorOf(sourceFolder, targetFolder)) {
      return false;
    }
    boolean unlink = unlinkFolderFromParent(sourceFolder);
    if (unlink) {
      return linkFolderUnderFolder(sourceFolder, targetFolder);
    }
    return false;
  }

  private boolean unlinkResourceFromParent(CedarFSResource resource) {
    String cypher = CypherQueryBuilder.unlinkResourceFromParent();
    Map<String, Object> params = CypherParamBuilder.matchResourceId(resource.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while unlinking resource:", error);
    }
    return errorsNode.size() == 0;
  }

  private boolean linkResourceUnderFolder(CedarFSResource resource, CedarFSFolder parentFolder) {
    String cypher = CypherQueryBuilder.linkResourceUnderFolder();
    Map<String, Object> params = CypherParamBuilder.matchResourceIdAndParentFolderId(resource.getId(), parentFolder
        .getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while linking resource:", error);
    }
    return errorsNode.size() == 0;
  }


  private boolean unlinkFolderFromParent(CedarFSFolder folder) {
    String cypher = CypherQueryBuilder.unlinkFolderFromParent();
    Map<String, Object> params = CypherParamBuilder.matchFolderId(folder.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while unlinking folder:", error);
    }
    return errorsNode.size() == 0;
  }

  private boolean linkFolderUnderFolder(CedarFSFolder folder, CedarFSFolder parentFolder) {
    String cypher = CypherQueryBuilder.linkFolderUnderFolder();
    Map<String, Object> params = CypherParamBuilder.matchFolderIdAndParentFolderId(folder.getId(), parentFolder.getId
        ());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while linking folder:", error);
    }
    return errorsNode.size() == 0;
  }

  private boolean folderIsAncestorOf(CedarFSFolder parentFolder, CedarFSFolder folder) {
    String cypher = CypherQueryBuilder.folderIsAncestorOf();
    Map<String, Object> params = CypherParamBuilder.matchFolderIdAndParentFolderId(folder.getId(), parentFolder.getId
        ());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode folderNode = jsonNode.at("/results/0/data/0/row/0");
    CedarFSFolder parent = buildFolder(folderNode);
    return parent != null;
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
          CedarFSNode node = buildNode(nodeNode);
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

  CedarFSGroup updateGroupById(String groupId, Map<String, String> updateFields, String updatedBy) {
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

  List<CedarFSUser> findGroupMembers(String groupURL) {
    List<CedarFSUser> memberList = new ArrayList<>();
    String cypher = CypherQueryBuilder.getGroupUsersWithRelation(RelationLabel.MEMBEROF);
    Map<String, Object> params = CypherParamBuilder.matchGroupId(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode userNode = f.at("/row/0");
        if (userNode != null && !userNode.isMissingNode()) {
          CedarFSUser u = buildUser(userNode);
          memberList.add(u);
        }
      });
    }
    return memberList;
  }

  List<CedarFSUser> findGroupAdministrators(String groupURL) {
    List<CedarFSUser> memberList = new ArrayList<>();
    String cypher = CypherQueryBuilder.getGroupUsersWithRelation(RelationLabel.ADMINISTERS);
    Map<String, Object> params = CypherParamBuilder.matchGroupId(groupURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userListJsonNode = jsonNode.at("/results/0/data");
    if (userListJsonNode != null && !userListJsonNode.isMissingNode()) {
      userListJsonNode.forEach(f -> {
        JsonNode userNode = f.at("/row/0");
        if (userNode != null && !userNode.isMissingNode()) {
          CedarFSUser u = buildUser(userNode);
          memberList.add(u);
        }
      });
    }
    return memberList;
  }

  public void addUserGroupRelation(String userURL, String groupURL, RelationLabel relation) {
    CedarFSGroup group = findGroupById(groupURL);
    if (group != null) {
      CedarFSUser user = findUserById(userURL);
      if (user != null) {
        addRelation(user, group, relation);
      }
    }
  }

  public void removeUserGroupRelation(String userURL, String groupURL, RelationLabel relation) {
    CedarFSGroup group = findGroupById(groupURL);
    if (group != null) {
      CedarFSUser user = findUserById(userURL);
      if (user != null) {
        removeRelation(user, group, relation);
      }
    }
  }

  boolean addRelation(CedarFSUser user, CedarFSGroup group, RelationLabel relation) {
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

  boolean removeRelation(CedarFSUser user, CedarFSGroup group, RelationLabel relation) {
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