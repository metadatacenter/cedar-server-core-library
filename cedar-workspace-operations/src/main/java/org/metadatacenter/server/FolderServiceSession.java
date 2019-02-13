package org.metadatacenter.server;

import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.basic.FolderServerResource;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerResourceExtract;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.List;
import java.util.Map;

public interface FolderServiceSession {
  // Expose methods of PathUtil
  String sanitizeName(String name);

  String normalizePath(String path);

  String getRootPath();

  FolderServerFolder findFolderById(String folderURL);

  List<FolderServerNodeExtract> findAllNodes(int limit, int offset, List<String> sortList);

  long findAllNodesCount();

  FolderServerResource findResourceById(String resourceURL);

  FolderServerNode findNodeById(String nodeURL);

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

  List<FolderServerNodeExtract> findNodePathExtract(FolderServerNode node);

  List<FolderServerNode> findFolderContentsFiltered(String folderURL, List<CedarNodeType> nodeTypeList,
                                                    ResourceVersionFilter version, ResourcePublicationStatusFilter
                                                        publicationStatus, int limit, int offset, List<String>
                                                        sortList);

  List<FolderServerNodeExtract> findFolderContentsExtractFiltered(String folderURL, List<CedarNodeType> nodeTypeList,
                                                                  ResourceVersionFilter version,
                                                                  ResourcePublicationStatusFilter publicationStatus,
                                                                  int limit, int offset, List<String> sortList);

  List<FolderServerNodeExtract> findFolderContentsExtract(String folderURL, List<CedarNodeType> nodeTypeList,
                                                          ResourceVersionFilter version,
                                                          ResourcePublicationStatusFilter publicationStatus,
                                                          int limit, int offset, List<String> sortList);

  long findFolderContentsFilteredCount(String folderURL, List<CedarNodeType> nodeTypeList, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus);

  long findFolderContentsCount(String folderURL, List<CedarNodeType> nodeTypeList, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus);

  long findFolderContentsUnfilteredCount(String folderURL);

  void addPathAndParentId(FolderServerFolder folder);

  void addPathAndParentId(FolderServerResource resource);

  boolean moveResource(FolderServerResource sourceResource, FolderServerFolder targetFolder);

  boolean moveFolder(FolderServerFolder sourceFolder, FolderServerFolder targetFolder);

  FolderServerFolder ensureUserHomeExists();

  List<FolderServerNodeExtract> viewSharedWithMe(List<CedarNodeType> nodeTypeList, ResourceVersionFilter version,
                                                 ResourcePublicationStatusFilter publicationStatus, int limit,
                                                 int offset, List<String> sortList);

  List<FolderServerNodeExtract> viewSharedWithEverybody(List<CedarNodeType> nodeTypeList, ResourceVersionFilter version,
                                                        ResourcePublicationStatusFilter publicationStatus, int limit,
                                                        int offset, List<String> sortList);

  long viewSharedWithMeCount(List<CedarNodeType> nodeTypeList, ResourceVersionFilter version,
                             ResourcePublicationStatusFilter publicationStatus);

  long viewSharedWithEverybodyCount(List<CedarNodeType> nodeTypeList, ResourceVersionFilter version,
                                    ResourcePublicationStatusFilter publicationStatus);

  List<FolderServerNodeExtract> viewAll(List<CedarNodeType> nodeTypeList, ResourceVersionFilter version,
                                        ResourcePublicationStatusFilter publicationStatus, int limit, int offset,
                                        List<String> sortList);

  long viewAllCount(List<CedarNodeType> nodeTypeList, ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus);

  List<FolderServerNode> findAllDescendantNodesById(String id);

  List<FolderServerNode> findAllNodesVisibleByGroupId(String id);

  FolderServerFolder findHomeFolderOf();

  FolderServerFolder createUserHomeFolder();

  boolean setDerivedFrom(String newId, String oldId);

  boolean unsetLatestVersion(String id);

  boolean setLatestVersion(String id);

  boolean setLatestPublishedVersion(String id);

  boolean unsetLatestPublishedVersion(String id);

  boolean unsetLatestDraftVersion(String id);

  boolean setOpen(String id);

  boolean setNotOpen(String id);

  long getNumberOfInstances(String templateId);

  FolderServerResourceExtract findResourceExtractById(ResourceUri id);

  List<FolderServerResourceExtract> getVersionHistory(String id);

  List<FolderServerResourceExtract> getVersionHistoryWithPermission(String id);

  List<FolderServerNodeExtract> searchIsBasedOn(List<CedarNodeType> nodeTypeList, String isBasedOn, int limit,
                                                int offset, List<String> sortList);

  long searchIsBasedOnCount(List<CedarNodeType> nodeTypeList, String isBasedOn);
}
