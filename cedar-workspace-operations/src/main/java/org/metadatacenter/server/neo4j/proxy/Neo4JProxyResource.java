package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarResource;
import org.metadatacenter.model.ResourceUri;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerArtifact;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.model.folderserver.extract.FolderServerArtifactExtract;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderResource;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderNode;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderResource;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

import java.util.List;
import java.util.Map;

public class Neo4JProxyResource extends AbstractNeo4JProxy {

  Neo4JProxyResource(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
  }

  FolderServerArtifact createResourceAsChildOfId(FolderServerArtifact newResource, String parentId) {
    String cypher = CypherQueryBuilderResource.createResourceAsChildOfId(newResource);
    CypherParameters params = CypherParamBuilderResource.createResource(newResource, parentId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    FileSystemResource folderServerNode = executeWriteGetOne(q, FileSystemResource.class);
    return folderServerNode == null ? null : folderServerNode.asArtifact();
  }

  FolderServerArtifact updateResourceById(String resourceURL, Map<NodeProperty, String> updateFields, String
      updatedBy) {
    String cypher = CypherQueryBuilderResource.updateResourceById(updateFields);
    CypherParameters params = CypherParamBuilderResource.updateResourceById(resourceURL, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    FileSystemResource folderServerNode = executeWriteGetOne(q, FileSystemResource.class);
    return folderServerNode == null ? null : folderServerNode.asArtifact();
  }

  boolean deleteResourceById(String resourceURL) {
    String cypher = CypherQueryBuilderResource.deleteResourceById();
    CypherParameters params = CypherParamBuilderResource.deleteResourceById(resourceURL);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "deleting resource");
  }

  boolean moveResource(FolderServerArtifact sourceResource, FolderServerFolder targetFolder) {
    boolean unlink = unlinkResourceFromParent(sourceResource);
    if (unlink) {
      return linkResourceUnderFolder(sourceResource, targetFolder);
    }
    return false;
  }

  private boolean unlinkResourceFromParent(FolderServerArtifact resource) {
    String cypher = CypherQueryBuilderResource.unlinkResourceFromParent();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resource.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "unlinking resource");
  }

  private boolean linkResourceUnderFolder(FolderServerArtifact resource, FolderServerFolder parentFolder) {
    String cypher = CypherQueryBuilderResource.linkResourceUnderFolder();
    CypherParameters params = AbstractCypherParamBuilder.matchResourceIdAndParentFolderId(resource.getId(), parentFolder
        .getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "linking resource");
  }

  private boolean setOwner(FolderServerArtifact resource, FolderServerUser user) {
    String cypher = CypherQueryBuilderResource.setResourceOwner();
    CypherParameters params = AbstractCypherParamBuilder.matchResourceAndUser(resource.getId(), user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting owner");
  }

  boolean updateOwner(FolderServerArtifact resource, FolderServerUser user) {
    boolean removed = removeOwner(resource);
    if (removed) {
      return setOwner(resource, user);
    }
    return false;
  }

  boolean removeOwner(FolderServerArtifact resource) {
    String cypher = CypherQueryBuilderResource.removeResourceOwner();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resource.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "removing owner");
  }

  private <T extends CedarResource> T findResourceGenericById(String id, Class<T> klazz) {
    String cypher = CypherQueryBuilderNode.getNodeById();
    CypherParameters params = CypherParamBuilderNode.getNodeById(id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, klazz);
  }

  public FolderServerArtifactExtract findResourceExtractById(ResourceUri id) {
    return findResourceGenericById(id.getValue(), FolderServerArtifactExtract.class);
  }

  public FolderServerArtifact findResourceById(String resourceURL) {
    FileSystemResource folderServerNode = findResourceGenericById(resourceURL, FileSystemResource.class);
    return folderServerNode == null ? null : folderServerNode.asArtifact();
  }

  List<FileSystemResource> findResourcePathById(String id) {
    String cypher = CypherQueryBuilderResource.getResourceLookupQueryById();
    CypherParameters params = CypherParamBuilderNode.getNodeLookupByIDParameters(proxies.pathUtil, id);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FileSystemResource.class);
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

  public boolean unsetLatestDraftVersion(String resourceId) {
    String cypher = CypherQueryBuilderResource.unsetLatestDraftVersion();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "unsetting isLatestDraftVersion");
  }

  public boolean setLatestPublishedVersion(String resourceId) {
    String cypher = CypherQueryBuilderResource.setLatestPublishedVersion();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting isLatestPublishedVersion");
  }

  public boolean unsetLatestPublishedVersion(String resourceId) {
    String cypher = CypherQueryBuilderResource.unsetLatestPublishedVersion();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "unsetting isLatestPublishedVersion");
  }

  public long getIsBasedOnCount(String templateId) {
    String cypher = CypherQueryBuilderResource.getIsBasedOnCount();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(templateId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public List<FolderServerArtifactExtract> getVersionHistory(String resourceId) {
    String cypher = CypherQueryBuilderResource.getVersionHistory();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerArtifactExtract.class);
  }

  public List<FolderServerArtifactExtract> getVersionHistoryWithPermission(String resourceId, String userURL) {
    FolderServerUser user = proxies.user().findUserById(userURL);
    String cypher = CypherQueryBuilderResource.getVersionHistoryWithPermission();
    CypherParameters params = CypherParamBuilderResource.matchResourceIdAndUserId(resourceId, user.getId());
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerArtifactExtract.class);
  }

  public boolean setOpen(String resourceId) {
    String cypher = CypherQueryBuilderResource.setOpen();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting isOpen");
  }

  public boolean setNotOpen(String resourceId) {
    String cypher = CypherQueryBuilderResource.setNotOpen();
    CypherParameters params = CypherParamBuilderResource.matchResourceId(resourceId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "setting isOpen");
  }

}
