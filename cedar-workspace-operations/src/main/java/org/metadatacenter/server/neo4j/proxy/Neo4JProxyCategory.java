package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderCategory;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderCategory;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

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
    //TODO: implement this
    return null;
  }
}
