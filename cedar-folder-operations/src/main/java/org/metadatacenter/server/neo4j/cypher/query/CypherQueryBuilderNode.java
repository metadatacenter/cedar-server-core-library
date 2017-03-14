package org.metadatacenter.server.neo4j.cypher.query;

import java.util.List;

public class CypherQueryBuilderNode extends AbstractCypherQueryBuilder {

  public static String getAllNodesLookupQuery(List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (node:<LABEL.FSNODE>)");
    sb.append(" RETURN node");
    sb.append(" ORDER BY ").append(getOrderByExpression("node", sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getNodeLookupQueryById() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (root:<LABEL.FOLDER> {name:{name}}),");
    sb.append(" (current {id:{id}}),");
    sb.append(" path=shortestPath((root)-[:<REL.CONTAINS>*]->(current))");
    sb.append(" RETURN path");
    return sb.toString();
  }

  public static String getAllNodesCountQuery() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (node:<LABEL.FSNODE>)");
    sb.append(" RETURN count(node)");
    return sb.toString();
  }

  public static String getNodeByParentIdAndName() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (parent:<LABEL.FSNODE> {id:{id}})");
    sb.append(" MATCH (child)");
    sb.append(" MATCH (parent)-[:<REL.CONTAINS>]->(child)");
    sb.append(" WHERE child.<PROP.NAME> = {name}");
    sb.append(" RETURN child");
    return sb.toString();
  }

  public static String getNodeOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (node:<LABEL.FSNODE> {id:{nodeId} })");
    sb.append(" MATCH (user)-[:<REL.OWNS>]->(node)");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getSharedWithMeLookupQuery(List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (node)");
    sb.append(" WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}");
    sb.append(" AND node.<PROP.OWNED_BY> <> {userId}");
    sb.append(" AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) ");
    sb.append(getSharedWithMeConditions(" AND ", "node"));
    sb.append(" RETURN node");
    sb.append(" ORDER BY node.<PROP.NODE_SORT_ORDER>,").append(getOrderByExpression("node", sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getSharedWithMeCountQuery() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (node)");
    sb.append(" WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}");
    sb.append(" AND node.<PROP.OWNED_BY>  <> {userId}");
    sb.append(" AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) ");
    sb.append(getSharedWithMeConditions(" AND ", "node"));
    sb.append(" RETURN count(node)");
    return sb.toString();
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
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (parent:<LABEL.FOLDER> {id:{id} })");
    sb.append(" MATCH (child)");
    sb.append(" MATCH (parent)-[:<REL>CONTAINS>*0..]->(child)");
    sb.append(" RETURN child");
    return sb.toString();
  }

  public static String getAllVisibleByUserQuery() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (node)");
    sb.append(" WHERE");
    sb.append(getResourcePermissionConditions(" ", "node"));
    sb.append(" RETURN node");
    return sb.toString();
  }

  public static String getAllVisibleByGroupQuery() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{groupId}})");
    sb.append(" MATCH (node)");
    sb.append(" WHERE");
    sb.append(" (");
    sb.append(" (group)-[:<REL.CANREADTHIS>]->(node)");
    sb.append(" OR ");
    sb.append(" (group)-[:<REL.CANREAD>]->()-[:<REL.CONTAINS>*0..]->(node)");
    sb.append(" OR ");
    sb.append(" (group)-[:<REL.CANWRITE]->()-[:<REL.CONTAINS>*0..]->(node)");
    sb.append(" )");
    sb.append(" RETURN node");
    return sb.toString();
  }

}
