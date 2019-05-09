package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerArtifact;
import org.metadatacenter.model.folderserver.extract.FolderServerFolderExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerResourceExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerArtifactExtract;
import org.metadatacenter.server.FolderServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.neo4j.Neo4jConfig;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;
import org.metadatacenter.util.CedarUserNameUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Neo4JUserSessionFolderService extends AbstractNeo4JUserSession implements FolderServiceSession {

  private Neo4JUserSessionFolderService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu,
                                        String globalRequestId, String localRequestId) {
    super(cedarConfig, proxies, cu, globalRequestId, localRequestId);
  }

  public static FolderServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser,
                                         String globalRequestId, String localRequestId) {
    return new Neo4JUserSessionFolderService(cedarConfig, proxies, cedarUser, globalRequestId, localRequestId);
  }

  @Override
  public FolderServerArtifact createResourceAsChildOfId(FolderServerArtifact newResource, String parentFolderURL) {
    newResource.setCreatedByTotal(cu.getId());
    return proxies.resource().createResourceAsChildOfId(newResource, parentFolderURL);
  }

  @Override
  public FolderServerFolder updateFolderById(String folderURL, Map<NodeProperty, String> updateFields) {
    return proxies.folder().updateFolderById(folderURL, updateFields, cu.getId());
  }

  @Override
  public FolderServerArtifact updateResourceById(String resourceURL, CedarResourceType resourceType, Map<NodeProperty,
      String> updateFields) {
    return proxies.resource().updateResourceById(resourceURL, updateFields, cu.getId());
  }

  @Override
  public boolean deleteFolderById(String folderURL) {
    return proxies.folder().deleteFolderById(folderURL);
  }

  @Override
  public boolean deleteResourceById(String resourceURL) {
    return proxies.resource().deleteResourceById(resourceURL);
  }

  private void setPaths(FileSystemResource node, List<? extends FileSystemResource> path) {
    node.setPath(getPathString(path));
    node.setParentPath(getParentPathString(path));
  }

  @Override
  public void addPathAndParentId(FolderServerFolder folder) {
    if (folder != null) {
      List<FolderServerFolder> path = findFolderPath(folder);
      if (path != null) {
        setPaths(folder, path);
      }
    }
  }

  @Override
  public void addPathAndParentId(FolderServerArtifact resource) {
    if (resource != null) {
      List<FileSystemResource> path = proxies.resource().findResourcePathById(resource.getId());
      if (path != null) {
        setPaths(resource, path);
      }
    }
  }

  private String getParentPathString(List<? extends FileSystemResource> path) {
    List<FileSystemResource> p = new ArrayList<>(path);
    if (path.size() > 0) {
      p.remove(p.size() - 1);
    } else {
      return null;
    }
    return getPathString(p);
  }

  private String getPathString(List<? extends FileSystemResource> path) {
    StringBuilder sb = new StringBuilder();
    boolean addSeparator = false;
    for (FileSystemResource node : path) {
      if (addSeparator) {
        sb.append(proxies.pathUtil.getSeparator());
      }
      if (node instanceof FolderServerFolder) {
        if (!((FolderServerFolder) node).isRoot()) {
          addSeparator = true;
        }
      }
      sb.append(node.getName());
    }
    return sb.length() == 0 ? null : sb.toString();
  }

  @Override
  public String sanitizeName(String name) {
    return proxies.pathUtil.sanitizeName(name);
  }

  @Override
  public long findFolderContentsFilteredCount(String folderURL, List<CedarResourceType> resourceTypeList,
                                              ResourceVersionFilter version,
                                              ResourcePublicationStatusFilter publicationStatus) {
    return proxies.node().findFolderContentsFilteredCount(folderURL, resourceTypeList, version, publicationStatus, cu);
  }

  @Override
  public long findFolderContentsCount(String folderURL, List<CedarResourceType> resourceTypeList,
                                      ResourceVersionFilter version,
                                      ResourcePublicationStatusFilter publicationStatus) {
    return proxies.node().findFolderContentsCount(folderURL, resourceTypeList, version, publicationStatus, cu);
  }

  @Override
  public long findFolderContentsUnfilteredCount(String folderURL) {
    return proxies.node().findFolderContentsUnfilteredCount(folderURL);
  }

  @Override
  public FolderServerArtifact findResourceById(String resourceURL) {
    return proxies.resource().findResourceById(resourceURL);
  }

  @Override
  public FileSystemResource findNodeById(String nodeURL) {
    return proxies.node().findNodeById(nodeURL);
  }

  @Override
  public List<FolderServerResourceExtract> findAllNodes(int limit, int offset, List<String> sortList) {
    return proxies.node().findAllNodes(limit, offset, sortList);
  }

  @Override
  public long findAllNodesCount() {
    return proxies.node().findAllNodesCount();
  }

  @Override
  public List<FileSystemResource> findFolderContentsFiltered(String folderURL, List<CedarResourceType> resourceTypeList,
                                                             ResourceVersionFilter version,
                                                             ResourcePublicationStatusFilter publicationStatus, int
                                                               limit, int offset, List<String> sortList) {
    return proxies.node().findFolderContentsFiltered(folderURL, resourceTypeList, version, publicationStatus, limit,
        offset, sortList, cu);
  }

  @Override
  public List<FolderServerResourceExtract> findFolderContentsExtractFiltered(String folderURL, List<CedarResourceType>
      resourceTypeList, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                             offset, List<String> sortList) {
    return proxies.node().findFolderContentsExtractFiltered(folderURL, resourceTypeList, version, publicationStatus, limit,
        offset, sortList, cu);
  }

  @Override
  public List<FolderServerResourceExtract> findFolderContentsExtract(String folderURL, List<CedarResourceType>
      resourceTypeList, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                     offset, List<String> sortList) {
    return proxies.node().findFolderContentsExtract(folderURL, resourceTypeList, version, publicationStatus, limit,
        offset, sortList, cu);
  }

  @Override
  public String getRootPath() {
    return proxies.pathUtil.getRootPath();
  }

  @Override
  public FolderServerFolder findFolderById(String folderURL) {
    return proxies.folder().findFolderById(folderURL);
  }

  @Override
  public FolderServerFolder findFolderByPath(String path) {
    return proxies.folder().findFolderByPath(path);
  }

  @Override
  public FileSystemResource findNodeByParentIdAndName(FolderServerFolder parentFolder, String name) {
    return proxies.node().findNodeByParentIdAndName(parentFolder.getId(), name);
  }

  @Override
  public FolderServerFolder createFolderAsChildOfId(FolderServerFolder newFolder, String parentFolderURL) {
    newFolder.setCreatedByTotal(cu.getId());
    return proxies.folder().createFolderAsChildOfId(newFolder, parentFolderURL);
  }

  @Override
  public boolean moveResource(FolderServerArtifact sourceResource, FolderServerFolder targetFolder) {
    return proxies.resource().moveResource(sourceResource, targetFolder);
  }

  @Override
  public boolean moveFolder(FolderServerFolder sourceFolder, FolderServerFolder targetFolder) {
    return proxies.folder().moveFolder(sourceFolder, targetFolder);
  }

  @Override
  public String normalizePath(String path) {
    return proxies.pathUtil.normalizePath(path);
  }

  @Override
  public List<FolderServerFolder> findFolderPath(FolderServerFolder folder) {
    if (folder.isRoot()) {
      List<FolderServerFolder> pathInfo = new ArrayList<>();
      pathInfo.add(folder);
      return pathInfo;
    } else {
      return proxies.folder().findFolderPathById(folder.getId());
    }
  }

  @Override
  public List<FolderServerResourceExtract> findNodePathExtract(FileSystemResource node) {
    if (node instanceof FolderServerFolder && ((FolderServerFolder) node).isRoot()) {
      List<FolderServerResourceExtract> pathInfo = new ArrayList<>();
      pathInfo.add(FolderServerFolderExtract.fromFolder((FolderServerFolder) node));
      return pathInfo;
    } else {
      return proxies.node().findNodePathExtractById(node.getId());
    }
  }

  @Override
  public FolderServerFolder ensureUserHomeExists() {
    FolderServerFolder currentUserHomeFolder = findHomeFolderOf();
    if (currentUserHomeFolder == null) {
      currentUserHomeFolder = createUserHomeFolder();
      cu.setHomeFolderId(currentUserHomeFolder.getId());
      proxies.user().updateUser(cu);
    }
    return currentUserHomeFolder;
  }

  @Override
  public List<FolderServerResourceExtract> viewSharedWithMe(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                                            ResourcePublicationStatusFilter publicationStatus, int limit,
                                                            int offset, List<String> sortList) {
    return proxies.node().viewSharedWithMeFiltered(resourceTypeList, version, publicationStatus, limit, offset, sortList, cu);
  }

  @Override
  public List<FolderServerResourceExtract> viewSharedWithEverybody(List<CedarResourceType> resourceTypeList,
                                                                   ResourceVersionFilter version,
                                                                   ResourcePublicationStatusFilter publicationStatus,
                                                                   int limit, int offset, List<String> sortList) {
    return proxies.node()
        .viewSharedWithEverybodyFiltered(resourceTypeList, version, publicationStatus, limit, offset, sortList, cu);
  }

  @Override
  public long viewSharedWithMeCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                    ResourcePublicationStatusFilter publicationStatus) {
    return proxies.node().viewSharedWithMeFilteredCount(resourceTypeList, version, publicationStatus, cu);
  }

  @Override
  public long viewSharedWithEverybodyCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                           ResourcePublicationStatusFilter publicationStatus) {
    return proxies.node().viewSharedWithEverybodyFilteredCount(resourceTypeList, version, publicationStatus, cu);
  }

  @Override
  public List<FolderServerResourceExtract> viewAll(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                                                   ResourcePublicationStatusFilter publicationStatus, int limit, int offset,
                                                   List<String> sortList) {
    return proxies.node().viewAllFiltered(resourceTypeList, version, publicationStatus, limit, offset, sortList, cu);
  }

  @Override
  public long viewAllCount(List<CedarResourceType> resourceTypeList, ResourceVersionFilter version,
                           ResourcePublicationStatusFilter publicationStatus) {
    return proxies.node().viewAllFilteredCount(resourceTypeList, version, publicationStatus, cu);
  }

  @Override
  public List<FileSystemResource> findAllDescendantNodesById(String id) {
    return proxies.node().findAllDescendantNodesById(id);
  }

  @Override
  public List<FileSystemResource> findAllNodesVisibleByGroupId(String id) {
    return proxies.node().findAllNodesVisibleByGroupId(id);
  }

  @Override
  public FolderServerFolder findHomeFolderOf() {
    return proxies.folder().findHomeFolderOf(cu.getId());
  }

  @Override
  public FolderServerFolder createUserHomeFolder() {
    String userId = cu.getId();
    Neo4jConfig config = proxies.config;
    FolderServerFolder currentUserHomeFolder;
    FolderServerFolder usersFolder = findFolderByPath(config.getUsersFolderPath());
    // usersFolder should not be null at this point. If it is, we let the NPE to be thrown
    String name = CedarUserNameUtil.getDisplayName(cedarConfig, cu);
    String description = CedarUserNameUtil.getHomeFolderDescription(cedarConfig, cu);
    FolderServerFolder newUserHome = new FolderServerFolder();
    newUserHome.setName(name);
    newUserHome.setDescription(description);
    newUserHome.setRoot(false);
    newUserHome.setSystem(false);
    newUserHome.setUserHome(true);
    newUserHome.setCreatedByTotal(userId);
    newUserHome.setHomeOf(userId);
    currentUserHomeFolder = createFolderAsChildOfId(newUserHome, usersFolder.getId());
    return currentUserHomeFolder;
  }

  @Override
  public boolean setDerivedFrom(String newId, String oldId) {
    return proxies.resource().setDerivedFrom(newId, oldId);
  }

  @Override
  public boolean unsetLatestVersion(String id) {
    return proxies.resource().unsetLatestVersion(id);
  }

  @Override
  public boolean setLatestVersion(String id) {
    return proxies.resource().setLatestVersion(id);
  }

  @Override
  public boolean setLatestPublishedVersion(String id) {
    return proxies.resource().setLatestPublishedVersion(id);
  }

  @Override
  public boolean unsetLatestPublishedVersion(String id) {
    return proxies.resource().unsetLatestPublishedVersion(id);
  }

  @Override
  public boolean unsetLatestDraftVersion(String id) {
    return proxies.resource().unsetLatestDraftVersion(id);
  }

  @Override
  public boolean setOpen(String id) {
    return proxies.resource().setOpen(id);
  }

  @Override
  public boolean setNotOpen(String id) {
    return proxies.resource().setNotOpen(id);
  }

  @Override
  public long getNumberOfInstances(String templateId) {
    return proxies.resource().getIsBasedOnCount(templateId);
  }

  @Override
  public FolderServerArtifactExtract findResourceExtractById(ResourceUri id) {
    return proxies.resource().findResourceExtractById(id);
  }

  @Override
  public List<FolderServerArtifactExtract> getVersionHistory(String id) {
    return proxies.resource().getVersionHistory(id);
  }

  @Override
  public List<FolderServerArtifactExtract> getVersionHistoryWithPermission(String id) {
    return proxies.resource().getVersionHistoryWithPermission(id, cu.getId());
  }

  @Override
  public List<FolderServerResourceExtract> searchIsBasedOn(List<CedarResourceType> resourceTypeList, String isBasedOn, int limit,
                                                           int offset, List<String> sortList) {
    return proxies.node().searchIsBasedOn(resourceTypeList, isBasedOn, limit, offset, sortList, cu);
  }

  @Override
  public long searchIsBasedOnCount(List<CedarResourceType> resourceTypeList, String isBasedOn) {
    return proxies.node().searchIsBasedOnCount(resourceTypeList, isBasedOn, cu);
  }

}
