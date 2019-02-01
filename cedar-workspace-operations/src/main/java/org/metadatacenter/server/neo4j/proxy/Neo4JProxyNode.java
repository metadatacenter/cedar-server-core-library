package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.model.CedarNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.basic.FolderServerResource;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.*;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderFolderContent;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderNode;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderResource;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.Collection;
import java.util.List;

import static org.metadatacenter.server.security.model.auth.CedarPermission.READ_NOT_READABLE_NODE;

public class Neo4JProxyNode extends AbstractNeo4JProxy {

  Neo4JProxyNode(Neo4JProxies proxies) {
    super(proxies);
  }

  long findFolderContentsFilteredCount(String folderId, List<CedarNodeType> nodeTypeList, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsFilteredCountQuery(version, publicationStatus,
        addPermissionConditions);
    CypherParameters params = CypherParamBuilderFolderContent.getFolderContentsFilteredCountParameters(folderId,
        nodeTypeList, version, publicationStatus, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  long findFolderContentsCount(String folderId, List<CedarNodeType> nodeTypeList, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    boolean addPermissionConditions = false;
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsFilteredCountQuery(version, publicationStatus,
        addPermissionConditions);
    CypherParameters params = CypherParamBuilderFolderContent.getFolderContentsFilteredCountParameters(folderId,
        nodeTypeList, version, publicationStatus, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  long findFolderContentsUnfilteredCount(String folderId) {
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsUnfilteredCountQuery();
    CypherParameters params = CypherParamBuilderFolder.matchId(folderId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  List<FolderServerNodeExtract> findAllNodes(int limit, int offset, List<String> sortList) {
    String cypher = CypherQueryBuilderNode.getAllNodesLookupQuery(sortList);
    CypherParameters params = CypherParamBuilderNode.getAllNodesLookupParameters(limit, offset);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerNodeExtract.class);
  }

  long findAllNodesCount() {
    String cypher = CypherQueryBuilderNode.getAllNodesCountQuery();
    CypherQuery q = new CypherQueryLiteral(cypher);
    return executeReadGetCount(q);
  }

  private <T extends CedarNode> List<T> findFolderContentsFilteredGeneric(String folderId, Collection<CedarNodeType>
      nodeTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                              offset, List<String> sortList,
                                                                          CedarUser cu, Class<T> klazz) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsFilteredLookupQuery(sortList, version,
        publicationStatus, addPermissionConditions);
    CypherParameters params = CypherParamBuilderFolderContent.getFolderContentsFilteredLookupParameters(folderId,
        nodeTypes, version, publicationStatus, limit, offset, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, klazz);
  }

  List<FolderServerNode> findFolderContentsFiltered(String folderId, Collection<CedarNodeType> nodeTypes,
                                                    ResourceVersionFilter version, ResourcePublicationStatusFilter
                                                        publicationStatus, int limit, int offset, List<String>
                                                        sortList, CedarUser cu) {
    return findFolderContentsFilteredGeneric(folderId, nodeTypes, version, publicationStatus, limit, offset,
        sortList, cu, FolderServerNode.class);
  }

  List<FolderServerNodeExtract> findFolderContentsExtractFiltered(String folderId, Collection<CedarNodeType>
      nodeTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                      offset, List<String> sortList, CedarUser cu) {
    return findFolderContentsFilteredGeneric(folderId, nodeTypes, version, publicationStatus, limit, offset,
        sortList, cu, FolderServerNodeExtract.class);
  }

  List<FolderServerNodeExtract> findFolderContentsExtract(String folderId, Collection<CedarNodeType>
      nodeTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                                      offset, List<String> sortList, CedarUser cu) {
    boolean addPermissionConditions = false;
    String cypher = CypherQueryBuilderFolderContent.getFolderContentsFilteredLookupQuery(sortList, version,
        publicationStatus, addPermissionConditions);
    CypherParameters params = CypherParamBuilderFolderContent.getFolderContentsFilteredLookupParameters(folderId,
        nodeTypes, version, publicationStatus, limit, offset, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerNodeExtract.class);
  }

  FolderServerNode findNodeByParentIdAndName(String parentId, String name) {
    String cypher = CypherQueryBuilderNode.getNodeByParentIdAndName();
    CypherParameters params = CypherParamBuilderNode.getNodeByParentIdAndName(parentId, name);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerNode.class);
  }

  void updateNodeOwner(String nodeURL, String userURL, FolderOrResource folderOrResource) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    if (user != null) {
      if (folderOrResource == FolderOrResource.FOLDER) {
        FolderServerFolder folder = proxies.folder().findFolderById(nodeURL);
        if (folder != null) {
          proxies.folder().updateOwner(folder, user);
        }
      } else {
        FolderServerResource resource = proxies.resource().findResourceById(nodeURL);
        if (resource != null) {
          proxies.resource().updateOwner(resource, user);
        }
      }
    }
  }

  public FolderServerUser getNodeOwner(String nodeURL) {
    String cypher = CypherQueryBuilderNode.getNodeOwner();
    CypherParameters params = CypherParamBuilderNode.matchNodeId(nodeURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerUser.class);
  }

  public List<FolderServerNodeExtract> viewSharedWithMeFiltered(List<CedarNodeType> nodeTypes, ResourceVersionFilter
      version, ResourcePublicationStatusFilter publicationStatus, int limit, int offset, List<String> sortList,
                                                                CedarUser cu) {
    String cypher = CypherQueryBuilderNode.getSharedWithMeLookupQuery(version, publicationStatus, sortList);
    CypherParameters params = CypherParamBuilderNode.getSharedWithMeLookupParameters(nodeTypes, version,
        publicationStatus, limit, offset, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerNodeExtract.class);
  }

  public long viewSharedWithMeFilteredCount(List<CedarNodeType> nodeTypes, ResourceVersionFilter version,
                                            ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    String cypher = CypherQueryBuilderNode.getSharedWithMeCountQuery(version, publicationStatus);
    CypherParameters params = CypherParamBuilderNode.getSharedWithMeCountParameters(nodeTypes, version,
        publicationStatus, cu.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public List<FolderServerNodeExtract> viewAllFiltered(List<CedarNodeType> nodeTypes, ResourceVersionFilter version,
                                                       ResourcePublicationStatusFilter publicationStatus, int limit, int
                                                           offset, List<String> sortList, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderNode.getAllLookupQuery(version, publicationStatus, sortList,
        addPermissionConditions);
    CypherParameters params = CypherParamBuilderNode.getAllLookupParameters(nodeTypes, version, publicationStatus,
        limit, offset, cu.getId(), addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerNodeExtract.class);
  }

  public long viewAllFilteredCount(List<CedarNodeType> nodeTypes, ResourceVersionFilter version,
                                   ResourcePublicationStatusFilter publicationStatus, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderNode.getAllCountQuery(version, publicationStatus, addPermissionConditions);
    CypherParameters params = CypherParamBuilderNode.getAllCountParameters(nodeTypes, version, publicationStatus, cu
        .getId(), addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public List<FolderServerNode> findAllDescendantNodesById(String id) {
    String cypher = CypherQueryBuilderNode.getAllDescendantNodes();
    CypherParameters params = CypherParamBuilderNode.getNodeById(id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerNode.class);
  }

  public List<FolderServerNode> findAllNodesVisibleByGroupId(String id) {
    String cypher = CypherQueryBuilderNode.getAllVisibleByGroupQuery();
    CypherParameters params = CypherParamBuilderGroup.matchGroupId(id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerNode.class);
  }

  private <T extends CedarNode> List<T> findNodePathGenericById(String id, Class<T> klazz) {
    String cypher = CypherQueryBuilderNode.getNodeLookupQueryById();
    CypherParameters params = CypherParamBuilderNode.getNodeLookupByIDParameters(proxies.pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, klazz);
  }

  List<FolderServerNodeExtract> findNodePathExtractById(String id) {
    return findNodePathGenericById(id, FolderServerNodeExtract.class);
  }

  public List<FolderServerNodeExtract> searchIsBasedOn(List<CedarNodeType> nodeTypes, String isBasedOn, int limit,
                                                       int offset, List<String> sortList, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderNode.getSearchIsBasedOnLookupQuery(sortList, addPermissionConditions);
    CypherParameters params = CypherParamBuilderNode.getSearchIsBasedOnLookupParameters(nodeTypes, isBasedOn, limit,
        offset, cu.getId(), addPermissionConditions);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerNodeExtract.class);
  }

  public long searchIsBasedOnCount(List<CedarNodeType> nodeTypes, String isBasedOn, CedarUser cu) {
    boolean addPermissionConditions = true;
    if (cu.has(READ_NOT_READABLE_NODE)) {
      addPermissionConditions = false;
    }
    String cypher = CypherQueryBuilderNode.getSearchIsBasedOnCountQuery(addPermissionConditions);
    CypherParameters params = CypherParamBuilderNode.getSearchIsBasedOnCountParameters(nodeTypes, isBasedOn,
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
}
