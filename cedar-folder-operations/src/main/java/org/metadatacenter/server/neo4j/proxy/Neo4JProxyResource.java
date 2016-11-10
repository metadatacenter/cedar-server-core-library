package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.*;

import java.util.Map;

public class Neo4JProxyResource extends AbstractNeo4JProxy {

  Neo4JProxyResource(Neo4JProxies proxies) {
    super(proxies);
  }

  FolderServerResource createResourceAsChildOfId(String parentId, String childURL, CedarNodeType nodeType, String
      name, String description, String creatorId, NodeLabel label, Map<String, Object> extraProperties) {
    String cypher = CypherQueryBuilder.createResourceAsChildOfId(label, extraProperties);
    Map<String, Object> params = CypherParamBuilder.createResource(parentId, childURL, nodeType, name, description,
        creatorId, extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode newNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(newNode);
  }

  FolderServerResource updateResourceById(String resourceURL, Map<String, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilder.updateResourceById(updateFields);
    Map<String, Object> params = CypherParamBuilder.updateResourceById(resourceURL, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode updatedNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(updatedNode);
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

  boolean moveResource(FolderServerResource sourceResource, FolderServerFolder targetFolder) {
    boolean unlink = unlinkResourceFromParent(sourceResource);
    if (unlink) {
      return linkResourceUnderFolder(sourceResource, targetFolder);
    }
    return false;
  }

  private boolean unlinkResourceFromParent(FolderServerResource resource) {
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

  private boolean linkResourceUnderFolder(FolderServerResource resource, FolderServerFolder parentFolder) {
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

  private boolean setOwner(FolderServerResource resource, FolderServerUser user) {
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

  boolean updateOwner(FolderServerResource resource, FolderServerUser user) {
    boolean removed = removeOwner(resource);
    if (removed) {
      return setOwner(resource, user);
    }
    return false;
  }

  boolean removeOwner(FolderServerResource resource) {
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

  FolderServerResource findResourceById(String resourceURL) {
    String cypher = CypherQueryBuilder.getResourceById();
    Map<String, Object> params = CypherParamBuilder.getResourceById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode resourceNode = jsonNode.at("/results/0/data/0/row/0");
    return buildResource(resourceNode);
  }

  String getResourceUUID(String resourceId, CedarNodeType nodeType) {
    String prefix = proxies.getNodeTypeFullPrefix(nodeType);
    if (resourceId != null && resourceId.startsWith(prefix)) {
      return resourceId.substring(prefix.length());
    } else {
      return null;
    }
  }

}
