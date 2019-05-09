package org.metadatacenter.server;

import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerArtifact;
import org.metadatacenter.model.folderserver.extract.FolderServerResourceExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerArtifactExtract;
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

  List<FolderServerResourceExtract> findAllNodes(int limit, int offset, List<String> sortList);

  long findAllNodesCount();

  FolderServerArtifact findResourceById(String resourceURL);

  FileSystemResource findNodeById(String nodeURL);

  FolderServerFolder createFolderAsChildOfId(FolderServerFolder newFolder, String parentFolderURL);

  FolderServerArtifact createResourceAsChildOfId(FolderServerArtifact newResource, String parentFolderURL);

  FolderServerFolder updateFolderById(String folderURL, Map<NodeProperty, String> updateFields);

  FolderServerArtifact updateResourceById(String resourceURL, CedarResourceType resourceType, Map<NodeProperty,
      String> updateFields);

  boolean deleteFolderById(String folderURL);

  boolean deleteResourceById(String resourceURL);

  FolderServerFolder findFolderByPath(String path);

  FileSystemResource findNodeByParentIdAndName(FolderServerFolder parentFolder, String name);

  List<FolderServerFolder> findFolderPath(FolderServerFolder folder);

  List<FolderServerResourceExtract> findNodePathExtract(FileSystemResource node);

  List<FileSystemResource> findFolderContentsFiltered(String folderURL, List<CedarResourceType> resourceTypeList,
                                                      ResourceVersionFilter version, ResourcePublicationStatusFilter
                                                        publicationStatus, int limit, int offset, List<String>
                                                        sortList);

  List<FolderServerResourceExtract> findFolderContentsExtractFiltered(String folderURL, List<CedarResourceType> resourceTypeList,
                                                                      ResourceVersionFilter version,
                                                                      ResourcePublicationStatusFilter publicationStatus,
                                                                      int limit, int offset, List<String> sortList);

  List<FolderServerResourceExtract> findFolderContentsExtract(String folderURL, List<CedarResourceType> resourceTypeList,
                                                              ResourceVersionFilter version,
                                                              ResourcePublicationStatusFilter publicationStatus,
                                                              int limit, int offset, List<String> sortList);

  long findFolderContentsFilteredCount(String folderURL, List<CedarResourceType> resourceTypeList, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus);

  long findFolderContentsCount(String folderURL, List<CedarResourceType> resourceTypeList, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus);

  long findFolderContentsUnfilteredCount(String folderURL);

  void addPathAndParentId(FolderServerFolder folder);

  void addPathAndParentId(FolderServerArtifact resource);

  boolean moveResource(FolderServerArtifact sourceResource, FolderServerFolder targetFolder);

  boolean moveFolder(FolderServerFolder sourceFolder, FolderServerFolder targetFolder);

  FolderServerFolder ensureUserHomeExists();

  List<FolderServerResourceExtract> viewSharedWithMe(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                                     ResourcePublicationStatusFilter publicationStatus, int limit,
                                                     int offset, List<String> sortList);

  List<FolderServerResourceExtract> viewSharedWithEverybody(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                                            ResourcePublicationStatusFilter publicationStatus, int limit,
                                                            int offset, List<String> sortList);

  long viewSharedWithMeCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                             ResourcePublicationStatusFilter publicationStatus);

  long viewSharedWithEverybodyCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                    ResourcePublicationStatusFilter publicationStatus);

  List<FolderServerResourceExtract> viewAll(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                            ResourcePublicationStatusFilter publicationStatus, int limit, int offset,
                                            List<String> sortList);

  long viewAllCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus);

  List<FileSystemResource> findAllDescendantNodesById(String id);

  List<FileSystemResource> findAllNodesVisibleByGroupId(String id);

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

  FolderServerArtifactExtract findResourceExtractById(ResourceUri id);

  List<FolderServerArtifactExtract> getVersionHistory(String id);

  List<FolderServerArtifactExtract> getVersionHistoryWithPermission(String id);

  List<FolderServerResourceExtract> searchIsBasedOn(List<CedarResourceType> resourceTypeList, String isBasedOn, int limit,
                                                    int offset, List<String> sortList);

  long searchIsBasedOnCount(List<CedarResourceType> resourceTypeList, String isBasedOn);
}
