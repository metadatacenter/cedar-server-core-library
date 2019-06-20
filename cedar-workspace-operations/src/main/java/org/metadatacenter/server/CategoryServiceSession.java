package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.basic.FolderServerCategory;
import org.metadatacenter.model.folderserver.recursive.FolderServerCategoryWithChildren;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

import java.util.List;
import java.util.Map;

public interface CategoryServiceSession {

  FolderServerCategory createCategory(String name, String description, String parentId);

  FolderServerCategory getCategoryById(String categoryId);

  FolderServerCategory getCategoryByNameAndParent(String name, String parentId);

  List<FolderServerCategory> getAllCategories(int limit, int offset);

  long getCategoryCount();

  FolderServerCategory updateCategoryById(String categoryId, Map<NodeProperty, String> updateFields);

  boolean deleteCategoryById(String categoryId);

  //

  FolderServerCategory getRootCategory();

  List<FolderServerCategory> getChildrenOf(String parentCategoryId, int limit, int offset);

  FolderServerCategoryWithChildren getCategoryTree();

  Object getCategoryPermissions(String categoryId);

  Object updateCategoryPermissions(String categoryId, Object permissions);

  Object getCategoryDetails(String categoryId);

  boolean attachCategoryToArtifact(String categoryId, String artifactId);

  boolean detachCategoryFromArtifact(String categoryId, String artifactId);

}
