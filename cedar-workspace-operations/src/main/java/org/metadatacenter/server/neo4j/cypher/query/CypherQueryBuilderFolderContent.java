package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;

import java.util.List;

public class CypherQueryBuilderFolderContent extends AbstractCypherQueryBuilder {

  public static String getFolderContentsUnfilteredCountQuery() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{<PROP.ID>}})" +
        " MATCH (parent)-[:<REL.CONTAINS>]->(child)" +
        " RETURN count(child)";
  }

  public static String getFolderContentsFilteredCountQuery(ResourceVersionFilter version,
                                                           ResourcePublicationStatusFilter publicationStatus,
                                                           boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(getFolderContentsFilteredLookupQueryWithPermission(version, publicationStatus));
    } else {
      sb.append(getFolderContentsFilteredLookupQueryWithoutPermission(version, publicationStatus));
    }
    sb.append("" +
        " RETURN count(finalChild)"
    );
    return sb.toString();
  }

  public static String getFolderContentsFilteredLookupQuery(List<String> sortList, ResourceVersionFilter version,
                                                            ResourcePublicationStatusFilter publicationStatus,
                                                            boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    if (addPermissionConditions) {
      sb.append(getFolderContentsFilteredLookupQueryWithPermission(version, publicationStatus));
    } else {
      sb.append(getFolderContentsFilteredLookupQueryWithoutPermission(version, publicationStatus));
    }
    sb.append("" +
        " RETURN finalChild" +
        " ORDER BY finalChild.<PROP.NODE_SORT_ORDER>," +
        getOrderByExpression("finalChild", sortList) +
        ", finalChild.<PROP.VERSION> DESC" +
        " SKIP {offset}" +
        " LIMIT {limit}"
    );
    return sb.toString();
  }

  private static StringBuilder getFolderContentsFilteredLookupQueryWithPermission(ResourceVersionFilter version,
                                                                                  ResourcePublicationStatusFilter publicationStatus) {
    StringBuilder sb = new StringBuilder();
    sb.append("" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{folderId}})" +
        " MATCH (superfolder)-[:CONTAINS*0..]->(parent)" +
        " WHERE" +
        "    (" +
        "        (user)-[:<REL.OWNS>]->(superfolder)" +
        "        OR" +
        "        (user)-[:<REL.MEMBEROF>*0..1]->()-[:<REL.CANREAD>|:<REL.CANWRITE>]->(superfolder)" +
        "        OR" +
        "        (user)-[:<REL.MEMBEROF>*0..1]->()-[:<REL.CANREADTHIS>]->(parent)" +
        "    )" +
        " WITH collect(parent) as p, user" +
        " UNWIND(p) as par" +
        "" +
        " OPTIONAL MATCH (par)-[:<REL.CONTAINS>]->(child)" +
        " WHERE" +
        "    (" +
        "        child.<PROP.NODE_TYPE> in {nodeTypeList}"
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "child"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "child"));
    }
    sb.append("" +
        "    )" +
        " WITH collect(child) as childrenInAccessibleFolder, user" +
        "" +
        " OPTIONAL MATCH (user)-[:<REL.MEMBEROF>*0..1]->()-[:<REL.CANREAD>|:<REL.CANWRITE>]->(directChild)" +
        " WHERE" +
        "    (" +
        "        directChild.<PROP.NODE_TYPE> in {nodeTypeList}"
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "directChild"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "directChild"));
    }
    sb.append("" +
        "    )" +
        " WITH childrenInAccessibleFolder + collect(directChild) as allChildren" +
        "" +
        " UNWIND allChildren as allChildrenList" +
        " WITH DISTINCT(allChildrenList) AS finalChild"
    );
    return sb;
  }

  private static StringBuilder getFolderContentsFilteredLookupQueryWithoutPermission(ResourceVersionFilter version,
                                                                                     ResourcePublicationStatusFilter publicationStatus) {
    StringBuilder sb = new StringBuilder();
    sb.append("" +
        " MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{folderId}})" +
        " MATCH (parent)-[:<REL.CONTAINS>]->(finalChild)" +
        " WHERE" +
        "    (" +
        "        finalChild.<PROP.NODE_TYPE> in {nodeTypeList}"
    );
    if (version != null && version != ResourceVersionFilter.ALL) {
      sb.append(getVersionConditions(version, " AND ", "finalChild"));
    }
    if (publicationStatus != null && publicationStatus != ResourcePublicationStatusFilter.ALL) {
      sb.append(getPublicationStatusConditions(" AND ", "finalChild"));
    }
    sb.append("" +
        "    )"
    );
    return sb;
  }


}
