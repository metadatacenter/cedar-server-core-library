package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.id.CedarCategoryId;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.model.folderserver.extract.FolderServerCategoryExtractWithChildren;
import org.metadatacenter.server.CategoryServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedCategories;
import org.metadatacenter.server.security.model.auth.NodePermission;
import org.metadatacenter.server.security.model.auth.NodeSharePermission;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.ArrayList;
import java.util.HashMap;
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
  public FolderServerCategory createCategory(CedarCategoryId parentId, String name, String description,
                                             String identifier) {
    CedarCategoryId categoryId = linkedDataUtil.buildNewLinkedDataCategoryId();
    return proxies.category().createCategory(parentId, categoryId, name, description, identifier, cu.getId());
  }

  @Override
  public FolderServerCategory getCategoryById(CedarCategoryId categoryId) {
    return proxies.category().getCategoryById(categoryId);
  }

  @Override
  public long getCategoryCount() {
    return proxies.category().getCategoryCount();
  }

  @Override
  public FolderServerCategory updateCategoryById(CedarCategoryId categoryId, Map<NodeProperty, String> updateFields) {
    return proxies.category().updateCategoryById(categoryId, updateFields, cu.getId());
  }

  @Override
  public boolean deleteCategoryById(CedarCategoryId categoryId) {
    return proxies.category().deleteCategoryById(categoryId);
  }

  @Override
  public FolderServerCategory getRootCategory() {
    return proxies.category().getRootCategory();
  }

  @Override
  public List<FolderServerCategory> getChildrenOf(CedarCategoryId parentCategoryId, int limit, int offset) {
    return null;
  }

  @Override
  public List<FolderServerCategory> getAllCategories(int limit, int offset) {
    return proxies.category().getAllCategories(limit, offset);
  }

  @Override
  public FolderServerCategoryExtractWithChildren getCategoryTree() {
    List<FolderServerCategory> allCategories = getAllCategories(10000, 0);
    FolderServerCategory rootCategory = getRootCategory();

    //Map<String, FolderServerCategoryExtractWithChildren> categoryMap = new HashMap<>();
    Map<String, List<FolderServerCategoryExtractWithChildren>> categoryChildMap = new HashMap<>();
    for (FolderServerCategory category : allCategories) {
      FolderServerCategoryExtractWithChildren extract = FolderServerCategoryExtractWithChildren.fromCategory(category);
      //categoryMap.put(extract.getId(), extract);

      String parentId = extract.getParentCategoryId();
      if (categoryChildMap.get(parentId) == null) {
        categoryChildMap.put(parentId, new ArrayList<>());
      }
      categoryChildMap.get(parentId).add(extract);
    }

    FolderServerCategoryExtractWithChildren rootExtract =
        FolderServerCategoryExtractWithChildren.fromCategory(rootCategory);

    injectChildrenRecursively(rootExtract, categoryChildMap);

    return rootExtract;
  }

  private void injectChildrenRecursively(FolderServerCategoryExtractWithChildren extract, Map<String,
      List<FolderServerCategoryExtractWithChildren>> categoryChildMap) {
    String id = extract.getId();
    List<FolderServerCategoryExtractWithChildren> children = categoryChildMap.get(id);
    if (children != null) {
      List<FolderServerCategoryExtractWithChildren> extractChildren = extract.getChildren();
      extractChildren.addAll(children);
      for (FolderServerCategoryExtractWithChildren extractChild : extractChildren) {
        injectChildrenRecursively(extractChild, categoryChildMap);
      }
    }
  }

  @Override
  public Object getCategoryPermissions(CedarCategoryId categoryId) {
    return null;
  }

  @Override
  public Object updateCategoryPermissions(CedarCategoryId categoryId, Object permissions) {
    return null;
  }

  @Override
  public Object getCategoryDetails(CedarCategoryId categoryId) {
    return null;
  }

  @Override
  public boolean attachCategoryToArtifact(CedarCategoryId categoryId, String artifactId) {
    return proxies.category().attachCategoryToArtifact(categoryId, artifactId);
  }

  @Override
  public boolean detachCategoryFromArtifact(CedarCategoryId categoryId, String artifactId) {
    return proxies.category().detachCategoryFromArtifact(categoryId, artifactId);
  }

  @Override
  public FolderServerCategory getCategoryByParentAndName(CedarCategoryId parentId, String name) {
    return proxies.category().getCategoryByParentAndName(parentId, name);
  }

  @Override
  public CedarNodeMaterializedCategories getNodeMaterializedCategories(String artifactId) {
    FileSystemResource artifact = proxies.resource().findNodeById(artifactId);
    if (artifact != null) {
      List<FolderServerCategory> attachedCategories = getCategoryPaths(artifactId);
      CedarNodeMaterializedCategories categories = new CedarNodeMaterializedCategories(artifactId);
      for(FolderServerCategory category: attachedCategories) {
        categories.addCategory(category.getId());
      }
      return categories;
    } else {
      return null;
    }
  }

  private List<FolderServerCategory> getCategoryPaths(String artifactId) {
    return proxies.category().getCategoryPaths(artifactId);
  }

}
