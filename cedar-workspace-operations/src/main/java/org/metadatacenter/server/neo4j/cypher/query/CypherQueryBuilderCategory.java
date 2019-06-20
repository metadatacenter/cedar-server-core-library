package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public class CypherQueryBuilderCategory extends AbstractCypherQueryBuilder {

  public static String getRootCategory() {
    return "" +
        " MATCH (category:<LABEL.CATEGORY>) WHERE category.<PROP.PARENT_CATEGORY_ID> IS NULL" +
        " RETURN category";
  }

  public static String createCategory(String parentCategoryId, String userId) {
    StringBuilder sb = new StringBuilder();
    sb.append(" CREATE (category:<COMPOSEDLABEL.CATEGORY> {");
    // BaseDataGroup
    sb.append(buildCreateAssignment(NodeProperty.ID)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.RESOURCE_TYPE)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON)).append(",");
    // TimestampDataGroup
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON_TS)).append(",");
    // NameDescriptionIdentifierGroup
    sb.append(buildCreateAssignment(NodeProperty.NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DESCRIPTION)).append(",");
    // UsersDataGroup
    sb.append(buildCreateAssignment(NodeProperty.CREATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.OWNED_BY)).append(",");
    //
    sb.append(buildCreateAssignment(NodeProperty.PARENT_CATEGORY_ID));
    sb.append("})");

    if (parentCategoryId != null) {
      sb.append(" WITH category");
      sb.append(" MATCH (parent:<LABEL.CATEGORY> {<PROP.ID>:{parentCategoryId}})");
      sb.append(" MERGE (parent)-[:<REL.CONTAINSCATEGORY>]->(category)");
    }

    if (userId != null) {
      sb.append(" WITH category");
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
      sb.append(" MERGE (user)-[:<REL.OWNS>]->(category)");
    }

    sb.append(" RETURN category");
    return sb.toString();
  }

}
