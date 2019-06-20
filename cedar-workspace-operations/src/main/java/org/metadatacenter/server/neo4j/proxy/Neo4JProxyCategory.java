package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderCategory;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderGroup;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderNode;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderCategory;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderGroup;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

import java.util.List;
import java.util.Map;

public class Neo4JProxyCategory extends AbstractNeo4JProxy {

  Neo4JProxyCategory(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
  }

  public FolderServerCategory getRootCategory() {
    String cypher = CypherQueryBuilderCategory.getRootCategory();
    CypherParameters params = new CypherParameters();
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerCategory.class);
  }

  public FolderServerCategory createCategory(String newCategoryId, String parentCategoryId, String categoryName,
                                             String categoryDescription, String userId) {
    String cypher = CypherQueryBuilderCategory.createCategory(parentCategoryId, userId);
    CypherParameters params = CypherParamBuilderCategory.createCategory(newCategoryId, parentCategoryId, categoryName,
        categoryDescription, userId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerCategory.class);
  }

  public FolderServerCategory getCategoryByNameAndParent(String name, String parentId) {
    String cypher = CypherQueryBuilderCategory.getCategoryByNameAndParent();
    CypherParameters params = CypherParamBuilderCategory.getCategoryByNameAndParent(name, parentId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerCategory.class);
  }

  public FolderServerCategory getCategoryById(String categoryId) {
    String cypher = CypherQueryBuilderCategory.getCategoryById();
    CypherParameters params = CypherParamBuilderNode.getNodeById(categoryId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetOne(q, FolderServerCategory.class);
  }

  public List<FolderServerCategory> getAllCategories(int limit, int offset) {
    String cypher = CypherQueryBuilderCategory.getAllCategories();
    CypherParameters params = CypherParamBuilderCategory.getAllCategories(limit, offset);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetList(q, FolderServerCategory.class);
  }

  public long getCategoryCount() {
    String cypher = CypherQueryBuilderCategory.getTotalCount();
    CypherParameters params = new CypherParameters();
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetCount(q);
  }

  public FolderServerCategory updateCategoryById(String categoryId, Map<NodeProperty, String> updateFields,
                                                 String updatedBy) {
    String cypher = CypherQueryBuilderGroup.updateCategoryById(updateFields);
    CypherParameters params = CypherParamBuilderGroup.updateCategoryById(categoryId, updateFields, updatedBy);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerCategory.class);
  }

  public boolean deleteCategoryById(String categoryId) {
    String cypher = CypherQueryBuilderCategory.deleteCategoryById();
    CypherParameters params = CypherParamBuilderCategory.deleteCategoryById(categoryId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "deleting category");
  }
}