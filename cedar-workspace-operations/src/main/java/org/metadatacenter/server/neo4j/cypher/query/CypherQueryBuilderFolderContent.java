package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.List;

public class CypherQueryBuilderFolderContent extends AbstractCypherQueryBuilder {

  public static String getFolderContentsUnfilteredCountQuery() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{<PROP.ID>}})" +
        " MATCH (child)" +
        " MATCH (parent)-[:<REL.CONTAINS>]->(child)" +
        " RETURN count(child)";
  }

  public static String getFolderContentsFilteredCountQuery(ResourceVersionFilter version,
                                                           ResourcePublicationStatusFilter publicationStatus,
                                                           boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    }
    sb.append(" MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{folderId}})");
    sb.append(" MATCH (child)");
    sb.append(" MATCH (parent)-[:<REL.CONTAINS>]->(child)");
    sb.append(" WHERE child.<PROP.RESOURCE_TYPE> in {resourceTypeList}");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "parent"));
      sb.append(getResourcePermissionConditions(" AND ", "child"));
    }
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "child"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "child"));
    }
    sb.append(" RETURN count(child)");
    return sb.toString();
  }

  public static String getFolderContentsFilteredLookupQuery(List<String> sortList, ResourceVersionFilter version,
                                                            ResourcePublicationStatusFilter publicationStatus,
                                                            boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    }
    sb.append(" MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{folderId}})");
    sb.append(" MATCH (child)");
    sb.append(" MATCH (parent)-[:<REL.CONTAINS>]->(child)");
    sb.append(" WHERE child.<PROP.RESOURCE_TYPE> in {resourceTypeList}");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions(" AND ", "parent"));
      sb.append(getResourcePermissionConditions(" AND ", "child"));
    }
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "child"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "child"));
    }
    sb.append(" RETURN child");
    sb.append(" ORDER BY child.<PROP.NODE_SORT_ORDER>,");
    sb.append(getOrderByExpression("child", sortList));
    sb.append(", child.<PROP.VERSION> DESC");
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }


}
