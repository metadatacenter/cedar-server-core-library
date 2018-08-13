package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.model.CedarNode;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.model.folderserverextract.FolderServerNodeExtract;
import org.metadatacenter.model.folderserverextract.FolderServerResourceExtract;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderResource;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderResource;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

import java.util.List;
import java.util.Map;

public class Neo4JProxyResource extends AbstractNeo4JProxy {

  Neo4JProxyResource(Neo4JProxies proxies) {
    super(proxies);
  }

  FolderServerResource createResourceAsChildOfId(FolderServerResource newResource, String parentId) {
    String cypher = CypherQueryBuilderResource.createResourceAsChildOfId(newResource);
    CypherParameters params = CypherParamBuilderResource.createResource(newResource, parentId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerResource.class);
  }

  FolderServerResource updateResourceById(String resourceURL, Map<NodeProperty, String> updateFields, String
      updatedBy) {
    String cypher = CypherQueryBuilderResource.updateResourceById(updateFields);
    CypherParameters params = CypherParamBuilderResource.updateResourceById(resourceURL, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerResource.class);
  }

  boolean deleteResourceById(String resourceURL) {
    String cypher = CypherQueryBuilderResource.deleteResourceById();
    CypherParameters params = CypherParamBuilderResource.deleteResourceById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "deleting resource");
  }

  boolean moveResource(FolderServerResource sourceResource, FolderServerFolder targetFolder) {
    boolean unlink = unlinkResourceFromParent(sourceResource);
    if (unlink) {
      return linkResourceUnderFolder(sourceResource, targetFolder);
    }
    return false;
  }

  private boolean unlinkResourceFromParent(FolderServerResource resource) {
    String cypher = CypherQueryBuilderResource.unlinkResourceFromParent();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resource.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "unlinking resource");
  }

  private boolean linkResourceUnderFolder(FolderServerResource resource, FolderServerFolder parentFolder) {
    String cypher = CypherQueryBuilderResource.linkResourceUnderFolder();
    CypherParameters params = AbstractCypherParamBuilder.matchResourceIdAndParentFolderId(resource.getId(), parentFolder
        .getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "linking resource");
  }

  private boolean setOwner(FolderServerResource resource, FolderServerUser user) {
    String cypher = CypherQueryBuilderResource.setResourceOwner();
    CypherParameters params = AbstractCypherParamBuilder.matchResourceAndUser(resource.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting owner");
  }

  boolean updateOwner(FolderServerResource resource, FolderServerUser user) {
    boolean removed = removeOwner(resource);
    if (removed) {
      return setOwner(resource, user);
    }
    return false;
  }

  boolean removeOwner(FolderServerResource resource) {
    String cypher = CypherQueryBuilderResource.removeResourceOwner();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resource.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing owner");
  }

  private <T extends CedarNode> T findResourceGenericById(String id, Class<T> klazz) {
    String cypher = CypherQueryBuilderResource.getResourceById();
    CypherParameters params = CypherParamBuilderResource.getResourceById(id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, klazz);
  }

  public FolderServerResourceExtract findResourceExtractById(ResourceUri id) {
    return findResourceGenericById(id.getValue(), FolderServerResourceExtract.class);
  }

  public FolderServerResource findResourceById(String resourceURL) {
    return findResourceGenericById(resourceURL, FolderServerResource.class);
  }

  List<FolderServerNode> findResourcePathById(String id) {
    String cypher = CypherQueryBuilderResource.getResourceLookupQueryById();
    CypherParameters params = CypherParamBuilderNode.getNodeLookupByIDParameters(proxies.pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerNode.class);
  }

  public boolean setDerivedFrom(String newId, String oldId) {
    String cypher = CypherQueryBuilderResource.setDerivedFrom();
    CypherParameters params = CypherParamBuilderResource.matchSourceAndTarget(newId, oldId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting derivedFrom");
  }

  public boolean unsetLatestVersion(String resourceId) {
    String cypher = CypherQueryBuilderResource.unsetLatestVersion();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "unsetting isLatestVersion");
  }

  public boolean setLatestVersion(String resourceId) {
    String cypher = CypherQueryBuilderResource.setLatestVersion();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting isLatestVersion");
  }

  public long getIsBasedOnCount(String templateId) {
    String cypher = CypherQueryBuilderResource.getIsBasedOnCount();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(templateId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public List<FolderServerResourceExtract> getVersionHistory(String resourceId) {
    String cypher = CypherQueryBuilderResource.getVersionHistory();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerResourceExtract.class);
  }

  public List<FolderServerResourceExtract> getVersionHistoryWithPermission(String resourceId, String userURL) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    String cypher = CypherQueryBuilderResource.getVersionHistoryWithPermission();
    CypherParameters params = CypherParamBuilderResource.matchResourceIdAndUserId(resourceId, user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerResourceExtract.class);
  }
}
