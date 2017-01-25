package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.neo4j.*;

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
    String cypher = CypherQueryBuilder.folderIsAncestorOf();
    Map<String, Object> params = CypherParamBuilder.matchFolderIdAndParentFolderId(folder.getId(), parentFolder.getId
        ());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode folderNode = jsonNode.at("/results/0/data/0/row/0");
    FolderServerFolder parent = buildFolder(folderNode);
    return parent != null;
  }

  private boolean unlinkFolderFromParent(FolderServerFolder folder) {
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

  private boolean linkFolderUnderFolder(FolderServerFolder folder, FolderServerFolder parentFolder) {
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

  FolderServerFolder updateFolderById(String folderId, Map<String, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilder.updateFolderById(updateFields);
    Map<String, Object> params = CypherParamBuilder.updateFolderById(folderId, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode updatedNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(updatedNode);
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

  List<FolderServerFolder> findFolderPathByPath(String path) {
    List<FolderServerFolder> pathList = new ArrayList<>();
    int cnt = proxies.pathUtil.getPathDepth(path);
    String cypher = CypherQueryBuilder.getFolderLookupQueryByDepth(cnt);
    Map<String, Object> params = CypherParamBuilder.getFolderLookupByDepthParameters(proxies.pathUtil, path);
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
    String cypher = CypherQueryBuilder.getFolderLookupQueryById();
    Map<String, Object> params = CypherParamBuilder.getFolderLookupByIDParameters(proxies.pathUtil, id);
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
                                             String creatorId, NodeLabel label, Map<String, Object> extraProperties) {
    String cypher = CypherQueryBuilder.createFolderAsChildOfId(label, extraProperties);
    Map<String, Object> params = CypherParamBuilder.createFolder(proxies.folderIdPrefix, parentId, name, displayName,
        description, creatorId, extraProperties);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode newNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(newNode);
  }

  private boolean setOwner(FolderServerFolder folder, FolderServerUser user) {
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

  private boolean removeOwner(FolderServerFolder folder) {
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

  boolean updateOwner(FolderServerFolder folder, FolderServerUser user) {
    boolean removed = removeOwner(folder);
    if (removed) {
      return setOwner(folder, user);
    }
    return false;
  }

  FolderServerFolder findFolderById(String folderUUID) {
    String cypher = CypherQueryBuilder.getFolderById();
    Map<String, Object> params = CypherParamBuilder.getFolderById(folderUUID);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode folderNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(folderNode);
  }

  FolderServerFolder createRootFolder(String creatorId) {
    Map<String, Object> extraParams = new HashMap<>();
    extraParams.put(Neo4JFields.IS_ROOT, true);
    extraParams.put(Neo4JFields.IS_SYSTEM, true);
    String cypher = CypherQueryBuilder.createRootFolder(extraParams);
    Map<String, Object> params = CypherParamBuilder.createFolder(proxies.folderIdPrefix, null, proxies.config
            .getRootFolderPath(), proxies.config.getRootFolderPath(), proxies.config.getRootFolderDescription(),
        creatorId, extraParams);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode rootNode = jsonNode.at("/results/0/data/0/row/0");
    return buildFolder(rootNode);
  }


}
