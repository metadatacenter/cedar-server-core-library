package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarResource;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderFolder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderUser;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderFolder;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderNode;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

import java.util.List;
import java.util.Map;

public class Neo4JProxyFolder extends AbstractNeo4JProxy {

  Neo4JProxyFolder(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
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
    FolderServerFolder parent = executeReadGetOne(q, FolderServerFolder.class);
    return parent != null;
  }

  private boolean unlinkFolderFromParent(FolderServerFolder folder) {
    String cypher = CypherQueryBuilderFolder.unlinkFolderFromParent();
    CypherParameters params = CypherParamBuilderFolder.matchFolderId(folder.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "unlinking folder");
  }

  private boolean linkFolderUnderFolder(FolderServerFolder folder, FolderServerFolder parentFolder) {
    String cypher = CypherQueryBuilderFolder.linkFolderUnderFolder();
    CypherParameters params = CypherParamBuilderFolder.matchFolderIdAndParentFolderId(folder.getId(), parentFolder.getId
        ());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "linking folder");
  }

  FolderServerFolder updateFolderById(String folderId, Map<NodeProperty, String> updateFields, String updatedBy) {
    String cypher = CypherQueryBuilderFolder.updateFolderById(updateFields);
    CypherParameters params = CypherParamBuilderFolder.updateFolderById(folderId, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerFolder.class);
  }

  boolean deleteFolderById(String folderId) {
    String cypher = CypherQueryBuilderFolder.deleteFolderContentsRecursivelyById();
    CypherParameters params = CypherParamBuilderFolder.deleteFolderById(folderId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "deleting folder");
  }

  List<FolderServerFolder> findFolderPathByPath(String path) {
    int cnt = proxies.pathUtil.getPathDepth(path);
    String cypher = CypherQueryBuilderFolder.getFolderLookupQueryByDepth(cnt);
    CypherParameters params = CypherParamBuilderFolder.getFolderLookupByDepthParameters(proxies.pathUtil, path);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerFolder.class);
  }

  private <T extends CedarResource> List<T> findFolderPathGenericById(String id, Class<T> klazz) {
    String cypher = CypherQueryBuilderFolder.getFolderLookupQueryById();
    CypherParameters params = CypherParamBuilderFolder.getFolderLookupByIDParameters(proxies.pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, klazz);
  }

  List<FolderServerFolder> findFolderPathById(String id) {
    return findFolderPathGenericById(id, FolderServerFolder.class);
  }

  FolderServerFolder findFolderByPath(String path) {
    List<FolderServerFolder> folderPath = findFolderPathByPath(path);
    if (folderPath != null && folderPath.size() > 0) {
      return folderPath.get(folderPath.size() - 1);
    }
    return null;
  }

  FolderServerFolder createFolderAsChildOfId(FolderServerFolder newFolder, String parentId) {
    String cypher = CypherQueryBuilderFolder.createFolderAsChildOfId(newFolder);
    CypherParameters params = CypherParamBuilderFolder.createFolder(proxies.getLinkedDataUtil(), newFolder, parentId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerFolder.class);
  }

  private boolean setOwner(FolderServerFolder folder, FolderServerUser user) {
    String cypher = CypherQueryBuilderFolder.setFolderOwner();
    CypherParameters params = CypherParamBuilderFolder.matchFolderAndUser(folder.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting owner");
  }

  private boolean removeOwner(FolderServerFolder folder) {
    String cypher = CypherQueryBuilderFolder.removeFolderOwner();
    CypherParameters params = CypherParamBuilderFolder.matchFolderId(folder.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing owner");
  }

  boolean updateOwner(FolderServerFolder folder, FolderServerUser user) {
    boolean removed = removeOwner(folder);
    if (removed) {
      return setOwner(folder, user);
    }
    return false;
  }

  FolderServerFolder createRootFolder(String creatorId) {
    FolderServerFolder newRoot = new FolderServerFolder();
    newRoot.setName(proxies.config.getRootFolderPath());
    newRoot.setDescription(proxies.config.getRootFolderDescription());
    newRoot.setCreatedByTotal(creatorId);
    newRoot.setRoot(true);
    newRoot.setSystem(true);
    newRoot.setUserHome(false);
    String cypher = CypherQueryBuilderFolder.createRootFolder(newRoot);
    CypherParameters params = CypherParamBuilderFolder.createFolder(proxies.getLinkedDataUtil(), newRoot, null);

    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerFolder.class);
  }

  public FolderServerFolder findHomeFolderOf(String userId) {
    String cypher = CypherQueryBuilderFolder.getHomeFolderOf();
    CypherParameters params = CypherParamBuilderUser.matchUserId(userId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerFolder.class);
  }

  public FolderServerFolder findFolderById(String nodeUUID) {
    String cypher = CypherQueryBuilderNode.getNodeById();
    CypherParameters params = CypherParamBuilderNode.getNodeById(nodeUUID);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerFolder.class);
  }
}
