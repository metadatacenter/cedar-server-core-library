package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.BiboStatus;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderResource;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderResource;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Neo4JProxyResource extends AbstractNeo4JProxy {

  Neo4JProxyResource(Neo4JProxies proxies) {
    super(proxies);
  }

  FolderServerResource createResourceAsChildOfId(String parentId, String childURL, CedarNodeType nodeType, String
      name, String description, String creatorId, NodeLabel label, ResourceVersion version, BiboStatus status) {
    String cypher = CypherQueryBuilderResource.createResourceAsChildOfId(label, version, status);
    CypherParameters params = CypherParamBuilderResource.createResource(parentId, childURL, nodeType, name,
        description, creatorId, version, status);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode newNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(newNode);
  }

  FolderServerResource updateResourceById(String resourceURL, Map<NodeProperty, String> updateFields, String
      updatedBy) {
    String cypher = CypherQueryBuilderResource.updateResourceById(updateFields);
    CypherParameters params = CypherParamBuilderResource.updateResourceById(resourceURL, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode updatedNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(updatedNode);
  }

  boolean deleteResourceById(String resourceURL) {
    String cypher = CypherQueryBuilderResource.deleteResourceById();
    CypherParameters params = CypherParamBuilderResource.deleteResourceById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while deleting resource:");
  }

  boolean moveResource(FolderServerResource sourceResource, FolderServerFolder targetFolder) {
    boolean unlink = unlinkResourceFromParent(sourceResource);
    if (unlink) {
      return linkResourceUnderFolder(sourceResource, targetFolder);
    }
    return false;
  }

  private boolean unlinkResourceFromParent(FolderServerResource resource) {
    String cypher = CypherQueryBuilderResource.unlinkResourceFromParent();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resource.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while unlinking resource:");
  }

  private boolean linkResourceUnderFolder(FolderServerResource resource, FolderServerFolder parentFolder) {
    String cypher = CypherQueryBuilderResource.linkResourceUnderFolder();
    CypherParameters params = AbstractCypherParamBuilder.matchResourceIdAndParentFolderId(resource.getId(), parentFolder
        .getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while linking resource:");
  }

  private boolean setOwner(FolderServerResource resource, FolderServerUser user) {
    String cypher = CypherQueryBuilderResource.setResourceOwner();
    CypherParameters params = AbstractCypherParamBuilder.matchResourceAndUser(resource.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while setting owner:");
  }

  boolean updateOwner(FolderServerResource resource, FolderServerUser user) {
    boolean removed = removeOwner(resource);
    if (removed) {
      return setOwner(resource, user);
    }
    return false;
  }

  boolean removeOwner(FolderServerResource resource) {
    String cypher = CypherQueryBuilderResource.removeResourceOwner();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resource.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while removing owner:");
  }

  FolderServerResource findResourceById(String resourceURL) {
    String cypher = CypherQueryBuilderResource.getResourceById();
    CypherParameters params = CypherParamBuilderResource.getResourceById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode resourceNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(resourceNode);
  }

  List<FolderServerNode> findResourcePathById(String id) {
    List<FolderServerNode> pathList = new ArrayList<>();
    String cypher = CypherQueryBuilderResource.getResourceLookupQueryById();
    CypherParameters params = CypherParamBuilderNode.getNodeLookupByIDParameters(proxies.pathUtil, id);
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


}
