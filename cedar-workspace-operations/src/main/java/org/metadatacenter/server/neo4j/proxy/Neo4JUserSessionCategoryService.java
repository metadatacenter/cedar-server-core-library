package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;
import org.metadatacenter.model.folderserver.recursive.FolderServerCategoryWithChildren;
import org.metadatacenter.server.CategoryServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.List;
import java.util.Map;

public class Neo4JUserSessionCategoryService extends AbstractNeo4JUserSession implements CategoryServiceSession {

  private Neo4JUserSessionCategoryService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu,
                                          String globalRequestId, String localRequestId) {
    super(cedarConfig, proxies, cu, globalRequestId, localRequestId);
  }

  public static CategoryServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser,
                                           String globalRequestId, String localRequestId) {
    return new Neo4JUserSessionCategoryService(cedarConfig, proxies, cedarUser, globalRequestId, localRequestId);
  }

  @Override
  public FolderServerCategory createCategory(String name, String description, String parentId) {
    String categoryId = linkedDataUtil.buildNewLinkedDataId(CedarResourceType.CATEGORY);
    return proxies.category().createCategory(categoryId, parentId, name, description, cu.getId());
  }

  @Override
  public FolderServerCategory getCategoryById(String categoryId) {
    return null;
  }

  @Override
  public FolderServerCategory updateCategoryById(String categoryId, Map<NodeProperty, String> updateFields) {
    return null;
  }

  @Override
  public boolean deleteCategoryById(String categoryId) {
    return false;
  }

  @Override
  public FolderServerCategory getRootCategory() {
    return null;
  }

  @Override
  public List<FolderServerCategory> getChildrenOf(String parentCategoryId, int limit, int offset) {
    return null;
  }

  @Override
  public List<FolderServerCategory> getAllCategories(int limit, int offset) {
    return null;
  }

  @Override
  public FolderServerCategoryWithChildren getCategoryTree() {
    return null;
  }

  @Override
  public Object getCategoryPermissions(String categoryId) {
    return null;
  }

  @Override
  public Object updateCategoryPermissions(String categoryId, Object permissions) {
    return null;
  }

  @Override
  public Object getCategoryDetails(String categoryId) {
    return null;
  }

  @Override
  public boolean attachCategoryToArtifact(String categoryId, String artifactId) {
    return false;
  }

  @Override
  public boolean detachCategoryFromArtifact(String categoryId, String artifactId) {
    return false;
  }

  @Override
  public FolderServerCategory getCategoryByNameAndParent(String name, String parentId) {
    return proxies.category().getCategoryByNameAndParent(name, parentId);
  }
}
