package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

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
        " MATCH (parent:<LABEL.FSNODE> {<PROP.ID>:{<PROP.ID>}})" +
        " MATCH (child)" +
        " MATCH (parent)-[:<REL.CONTAINS>]->(child)" +
        " WHERE child.<PROP.NAME> = {<PROP.NAME>}" +
        " RETURN child";
  }

  public static String getNodeOwner() {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId} })" +
        " MATCH (user)-[:<REL.OWNS>]->(node)" +
        " RETURN user";
  }

  public static String getSharedWithMeLookupQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus, List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})-" +
            "[:<REL.MEMBEROF>*0..1]->" +
            "()-" +
            "[:<REL.CANREAD>|:<REL.CANWRITE>]->" +
            "(node)" +
            " WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}" +
            " AND NOT EXISTS(node.<PROP.EVERYBODY_PERMISSION>)" +
            " AND node.<PROP.OWNED_BY> <> {userId}" +
            " AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) "
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "node"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "node"));
    }
    sb.append(" RETURN DISTINCT(node)");
    sb.append(" ORDER BY node.<PROP.NODE_SORT_ORDER>,");
    sb.append(getOrderByExpression("node", sortList));
    sb.append(", node.<PROP.VERSION> DESC");
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getSharedWithEverybodyLookupQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus, List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        " MATCH (node:<LABEL.FSNODE>)" +
            " WHERE EXISTS(node.<PROP.EVERYBODY_PERMISSION>) AND node.<PROP.EVERYBODY_PERMISSION> IS NOT NULL" +
            " AND node.<PROP.NODE_TYPE> in {nodeTypeList}" +
            " AND node.<PROP.OWNED_BY> <> {userId}" +
            " AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) "
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "node"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "node"));
    }
    sb.append(" RETURN DISTINCT(node)");
    sb.append(" ORDER BY node.<PROP.NODE_SORT_ORDER>,");
    sb.append(getOrderByExpression("node", sortList));
    sb.append(", node.<PROP.VERSION> DESC");
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getSharedWithMeCountQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})-" +
            "[:<REL.MEMBEROF>*0..1]->" +
            "()-" +
            "[:<REL.CANREAD>|:<REL.CANWRITE>]->" +
            "(node)" +
            " WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}" +
            " AND NOT EXISTS(node.<PROP.EVERYBODY_PERMISSION>)" +
            " AND node.<PROP.OWNED_BY> <> {userId}" +
            " AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) "
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "node"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "node"));
    }
    sb.append(
        " RETURN count(node)"
    );
    return sb.toString();
  }

  public static String getSharedWithEverybodyCountQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        " MATCH (node:<LABEL.FSNODE>)" +
            " WHERE EXISTS(node.<PROP.EVERYBODY_PERMISSION>) AND node.<PROP.EVERYBODY_PERMISSION> IS NOT NULL" +
            " AND node.<PROP.NODE_TYPE> in {nodeTypeList}" +
            " AND node.<PROP.OWNED_BY> <> {userId}" +
            " AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) "
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "node"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "node"));
    }
    sb.append(
        " RETURN count(node)"
    );
    return sb.toString();
  }

  public static String getAllLookupQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus, List<String> sortList, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    }
    sb.append(" MATCH (node)");
    sb.append(" WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}");
    sb.append(" AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "node"));
    }
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "node"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "node"));
    }
    sb.append(" RETURN node");
    sb.append(" ORDER BY node.<PROP.NODE_SORT_ORDER>,").append(getOrderByExpression("node", sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getAllCountQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    }
    sb.append(" MATCH (node)");
    sb.append(" WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}");
    sb.append(" AND (node.<PROP.IS_USER_HOME> IS NULL OR node.<PROP.IS_USER_HOME> <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "node"));
    }
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "node"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "node"));
    }
    sb.append(" RETURN count(node)");
    return sb.toString();
  }

  public static String getAllDescendantNodes() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{<PROP.ID>} })" +
        " MATCH (child)" +
        " MATCH (parent)-[:<REL.CONTAINS>*0..]->(child)" +
        " RETURN child";
  }

  public static String getAllVisibleByGroupQuery() {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (node)" +
        " WHERE" +
        " (" +
        " (group)-[:<REL.CANREAD>]->()-[:<REL.CONTAINS>*0..]->(node)" +
        " OR " +
        " (group)-[:<REL.CANWRITE>]->()-[:<REL.CONTAINS>*0..]->(node)" +
        " )" +
        " RETURN node";
  }

  public static String getNodeLookupQueryById() {
    return "" +
        " MATCH (root:<LABEL.FOLDER> {<PROP.NAME>:{<PROP.NAME>}})," +
        " (current:<LABEL.FSNODE> {<PROP.ID>:{<PROP.ID>} })," +
        " path=shortestPath((root)-[:<REL.CONTAINS>*]->(current))" +
        " RETURN path";
  }

  public static String getSearchIsBasedOnLookupQuery(List<String> sortList, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    }
    sb.append(" MATCH (node)");
    sb.append(" WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}");
    sb.append(" AND (node.<PROP.IS_BASED_ON> = {isBasedOn}) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "node"));
    }
    sb.append(" RETURN node");
    sb.append(" ORDER BY node.<PROP.NODE_SORT_ORDER>,").append(getOrderByExpression("node", sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getSearchIsBasedOnCountQuery(boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    }
    sb.append(" MATCH (node)");
    sb.append(" WHERE node.<PROP.NODE_TYPE> in {nodeTypeList}");
    sb.append(" AND (node.<PROP.IS_BASED_ON> = {isBasedOn}) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "node"));
    }
    sb.append(" RETURN count(node)");
    return sb.toString();
  }

  public static String setEverybodyPermission() {
    return "" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " SET node.<PROP.EVERYBODY_PERMISSION> = {everybodyPermission}" +
        " RETURN node";
  }

  public static String getNodeById() {
    return "" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{<PROP.ID>}})" +
        " RETURN node";
  }

  public static String setNodeOwner() {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " CREATE (user)-[:<REL.OWNS>]->(node)" +
        " SET node.<PROP.OWNED_BY> = {userId}" +
        " RETURN node";
  }

  public static String removeNodeOwner() {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " MATCH (user)-[relation:<REL.OWNS>]->(node)" +
        " DELETE (relation)" +
        " SET node.<PROP.OWNED_BY> = null" +
        " RETURN node";
  }


}
