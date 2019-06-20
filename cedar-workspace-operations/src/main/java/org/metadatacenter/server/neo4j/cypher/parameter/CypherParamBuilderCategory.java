package org.metadatacenter.server.neo4j.cypher.parameter;

import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.server.neo4j.parameter.ParameterPlaceholder;

import java.time.Instant;

public class CypherParamBuilderCategory extends AbstractCypherParamBuilder {

  public static CypherParameters createCategory(String newCategoryId, String parentCategoryId, String categoryName,
                                                String categoryDescription, String userId) {
    Instant now = Instant.now();
    String nowString = CedarConstants.xsdDateTimeFormatter.format(now);
    Long nowTS = now.getEpochSecond();
    CypherParameters params = new CypherParameters();
    // BaseDataGroup
    params.put(NodeProperty.ID, newCategoryId);
    params.put(NodeProperty.RESOURCE_TYPE, CedarResourceType.CATEGORY.getValue());
    params.put(NodeProperty.CREATED_ON, nowString);
    params.put(NodeProperty.LAST_UPDATED_ON, nowString);
    // TimestampDataGroup
    params.put(NodeProperty.CREATED_ON_TS, nowTS);
    params.put(NodeProperty.LAST_UPDATED_ON_TS, nowTS);
    // NameDescriptionIdentifierGroup
    params.put(NodeProperty.NAME, categoryName);
    params.put(NodeProperty.DESCRIPTION, categoryDescription);
    // UsersDataGroup
    params.put(NodeProperty.CREATED_BY, userId);
    params.put(NodeProperty.LAST_UPDATED_BY, userId);
    params.put(NodeProperty.OWNED_BY, userId);
    //
    params.put(NodeProperty.PARENT_CATEGORY_ID, parentCategoryId);
    params.put(ParameterPlaceholder.USER_ID, userId);
    return params;
  }

  public static CypherParameters getCategoryByNameAndParent(String name, String parentId) {
    CypherParameters params = new CypherParameters();
    params.put(NodeProperty.NAME, name);
    params.put(NodeProperty.PARENT_CATEGORY_ID, parentId);
    return params;
  }

  public static CypherParameters getAllCategories(int limit, int offset) {
    CypherParameters params = new CypherParameters();
    params.put(ParameterPlaceholder.LIMIT, limit);
    params.put(ParameterPlaceholder.OFFSET, offset);
    return params;
  }
}
