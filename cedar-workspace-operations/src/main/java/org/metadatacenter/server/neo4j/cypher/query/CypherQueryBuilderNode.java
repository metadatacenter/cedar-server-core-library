package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.List;

public class CypherQueryBuilderNode extends AbstractCypherQueryBuilder {

  public static String getAllNodesLookupQuery(List<String> sortList) {
    return "" +
        " MATCH (resource:<LABEL.FSNODE>)" +
        " RETURN resource" +
        " ORDER BY " + getOrderByExpression("resource", sortList) +
        " SKIP {offset}" +
        " LIMIT {limit}";
  }

  public static String getAllNodesCountQuery() {
    return "" +
        " MATCH (resource:<LABEL.FSNODE>)" +
        " RETURN count(resource)";
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
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId} })" +
        " MATCH (user)-[:<REL.OWNS>]->(resource)" +
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
            "(resource)" +
            " WHERE resource.<PROP.RESOURCE_TYPE> in {resourceTypeList}" +
            " AND NOT EXISTS(resource.<PROP.EVERYBODY_PERMISSION>)" +
            " AND resource.<PROP.OWNED_BY> <> {userId}" +
            " AND (resource.<PROP.IS_USER_HOME> IS NULL OR resource.<PROP.IS_USER_HOME> <> true) "
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "resource"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "resource"));
    }
    sb.append(" RETURN DISTINCT(resource)");
    sb.append(" ORDER BY resource.<PROP.NODE_SORT_ORDER>,");
    sb.append(getOrderByExpression("resource", sortList));
    sb.append(", resource.<PROP.VERSION> DESC");
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getSharedWithEverybodyLookupQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus, List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        " MATCH (resource:<LABEL.FSNODE>)" +
            " WHERE EXISTS(resource.<PROP.EVERYBODY_PERMISSION>) AND resource.<PROP.EVERYBODY_PERMISSION> IS NOT NULL" +
            " AND resource.<PROP.RESOURCE_TYPE> in {resourceTypeList}" +
            " AND resource.<PROP.OWNED_BY> <> {userId}" +
            " AND (resource.<PROP.IS_USER_HOME> IS NULL OR resource.<PROP.IS_USER_HOME> <> true) "
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "resource"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "resource"));
    }
    sb.append(" RETURN DISTINCT(resource)");
    sb.append(" ORDER BY resource.<PROP.NODE_SORT_ORDER>,");
    sb.append(getOrderByExpression("resource", sortList));
    sb.append(", resource.<PROP.VERSION> DESC");
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
            "(resource)" +
            " WHERE resource.<PROP.RESOURCE_TYPE> in {resourceTypeList}" +
            " AND NOT EXISTS(resource.<PROP.EVERYBODY_PERMISSION>)" +
            " AND resource.<PROP.OWNED_BY> <> {userId}" +
            " AND (resource.<PROP.IS_USER_HOME> IS NULL OR resource.<PROP.IS_USER_HOME> <> true) "
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "resource"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "resource"));
    }
    sb.append(
        " RETURN count(resource)"
    );
    return sb.toString();
  }

  public static String getSharedWithEverybodyCountQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus) {
    StringBuilder sb = new StringBuilder();
    sb.append(
        " MATCH (resource:<LABEL.FSNODE>)" +
            " WHERE EXISTS(resource.<PROP.EVERYBODY_PERMISSION>) AND resource.<PROP.EVERYBODY_PERMISSION> IS NOT NULL" +
            " AND resource.<PROP.RESOURCE_TYPE> in {resourceTypeList}" +
            " AND resource.<PROP.OWNED_BY> <> {userId}" +
            " AND (resource.<PROP.IS_USER_HOME> IS NULL OR resource.<PROP.IS_USER_HOME> <> true) "
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "resource"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "resource"));
    }
    sb.append(
        " RETURN count(resource)"
    );
    return sb.toString();
  }

  public static String getAllLookupQuery(ResourceVersionFilter version, ResourcePublicationStatusFilter
      publicationStatus, List<String> sortList, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    }
    sb.append(" MATCH (resource)");
    sb.append(" WHERE resource.<PROP.RESOURCE_TYPE> in {resourceTypeList}");
    sb.append(" AND (resource.<PROP.IS_USER_HOME> IS NULL OR resource.<PROP.IS_USER_HOME> <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "resource"));
    }
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "resource"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "resource"));
    }
    sb.append(" RETURN resource");
    sb.append(" ORDER BY resource.<PROP.NODE_SORT_ORDER>,").append(getOrderByExpression("resource", sortList));
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
    sb.append(" MATCH (resource)");
    sb.append(" WHERE resource.<PROP.RESOURCE_TYPE> in {resourceTypeList}");
    sb.append(" AND (resource.<PROP.IS_USER_HOME> IS NULL OR resource.<PROP.IS_USER_HOME> <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "resource"));
    }
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "resource"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "resource"));
    }
    sb.append(" RETURN count(resource)");
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
        " MATCH (resource)" +
        " WHERE" +
        " (" +
        " (group)-[:<REL.CANREAD>]->()-[:<REL.CONTAINS>*0..]->(resource)" +
        " OR " +
        " (group)-[:<REL.CANWRITE>]->()-[:<REL.CONTAINS>*0..]->(resource)" +
        " )" +
        " RETURN resource";
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
    sb.append(" MATCH (resource)");
    sb.append(" WHERE resource.<PROP.RESOURCE_TYPE> in {resourceTypeList}");
    sb.append(" AND (resource.<PROP.IS_BASED_ON> = {isBasedOn}) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "resource"));
    }
    sb.append(" RETURN resource");
    sb.append(" ORDER BY resource.<PROP.NODE_SORT_ORDER>,").append(getOrderByExpression("resource", sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getSearchIsBasedOnCountQuery(boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    }
    sb.append(" MATCH (resource)");
    sb.append(" WHERE resource.<PROP.RESOURCE_TYPE> in {resourceTypeList}");
    sb.append(" AND (resource.<PROP.IS_BASED_ON> = {isBasedOn}) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "resource"));
    }
    sb.append(" RETURN count(resource)");
    return sb.toString();
  }

  public static String setEverybodyPermission() {
    return "" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " SET resource.<PROP.EVERYBODY_PERMISSION> = {everybodyPermission}" +
        " RETURN resource";
  }

  public static String getNodeById() {
    return "" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{<PROP.ID>}})" +
        " RETURN resource";
  }

  public static String setNodeOwner() {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " CREATE (user)-[:<REL.OWNS>]->(resource)" +
        " SET resource.<PROP.OWNED_BY> = {userId}" +
        " RETURN resource";
  }

  public static String removeNodeOwner() {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " MATCH (user)-[relation:<REL.OWNS>]->(resource)" +
        " DELETE (relation)" +
        " SET resource.<PROP.OWNED_BY> = null" +
        " RETURN resource";
  }


}
