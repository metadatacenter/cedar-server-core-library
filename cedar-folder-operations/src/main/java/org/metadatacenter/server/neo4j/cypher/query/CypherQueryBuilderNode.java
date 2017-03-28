package org.metadatacenter.server.neo4j.cypher.query;

import java.util.List;

public class CypherQueryBuilderNode extends AbstractCypherQueryBuilder {

  public static String getAllNodesLookupQuery(List<String> sortList) {
    return "" +
        " MATCH (node:<LABEL.FSNODE>)" +
        " RETURN node" +
        " ORDER BY " + getOrderByExpression("node", sortList) +
        " SKIP {offset}" +
        " LIMIT {limit}";
  }

  public static String getAllNodesCountQuery() {
    return "" +
        " MATCH (node:<LABEL.FSNODE>)" +
        " RETURN count(node)";
  }

  public static String getNodeByParentIdAndName() {
    return "" +
        " MATCH (parent:<LABEL.FSNODE> {id:{id}})" +
        " MATCH (child)" +
        " MATCH (parent)-[:<REL.CONTAINS>]->(child)" +
        " WHERE child.<PROP.NAME> = {name}" +
        " RETURN child";
  }

  public static String getNodeOwner() {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (node:<LABEL.FSNODE> {id:{nodeId} })" +
        " MATCH (user)-[:<REL.OWNS>]->(node)" +
        " RETURN user";
  }

  public static String getSharedWithMeLookupQuery(List<String> sortList) {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})-" +
        "[:<REL.MEMBEROF>*0..1]->" +
        "()-" +
        "[:<REL.CANREAD>|:<REL.CANWRITE>]->" +
        "(node)" +
        " WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}" +
        " AND node.<PROP.OWNED_BY> <> {userId}" +
        " AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) " +
        " RETURN node" +
        " ORDER BY node.<PROP.NODE_SORT_ORDER>," + getOrderByExpression("node", sortList) +
        " SKIP {offset}" +
        " LIMIT {limit}";
  }

  public static String getSharedWithMeCountQuery() {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})-" +
        "[:<REL.MEMBEROF>*0..1]->" +
        "()-" +
        "[:<REL.CANREAD>|:<REL.CANWRITE>]->" +
        "(node)" +
        " WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}" +
        " AND node.<PROP.OWNED_BY> <> {userId}" +
        " AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) " +
        " RETURN count(node)";
  }

  public static String getAllLookupQuery(List<String> sortList, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (node)");
    sb.append(" WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}");
    sb.append(" AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "node"));
    }
    sb.append(" RETURN node");
    sb.append(" ORDER BY node.<PROP.NODE_SORT_ORDER>,").append(getOrderByExpression("node", sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getAllCountQuery(boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (node)");
    sb.append(" WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}");
    sb.append(" AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "node"));
    }
    sb.append(" RETURN count(node)");
    return sb.toString();
  }

  public static String getAllDescendantNodes() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER> {id:{id} })" +
        " MATCH (child)" +
        " MATCH (parent)-[:<REL.CONTAINS>*0..]->(child)" +
        " RETURN child";
  }

  public static String getAllVisibleByGroupQuery() {
    return "" +
        " MATCH (group:<LABEL.GROUP> {id:{groupId}})" +
        " MATCH (node)" +
        " WHERE" +
        " (" +
        " (group)-[:<REL.CANREADTHIS>]->(node)" +
        " OR " +
        " (group)-[:<REL.CANREAD>]->()-[:<REL.CONTAINS>*0..]->(node)" +
        " OR " +
        " (group)-[:<REL.CANWRITE]->()-[:<REL.CONTAINS>*0..]->(node)" +
        " )" +
        " RETURN node";
  }

}
