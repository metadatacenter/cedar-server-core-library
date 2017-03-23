package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.*;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderFolder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderFolder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderUser;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neo4JProxyFolder extends AbstractNeo4JProxy {

  Neo4JProxyFolder(Neo4JProxies proxies) {
    super(proxies);
  }

  boolean moveFolder(FolderServerFolder sourceFolder, FolderServerFolder targetFolder) {
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

  private boolean folderIsAncestorOf(FolderServerFolder parentFolder, FolderServerFolder folder) {
    String cypher = CypherQueryBuilderFolder.folderIsAncestorOf();
    CypherParameters params = CypherParamBuilderFolder.matchFolderIdAndParentFolderId(folder.getId(), parentFolder
        .getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode folderNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerFolder parent = buildFolder(folderNode);
    return parent != null;
  }

  private boolean unlinkFolderFromParent(FolderServerFolder folder) {
    String cypher = CypherQueryBuilderFolder.unlinkFolderFromParent();
    CypherParameters params = CypherParamBuilderFolder.matchFolderId(folder.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while unlinking folder:");
  }

  private boolean linkFolderUnderFolder(FolderServerFolder folder, FolderServerFolder parentFolder) {
    String cypher = CypherQueryBuilderFolder.linkFolderUnderFolder();
    CypherParameters params = CypherParamBuilderFolder.matchFolderIdAndParentFolderId(folder.getId(), parentFolder.getId
        ());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while linking folder:");
  }

  FolderServerFolder updateFolderById(String folderId, Map<NodeProperty, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilderFolder.updateFolderById(updateFields);
    CypherParameters params = CypherParamBuilderFolder.updateFolderById(folderId, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode updatedNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(updatedNode);
  }

  boolean deleteFolderById(String folderId) {
    String cypher = CypherQueryBuilderFolder.deleteFolderContentsRecursivelyById();
    CypherParameters params = CypherParamBuilderFolder.deleteFolderById(folderId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while deleting folder:");
  }

  List<FolderServerFolder> findFolderPathByPath(String path) {
    List<FolderServerFolder> pathList = new ArrayList<>();
    int cnt = proxies.pathUtil.getPathDepth(path);
    String cypher = CypherQueryBuilderFolder.getFolderLookupQueryByDepth(cnt);
    CypherParameters params = CypherParamBuilderFolder.getFolderLookupByDepthParameters(proxies.pathUtil, path);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode pathListJsonNode = jsonNode.at("/results/0/data/0/row");
    if (pathListJsonNode != null && !pathListJsonNode.isMissingNode()) {
      pathListJsonNode.forEach(f -> {
        FolderServerFolder cf = buildFolder(f);
        if (cf != null) {
          pathList.add(cf);
        }
      });
    }
    return pathList;
  }

  List<FolderServerFolder> findFolderPathById(String id) {
    List<FolderServerFolder> pathList = new ArrayList<>();
    String cypher = CypherQueryBuilderFolder.getFolderLookupQueryById();
    CypherParameters params = CypherParamBuilderFolder.getFolderLookupByIDParameters(proxies.pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode pathListJsonNode = jsonNode.at("/results/0/data/0/row/0");
    if (pathListJsonNode != null && !pathListJsonNode.isMissingNode()) {
      pathListJsonNode.forEach(f -> {
        // relationships are also included, filter them out
        Map pathElement = buildMap(f);
        if (pathElement != null && !pathElement.isEmpty()) {
          FolderServerFolder cf = buildFolder(f);
          if (cf != null) {
            pathList.add(cf);
          }
        }
      });
    }
    return pathList;
  }

  FolderServerFolder findFolderByPath(String path) {
    List<FolderServerFolder> folderPath = findFolderPathByPath(path);
    if (folderPath != null && folderPath.size() > 0) {
      return folderPath.get(folderPath.size() - 1);
    }
    return null;
  }

  FolderServerFolder createFolderAsChildOfId(String parentId, String name, String displayName, String description,
                                             String creatorId, NodeLabel label, Map<NodeProperty, Object>
                                                 extraProperties) {
    String cypher = CypherQueryBuilderFolder.createFolderAsChildOfId(label, extraProperties);
    CypherParameters params = CypherParamBuilderFolder.createFolder(proxies.getLinkedDataUtil(), parentId, name,
        displayName, description, creatorId, extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode newNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(newNode);
  }

  private boolean setOwner(FolderServerFolder folder, FolderServerUser user) {
    String cypher = CypherQueryBuilderFolder.setFolderOwner();
    CypherParameters params = CypherParamBuilderFolder.matchFolderAndUser(folder.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while setting owner:");
  }

  private boolean removeOwner(FolderServerFolder folder) {
    String cypher = CypherQueryBuilderFolder.removeFolderOwner();
    CypherParameters params = CypherParamBuilderFolder.matchFolderId(folder.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while removing owner:");
  }

  boolean updateOwner(FolderServerFolder folder, FolderServerUser user) {
    boolean removed = removeOwner(folder);
    if (removed) {
      return setOwner(folder, user);
    }
    return false;
  }

  FolderServerFolder findFolderById(String folderUUID) {
    String cypher = CypherQueryBuilderFolder.getFolderById();
    CypherParameters params = CypherParamBuilderFolder.getFolderById(folderUUID);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode folderNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(folderNode);
  }

  FolderServerFolder createRootFolder(String creatorId) {
    Map<NodeProperty, Object> extraParams = new HashMap<>();
    extraParams.put(NodeProperty.IS_ROOT, true);
    extraParams.put(NodeProperty.IS_SYSTEM, true);
    String cypher = CypherQueryBuilderFolder.createRootFolder(extraParams);
    CypherParameters params = CypherParamBuilderFolder.createFolder(proxies.getLinkedDataUtil(), null, proxies.config
            .getRootFolderPath(), proxies.config.getRootFolderPath(), proxies.config.getRootFolderDescription(),
        creatorId, extraParams);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode rootNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(rootNode);
  }

  public FolderServerFolder findHomeFolderOf(String userId) {
    String cypher = CypherQueryBuilderFolder.getHomeFolderOf();
    CypherParameters params = CypherParamBuilderUser.matchUserId(userId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode folderNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(folderNode);
  }
}
