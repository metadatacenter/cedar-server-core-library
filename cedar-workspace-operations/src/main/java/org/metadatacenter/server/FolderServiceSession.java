package org.metadatacenter.server;

import org.metadatacenter.id.*;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerArtifact;
import org.metadatacenter.model.folderserver.basic.FolderServerSchemaArtifact;
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

  FolderServerFolder findFolderById(CedarFolderId folderId);

  List<FolderServerResourceExtract> findAllNodes(int limit, int offset, List<String> sortList);

  long findAllNodesCount();

  FolderServerArtifact findArtifactById(CedarArtifactId artifactId);

  FolderServerSchemaArtifact findSchemaArtifactById(CedarSchemaArtifactId artifactId);

  FileSystemResource findResourceById(CedarFilesystemResourceId resourceId);

  FolderServerFolder createFolderAsChildOfId(FolderServerFolder newFolder, CedarFolderId parentFolderId);

  FolderServerArtifact createResourceAsChildOfId(FolderServerArtifact newResource, CedarFolderId parentFolderId);

  FolderServerFolder updateFolderById(CedarFolderId folderId, Map<NodeProperty, String> updateFields);

  FolderServerArtifact updateArtifactById(CedarArtifactId artifactId, CedarResourceType resourceType, Map<NodeProperty, String> updateFields);

  boolean deleteFolderById(CedarFolderId folderId);

  boolean deleteResourceById(CedarArtifactId resourceId);

  FolderServerFolder findFolderByPath(String path);

  FileSystemResource findFilesystemResourceByParentFolderIdAndName(CedarFolderId parentFolderId, String name);

  List<FolderServerFolder> findFolderPath(FolderServerFolder folder);

  List<FolderServerResourceExtract> findNodePathExtract(FileSystemResource node);

  List<FileSystemResource> findFolderContentsFiltered(CedarFolderId folderId, List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                                      ResourcePublicationStatusFilter publicationStatus, int limit, int offset,
                                                      List<String> sortList);

  List<FolderServerResourceExtract> findFolderContentsExtractFiltered(CedarFolderId folderId, List<CedarResourceType> resourceTypeList,
                                                                      ResourceVersionFilter version,
                                                                      ResourcePublicationStatusFilter publicationStatus, int limit, int offset,
                                                                      List<String> sortList);

  List<FolderServerResourceExtract> findFolderContentsExtract(CedarFolderId folderId, List<CedarResourceType> resourceTypeList,
                                                              ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus,
                                                              int limit, int offset, List<String> sortList);

  long findFolderContentsFilteredCount(CedarFolderId folderId, List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                       ResourcePublicationStatusFilter publicationStatus);

  long findFolderContentsCount(CedarFolderId folderId, List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                               ResourcePublicationStatusFilter publicationStatus);

  long findFolderContentsUnfilteredCount(CedarFolderId folderId);

  void addPathAndParentId(FolderServerFolder folder);

  void addPathAndParentId(FolderServerArtifact resource);

  boolean moveResource(CedarArtifactId sourceArtifactId, CedarFolderId targetFolderId);

  boolean moveFolder(CedarFolderId sourceFolderId, CedarFolderId targetFolderId);

  FolderServerFolder ensureUserHomeExists();

  List<FolderServerResourceExtract> viewSharedWithMe(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                                     ResourcePublicationStatusFilter publicationStatus, int limit, int offset, List<String> sortList);

  List<FolderServerResourceExtract> viewSharedWithEverybody(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                                            ResourcePublicationStatusFilter publicationStatus, int limit, int offset,
                                                            List<String> sortList);

  long viewSharedWithMeCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                             ResourcePublicationStatusFilter publicationStatus);

  long viewSharedWithEverybodyCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                    ResourcePublicationStatusFilter publicationStatus);

  List<FolderServerResourceExtract> viewAll(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                            ResourcePublicationStatusFilter publicationStatus, int limit, int offset, List<String> sortList);

  long viewAllCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus);

  List<FileSystemResource> findAllDescendantNodesById(CedarFolderId id);

  List<FileSystemResource> findAllChildArtifactsOfFolder(CedarFolderId id);

  List<FileSystemResource> findAllNodesVisibleByGroupId(CedarGroupId id);

  FolderServerFolder findHomeFolderOf();

  FolderServerFolder createUserHomeFolder();

  boolean setDerivedFrom(CedarArtifactId newId, CedarArtifactId oldId);

  boolean unsetLatestVersion(CedarSchemaArtifactId id);

  boolean setLatestVersion(CedarSchemaArtifactId id);

  boolean setLatestPublishedVersion(CedarSchemaArtifactId id);

  boolean unsetLatestPublishedVersion(CedarSchemaArtifactId id);

  boolean unsetLatestDraftVersion(CedarSchemaArtifactId id);

  boolean setOpen(CedarArtifactId id);

  boolean setNotOpen(CedarArtifactId id);

  long getNumberOfInstances(CedarTemplateId templateId);

  FolderServerArtifactExtract findResourceExtractById(CedarArtifactId id);

  List<FolderServerArtifactExtract> getVersionHistory(CedarSchemaArtifactId id);

  List<FolderServerArtifactExtract> getVersionHistoryWithPermission(CedarSchemaArtifactId id);

  List<FolderServerResourceExtract> searchIsBasedOn(List<CedarResourceType> resourceTypeList, CedarTemplateId isBasedOn, int limit, int offset,
                                                    List<String> sortList);

  long searchIsBasedOnCount(List<CedarResourceType> resourceTypeList, CedarTemplateId isBasedOn);

  CedarResourceType getResourceType(CedarResourceId resourceId);
}
