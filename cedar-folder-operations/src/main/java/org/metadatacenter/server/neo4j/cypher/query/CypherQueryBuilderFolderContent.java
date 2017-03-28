package org.metadatacenter.server.neo4j.cypher.query;

import java.util.List;

public class CypherQueryBuilderFolderContent extends AbstractCypherQueryBuilder {

  public static String getFolderContentsUnfilteredCountQuery() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER> {id:{id}})" +
        " MATCH (child)" +
        " MATCH (parent)-[:<REL.CONTAINS>]->(child)" +
        " RETURN count(child)";
  }

  public static String getFolderContentsFilteredCountQuery(boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (parent:<LABEL.FOLDER> {id:{folderId}})");
    sb.append(" MATCH (child)");
    sb.append(" MATCH (parent)-[:<REL.CONTAINS>]->(child)");
    sb.append(" WHERE child.<PROP.NODE_TYPE> in {nodeTypeList}");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "parent"));
      sb.append(getResourcePermissionConditions(" AND ", "child"));
    }
    sb.append(" RETURN count(child)");
    return sb.toString();
  }

  public static String getFolderContentsFilteredLookupQuery(List<String> sortList, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (parent:<LABEL.FOLDER> {id:{folderId}})");
    sb.append(" MATCH (child)");
    sb.append(" MATCH (parent)-[:<REL.CONTAINS>]->(child)");
    sb.append(" WHERE child.<PROP.NODE_TYPE> in {nodeTypeList}");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "parent"));
      sb.append(getResourcePermissionConditions(" AND ", "child"));
    }
    sb.append(" RETURN child");
    sb.append(" ORDER BY child.<PROP.NODE_SORT_ORDER>,").append(getOrderByExpression("child", sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }


}
