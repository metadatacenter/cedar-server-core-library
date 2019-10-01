package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarResource;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.model.folderserver.extract.FolderServerResourceExtract;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderFolder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderFolderContent;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderGroup;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderFolderContent;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderNode;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.Collection;
import java.util.List;

import static org.metadatacenter.server.security.model.auth.CedarPermission.READ_NOT_READABLE_NODE;

public class Neo4JProxyResource extends AbstractNeo4JProxy {

  Neo4JProxyResource(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
  }

  long findFolderContentsFilteredCount(String folderId, List<CedarResourceType> resourceTypeList, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsFilteredCountQuery(version, publicationStatus,
        addPermissionConditions);
    CypherParameters params = CypherParamBuilderFolderContent.getFolderContentsFilteredCountParameters(folderId,
        resourceTypeList, version, publicationStatus, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  long findFolderContentsCount(String folderId, List<CedarResourceType> resourceTypeList, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    boolean addPermissionConditions = false;
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsFilteredCountQuery(version, publicationStatus,
        addPermissionConditions);
    CypherParameters params = CypherParamBuilderFolderContent.getFolderContentsFilteredCountParameters(folderId,
        resourceTypeList, version, publicationStatus, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  long findFolderContentsUnfilteredCount(String folderId) {
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsUnfilteredCountQuery();
    CypherParameters params = CypherParamBuilderFolder.matchId(folderId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  List<FolderServerResourceExtract> findAllNodes(int limit, int offset, List<String> sortList) {
    String cypher = CypherQueryBuilderNode.getAllNodesLookupQuery(sortList);
    CypherParameters params = CypherParamBuilderNode.getAllNodesLookupParameters(limit, offset);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerResourceExtract.class);
  }

  long findAllNodesCount() {
    String cypher = CypherQueryBuilderNode.getAllNodesCountQuery();
    CypherQuery q = new CypherQueryLiteral(cypher);
    return executeReadGetCount(q);
  }

  private <T extends CedarResource> List<T> findFolderContentsFilteredGeneric(String folderId,
                                                                              Collection<CedarResourceType>
                                                                                  resourceTypes,
                                                                              ResourceVersionFilter version,
                                                                              ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                                  offset, List<String> sortList,
                                                                              CedarUser cu, Class<T> klazz) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsFilteredLookupQuery(sortList, version,
        publicationStatus, addPermissionConditions);
    CypherParameters params = CypherParamBuilderFolderContent.getFolderContentsFilteredLookupParameters(folderId,
        resourceTypes, version, publicationStatus, limit, offset, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, klazz);
  }

  List<FileSystemResource> findFolderContentsFiltered(String folderId, Collection<CedarResourceType> resourceTypes,
                                                      ResourceVersionFilter version, ResourcePublicationStatusFilter
                                                          publicationStatus, int limit, int offset, List<String>
                                                          sortList, CedarUser cu) {
    return findFolderContentsFilteredGeneric(folderId, resourceTypes, version, publicationStatus, limit, offset,
        sortList, cu, FileSystemResource.class);
  }

  List<FolderServerResourceExtract> findFolderContentsExtractFiltered(String folderId, Collection<CedarResourceType>
      resourceTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                          offset, List<String> sortList, CedarUser cu) {
    return findFolderContentsFilteredGeneric(folderId, resourceTypes, version, publicationStatus, limit, offset,
        sortList, cu, FolderServerResourceExtract.class);
  }

  List<FolderServerResourceExtract> findFolderContentsExtract(String folderId, Collection<CedarResourceType>
      resourceTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                  offset, List<String> sortList, CedarUser cu) {
    boolean addPermissionConditions = false;
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsFilteredLookupQuery(sortList, version,
        publicationStatus, addPermissionConditions);
    CypherParameters params = CypherParamBuilderFolderContent.getFolderContentsFilteredLookupParameters(folderId,
        resourceTypes, version, publicationStatus, limit, offset, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerResourceExtract.class);
  }

  FileSystemResource findNodeByParentIdAndName(String parentId, String name) {
    String cypher = CypherQueryBuilderNode.getNodeByParentIdAndName();
    CypherParameters params = CypherParamBuilderNode.getNodeByParentIdAndName(parentId, name);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FileSystemResource.class);
  }

  void updateNodeOwner(String nodeURL, String userURL) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      FileSystemResource node = proxies.resource().findNodeById(nodeURL);
      if (node != null) {
        proxies.resource().updateOwner(node, user);
      }
    }
  }

  public FolderServerUser getNodeOwner(String nodeURL) {
    String cypher = CypherQueryBuilderNode.getNodeOwner();
    CypherParameters params = CypherParamBuilderNode.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerUser.class);
  }

  public List<FolderServerResourceExtract> viewSharedWithMeFiltered(List<CedarResourceType> resourceTypes,
                                                                    ResourceVersionFilter version,
                                                                    ResourcePublicationStatusFilter publicationStatus
      , int limit, int offset, List<String> sortList, CedarUser cu) {
    String cypher = CypherQueryBuilderNode.getSharedWithMeLookupQuery(version, publicationStatus, sortList);
    CypherParameters params = CypherParamBuilderNode.getSharedWithMeLookupParameters(resourceTypes, version,
        publicationStatus, limit, offset, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerResourceExtract.class);
  }

  public List<FolderServerResourceExtract> viewSharedWithEverybodyFiltered(List<CedarResourceType> resourceTypes,
                                                                           ResourceVersionFilter version,
                                                                           ResourcePublicationStatusFilter publicationStatus,
                                                                           int limit, int offset, List<String> sortList,
                                                                           CedarUser cu) {
    String cypher = CypherQueryBuilderNode.getSharedWithEverybodyLookupQuery(version, publicationStatus, sortList);
    CypherParameters params = CypherParamBuilderNode.getSharedWithEverybodyLookupParameters(resourceTypes, version,
        publicationStatus, limit, offset, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerResourceExtract.class);
  }

  public long viewSharedWithMeFilteredCount(List<CedarResourceType> resourceTypes, ResourceVersionFilter version,
                                            ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    String cypher = CypherQueryBuilderNode.getSharedWithMeCountQuery(version, publicationStatus);
    CypherParameters params = CypherParamBuilderNode.getSharedWithMeCountParameters(resourceTypes, version,
        publicationStatus, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public long viewSharedWithEverybodyFilteredCount(List<CedarResourceType> resourceTypes, ResourceVersionFilter version,
                                                   ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    String cypher = CypherQueryBuilderNode.getSharedWithEverybodyCountQuery(version, publicationStatus);
    CypherParameters params = CypherParamBuilderNode.getSharedWithEverybodyCountParameters(resourceTypes, version,
        publicationStatus, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public List<FolderServerResourceExtract> viewAllFiltered(List<CedarResourceType> resourceTypes,
                                                           ResourceVersionFilter version,
                                                           ResourcePublicationStatusFilter publicationStatus,
                                                           int limit, int
                                                               offset, List<String> sortList, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderNode.getAllLookupQuery(version, publicationStatus, sortList,
        addPermissionConditions);
    CypherParameters params = CypherParamBuilderNode.getAllLookupParameters(resourceTypes, version, publicationStatus,
        limit, offset, cu.getId(), addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerResourceExtract.class);
  }

  public long viewAllFilteredCount(List<CedarResourceType> resourceTypes, ResourceVersionFilter version,
                                   ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderNode.getAllCountQuery(version, publicationStatus, addPermissionConditions);
    CypherParameters params = CypherParamBuilderNode.getAllCountParameters(resourceTypes, version, publicationStatus, cu
        .getId(), addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public List<FileSystemResource> findAllDescendantNodesById(String id) {
    String cypher = CypherQueryBuilderNode.getAllDescendantNodes();
    CypherParameters params = CypherParamBuilderNode.getNodeById(id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FileSystemResource.class);
  }

  public List<FileSystemResource> findAllChildArtifactsOfFolder(String id) {
    String cypher = CypherQueryBuilderNode.getAllChildArtifacts();
    CypherParameters params = CypherParamBuilderNode.getNodeById(id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FileSystemResource.class);
  }

  public List<FileSystemResource> findAllNodesVisibleByGroupId(String id) {
    String cypher = CypherQueryBuilderNode.getAllVisibleByGroupQuery();
    CypherParameters params = CypherParamBuilderGroup.matchGroupId(id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FileSystemResource.class);
  }

  private <T extends CedarResource> List<T> findNodePathGenericById(String id, Class<T> klazz) {
    String cypher = CypherQueryBuilderNode.getNodeLookupQueryById();
    CypherParameters params = CypherParamBuilderNode.getNodeLookupByIDParameters(proxies.pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, klazz);
  }

  List<FolderServerResourceExtract> findNodePathExtractById(String id) {
    return findNodePathGenericById(id, FolderServerResourceExtract.class);
  }

  public List<FolderServerResourceExtract> searchIsBasedOn(List<CedarResourceType> resourceTypes, String isBasedOn,
                                                           int limit,
                                                           int offset, List<String> sortList, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderNode.getSearchIsBasedOnLookupQuery(sortList, addPermissionConditions);
    CypherParameters params = CypherParamBuilderNode.getSearchIsBasedOnLookupParameters(resourceTypes, isBasedOn, limit,
        offset, cu.getId(), addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerResourceExtract.class);
  }

  public long searchIsBasedOnCount(List<CedarResourceType> resourceTypes, String isBasedOn, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderNode.getSearchIsBasedOnCountQuery(addPermissionConditions);
    CypherParameters params = CypherParamBuilderNode.getSearchIsBasedOnCountParameters(resourceTypes, isBasedOn,
        cu.getId(), addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public boolean setEverybodyPermission(String nodeId, NodeSharePermission everybodyPermission) {
    String cypher = CypherQueryBuilderNode.setEverybodyPermission();
    CypherParameters params = CypherParamBuilderNode.matchNodeIdAndEverybodyPermission(nodeId, everybodyPermission);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting everybodyPermission");
  }

  public FileSystemResource findNodeById(String nodeUUID) {
    String cypher = CypherQueryBuilderNode.getNodeById();
    CypherParameters params = CypherParamBuilderNode.getNodeById(nodeUUID);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FileSystemResource.class);
  }

  private boolean setOwner(FileSystemResource node, FolderServerUser user) {
    String cypher = CypherQueryBuilderNode.setNodeOwner();
    CypherParameters params = CypherParamBuilderNode.matchNodeAndUser(node.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting owner");
  }

  private boolean removeOwner(FileSystemResource node) {
    String cypher = CypherQueryBuilderNode.removeNodeOwner();
    CypherParameters params = CypherParamBuilderNode.matchNodeId(node.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing owner");
  }

  boolean updateOwner(FileSystemResource node, FolderServerUser user) {
    boolean removed = removeOwner(node);
    if (removed) {
      return setOwner(node, user);
    }
    return false;
  }

}
