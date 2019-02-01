package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.basic.FolderServerResource;
import org.metadatacenter.model.folderserver.extract.FolderServerFolderExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerResourceExtract;
import org.metadatacenter.server.FolderServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.neo4j.Neo4JFieldValues;
import org.metadatacenter.server.neo4j.Neo4jConfig;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.auth.NodePermission;
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
  public FolderServerResource createResourceAsChildOfId(FolderServerResource newResource, String parentFolderURL) {
    newResource.setCreatedByTotal(cu.getId());
    return proxies.resource().createResourceAsChildOfId(newResource, parentFolderURL);
  }

  @Override
  public FolderServerFolder updateFolderById(String folderURL, Map<NodeProperty, String> updateFields) {
    return proxies.folder().updateFolderById(folderURL, updateFields, cu.getId());
  }

  @Override
  public FolderServerResource updateResourceById(String resourceURL, CedarNodeType nodeType, Map<NodeProperty,
      String> updateFields) {
    return proxies.resource().updateResourceById(resourceURL, updateFields, cu.getId());
  }

  @Override
  public boolean deleteFolderById(String folderURL) {
    return proxies.folder().deleteFolderById(folderURL);
  }

  @Override
  public boolean deleteResourceById(String resourceURL, CedarNodeType nodeType) {
    return proxies.resource().deleteResourceById(resourceURL);
  }

  private void setPaths(FolderServerNode node, List<? extends FolderServerNode> path) {
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
  public void addPathAndParentId(FolderServerResource resource) {
    if (resource != null) {
      List<FolderServerNode> path = proxies.resource().findResourcePathById(resource.getId());
      if (path != null) {
        setPaths(resource, path);
      }
    }
  }

  private String getParentPathString(List<? extends FolderServerNode> path) {
    List<FolderServerNode> p = new ArrayList<>(path);
    if (path.size() > 0) {
      p.remove(p.size() - 1);
    } else {
      return null;
    }
    return getPathString(p);
  }

  private String getPathString(List<? extends FolderServerNode> path) {
    StringBuilder sb = new StringBuilder();
    boolean addSeparator = false;
    for (FolderServerNode node : path) {
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
  public long findFolderContentsFilteredCount(String folderURL, List<CedarNodeType> nodeTypeList,
                                              ResourceVersionFilter version, ResourcePublicationStatusFilter
                                                  publicationStatus) {
    return proxies.node().findFolderContentsFilteredCount(folderURL, nodeTypeList, version, publicationStatus, cu);
  }

  @Override
  public long findFolderContentsCount(String folderURL, List<CedarNodeType> nodeTypeList,
                                              ResourceVersionFilter version, ResourcePublicationStatusFilter
                                                  publicationStatus) {
    return proxies.node().findFolderContentsCount(folderURL, nodeTypeList, version, publicationStatus, cu);
  }

  @Override
  public long findFolderContentsUnfilteredCount(String folderURL) {
    return proxies.node().findFolderContentsUnfilteredCount(folderURL);
  }

  @Override
  public FolderServerResource findResourceById(String resourceURL) {
    return proxies.resource().findResourceById(resourceURL);
  }

  @Override
  public List<FolderServerNodeExtract> findAllNodes(int limit, int offset, List<String> sortList) {
    return proxies.node().findAllNodes(limit, offset, sortList);
  }

  @Override
  public long findAllNodesCount() {
    return proxies.node().findAllNodesCount();
  }

  @Override
  public List<FolderServerNode> findFolderContentsFiltered(String folderURL, List<CedarNodeType> nodeTypeList,
                                                           ResourceVersionFilter version,
                                                           ResourcePublicationStatusFilter publicationStatus, int
                                                               limit, int offset, List<String> sortList) {
    return proxies.node().findFolderContentsFiltered(folderURL, nodeTypeList, version, publicationStatus, limit,
        offset, sortList, cu);
  }

  @Override
  public List<FolderServerNodeExtract> findFolderContentsExtractFiltered(String folderURL, List<CedarNodeType>
      nodeTypeList, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                             offset, List<String> sortList) {
    return proxies.node().findFolderContentsExtractFiltered(folderURL, nodeTypeList, version, publicationStatus, limit,
        offset, sortList, cu);
  }

  @Override
  public List<FolderServerNodeExtract> findFolderContentsExtract(String folderURL, List<CedarNodeType>
      nodeTypeList, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                             offset, List<String> sortList) {
    return proxies.node().findFolderContentsExtract(folderURL, nodeTypeList, version, publicationStatus, limit,
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
  public FolderServerNode findNodeByParentIdAndName(FolderServerFolder parentFolder, String name) {
    return proxies.node().findNodeByParentIdAndName(parentFolder.getId(), name);
  }

  @Override
  public FolderServerFolder createFolderAsChildOfId(FolderServerFolder newFolder, String parentFolderURL) {
    newFolder.setCreatedByTotal(cu.getId());
    return proxies.folder().createFolderAsChildOfId(newFolder, parentFolderURL);
  }

  @Override
  public boolean moveResource(FolderServerResource sourceResource, FolderServerFolder targetFolder) {
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
  public List<FolderServerNodeExtract> findNodePathExtract(FolderServerNode node) {
    if (node instanceof FolderServerFolder && ((FolderServerFolder) node).isRoot()) {
      List<FolderServerNodeExtract> pathInfo = new ArrayList<>();
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
    }
    return currentUserHomeFolder;
  }

  @Override
  public List<FolderServerNodeExtract> viewSharedWithMe(List<CedarNodeType> nodeTypes, ResourceVersionFilter version,
                                                        ResourcePublicationStatusFilter publicationStatus, int limit,
                                                        int
                                                            offset, List<String> sortList) {
    return proxies.node().viewSharedWithMeFiltered(nodeTypes, version, publicationStatus, limit, offset, sortList, cu);
  }

  @Override
  public long viewSharedWithMeCount(List<CedarNodeType> nodeTypes, ResourceVersionFilter version,
                                    ResourcePublicationStatusFilter publicationStatus) {
    return proxies.node().viewSharedWithMeFilteredCount(nodeTypes, version, publicationStatus, cu);
  }

  @Override
  public List<FolderServerNodeExtract> viewAll(List<CedarNodeType> nodeTypes, ResourceVersionFilter version,
                                               ResourcePublicationStatusFilter publicationStatus, int limit, int offset,
                                               List<String> sortList) {
    return proxies.node().viewAllFiltered(nodeTypes, version, publicationStatus, limit, offset, sortList, cu);
  }

  @Override
  public long viewAllCount(List<CedarNodeType> nodeTypes, ResourceVersionFilter version,
                           ResourcePublicationStatusFilter publicationStatus) {
    return proxies.node().viewAllFilteredCount(nodeTypes, version, publicationStatus, cu);
  }

  @Override
  public List<FolderServerNode> findAllDescendantNodesById(String id) {
    return proxies.node().findAllDescendantNodesById(id);
  }

  @Override
  public List<FolderServerNode> findAllNodesVisibleByGroupId(String id) {
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
    if (currentUserHomeFolder != null) {
      FolderServerGroup everybody = proxies.group().findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
      if (everybody != null) {
        proxies.permission().addPermission(currentUserHomeFolder, everybody, NodePermission.READTHIS);
      }
    }
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
  public FolderServerResourceExtract findResourceExtractById(ResourceUri id) {
    return proxies.resource().findResourceExtractById(id);
  }

  @Override
  public List<FolderServerResourceExtract> getVersionHistory(String id) {
    return proxies.resource().getVersionHistory(id);
  }

  @Override
  public List<FolderServerResourceExtract> getVersionHistoryWithPermission(String id) {
    return proxies.resource().getVersionHistoryWithPermission(id, cu.getId());
  }

  @Override
  public List<FolderServerNodeExtract> searchIsBasedOn(List<CedarNodeType> nodeTypes, String isBasedOn, int limit,
                                                       int offset, List<String> sortList) {
    return proxies.node().searchIsBasedOn(nodeTypes, isBasedOn, limit, offset, sortList, cu);
  }

  @Override
  public long searchIsBasedOnCount(List<CedarNodeType> nodeTypes, String isBasedOn) {
    return proxies.node().searchIsBasedOnCount(nodeTypes, isBasedOn, cu);
  }

}
