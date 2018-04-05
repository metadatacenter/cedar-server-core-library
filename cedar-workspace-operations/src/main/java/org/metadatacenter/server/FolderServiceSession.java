package org.metadatacenter.server;

import org.metadatacenter.model.*;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

import java.util.List;
import java.util.Map;

public interface FolderServiceSession {
  // Expose methods of PathUtil
  String sanitizeName(String name);

  String normalizePath(String path);

  String getRootPath();

  FolderServerFolder findFolderById(String folderURL);

  List<FolderServerNode> findAllNodes(int limit, int offset, List<String> sortList);

  long findAllNodesCount();

  FolderServerResource findResourceById(String resourceURL);

  FolderServerFolder createFolderAsChildOfId(FolderServerFolder newFolder, String parentFolderURL);

  FolderServerResource createResourceAsChildOfId(FolderServerResource newResource, String parentFolderURL);

  FolderServerFolder updateFolderById(String folderURL, Map<NodeProperty, String> updateFields);

  FolderServerResource updateResourceById(String resourceURL, CedarNodeType nodeType, Map<NodeProperty,
      String> updateFields);

  boolean deleteFolderById(String folderURL);

  boolean deleteResourceById(String resourceURL, CedarNodeType nodeType);

  FolderServerFolder findFolderByPath(String path);

  FolderServerNode findNodeByParentIdAndName(FolderServerFolder parentFolder, String name);

  List<FolderServerFolder> findFolderPath(FolderServerFolder folder);

  List<FolderServerNode> findFolderContentsFiltered(String folderURL, List<CedarNodeType> nodeTypeList, int
      limit, int offset, List<String> sortList);

  long findFolderContentsFilteredCount(String folderURL, List<CedarNodeType> nodeTypeList);

  long findFolderContentsUnfilteredCount(String folderURL);

  void addPathAndParentId(FolderServerFolder folder);

  void addPathAndParentId(FolderServerResource resource);

  boolean moveResource(FolderServerResource sourceResource, FolderServerFolder targetFolder);

  boolean moveFolder(FolderServerFolder sourceFolder, FolderServerFolder targetFolder);

  FolderServerFolder ensureUserHomeExists();

  List<FolderServerNode> viewSharedWithMe(List<CedarNodeType> nodeTypeList, int limit, int offset, List<String>
      sortList);

  long viewSharedWithMeCount(List<CedarNodeType> nodeTypeList);

  List<FolderServerNode> viewAll(List<CedarNodeType> nodeTypeList, int limit, int offset, List<String>
      sortList);

  long viewAllCount(List<CedarNodeType> nodeTypeList);

  List<FolderServerNode> findAllDescendantNodesById(String id);

  List<FolderServerNode> findAllNodesVisibleByGroupId(String id);

  FolderServerFolder findHomeFolderOf();

  FolderServerFolder createUserHomeFolder();

  boolean setPreviousVersion(String newId, String oldId);

  boolean setDerivedFrom(String newId, String oldId);
}