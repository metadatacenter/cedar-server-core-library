package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.*;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Neo4JProxyNode extends AbstractNeo4JProxy {

  Neo4JProxyNode(Neo4JProxies proxies) {
    super(proxies);
  }

  List<FolderServerNode> findNodePathById(String id) {
    List<FolderServerNode> pathList = new ArrayList<>();
    String cypher = CypherQueryBuilder.getNodeLookupQueryById();
    Map<String, Object> params = CypherParamBuilder.getNodeLookupByIDParameters(proxies.pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode pathListJsonNode = jsonNode.at("/results/0/data/0/row/0");
    if (pathListJsonNode != null && !pathListJsonNode.isMissingNode()) {
      pathListJsonNode.forEach(f -> {
        // relationships are also included, filter them out
        Map pathElement = buildMap(f);
        if (pathElement != null && !pathElement.isEmpty()) {
          FolderServerNode cf = buildNode(f);
          if (cf != null) {
            pathList.add(cf);
          }
        }
      });
    }
    return pathList;
  }

  long findFolderContentsFilteredCount(String folderId, List<CedarNodeType> nodeTypeList) {
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

  long findFolderContentsCount(String folderId) {
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

  List<FolderServerNode> findAllNodes(int limit, int offset, List<String> sortList) {
    List<FolderServerNode> resources = new ArrayList<>();
    String cypher = CypherQueryBuilder.getAllNodesLookupQuery(sortList);
    Map<String, Object> params = CypherParamBuilder.getAllNodesLookupParameters(limit, offset);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);

    JsonNode resourceListJsonNode = jsonNode.at("/results/0/data");
    if (resourceListJsonNode != null && !resourceListJsonNode.isMissingNode()) {
      resourceListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row/0");
        FolderServerNode cf = buildNode(nodeNode);
        if (cf != null) {
          resources.add(cf);
        }
      });
    }
    return resources;
  }

  long findAllNodesCount() {
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

  List<FolderServerNode> findFolderContents(String folderId, Collection<CedarNodeType> nodeTypes, int
      limit, int offset, List<String> sortList, CedarUser cu) {
    List<FolderServerNode> resources = new ArrayList<>();

    boolean addPermissionConditions = true;
    String cypher = CypherQueryBuilder.getFolderContentsLookupQuery(sortList, addPermissionConditions);
    String ownerId = proxies.userIdPrefix + cu.getId();
    Map<String, Object> params = CypherParamBuilder.getFolderContentsLookupParameters(folderId, nodeTypes, limit,
        offset, ownerId, addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode resourceListJsonNode = jsonNode.at("/results/0/data");
    if (resourceListJsonNode != null && !resourceListJsonNode.isMissingNode()) {
      resourceListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row/0");
        FolderServerNode cf = buildNode(nodeNode);
        if (cf != null) {
          resources.add(cf);
        }
      });
    }
    return resources;
  }

  FolderServerNode findNodeByParentIdAndName(String parentId, String name) {
    String cypher = CypherQueryBuilder.getNodeByParentIdAndName();
    Map<String, Object> params = CypherParamBuilder.getNodeByParentIdAndName(parentId, name);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode node = jsonNode.at("/results/0/data/0/row/0");
    return buildNode(node);
  }

  void updateNodeOwner(String nodeURL, String userURL, boolean nodeIsFolder) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      if (nodeIsFolder) {
        FolderServerFolder folder = proxies.folder().findFolderById(nodeURL);
        if (folder != null) {
          proxies.folder().updateOwner(folder, user);
        }
      } else {
        FolderServerResource resource = proxies.resource().findResourceById(nodeURL);
        if (resource != null) {
          proxies.resource().updateOwner(resource, user);
        }
      }
    }
  }

  FolderServerUser getNodeOwner(String nodeURL) {
    String cypher = CypherQueryBuilder.getNodeOwner();
    Map<String, Object> params = CypherParamBuilder.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return buildUser(userNode);
  }

  public List<FolderServerNode> findSharedWithMeFiltered(List<CedarNodeType> nodeTypes, int limit, int offset, List<String> sortList, CedarUser cu) {
    List<FolderServerNode> resources = new ArrayList<>();
    boolean addPermissionConditions = true;
    String cypher = CypherQueryBuilder.getSharedWithMeLookupQuery(sortList, addPermissionConditions);
    String ownerId = proxies.userIdPrefix + cu.getId();
    Map<String, Object> params = CypherParamBuilder.getSharedWithMeLookupParameters(nodeTypes, limit,
        offset, ownerId, addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode resourceListJsonNode = jsonNode.at("/results/0/data");
    if (resourceListJsonNode != null && !resourceListJsonNode.isMissingNode()) {
      resourceListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row/0");
        FolderServerNode cf = buildNode(nodeNode);
        if (cf != null) {
          resources.add(cf);
        }
      });
    }
    return resources;
  }

  public long findSharedWithMeFilteredCount(List<CedarNodeType> nodeTypes, CedarUser cu) {
    boolean addPermissionConditions = true;
    String cypher = CypherQueryBuilder.getSharedWithMeCountQuery(addPermissionConditions);
    String ownerId = proxies.userIdPrefix + cu.getId();
    Map<String, Object> params = CypherParamBuilder.getSharedWithMeCountParameters(nodeTypes, ownerId, addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode countNode = jsonNode.at("/results/0/data/0/row/0");
    if (countNode != null && !countNode.isMissingNode()) {
      return countNode.asLong();
    } else {
      return -1;
    }
  }

}