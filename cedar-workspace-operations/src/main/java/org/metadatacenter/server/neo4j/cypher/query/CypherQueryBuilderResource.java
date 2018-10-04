package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.folderserver.basic.FolderServerResource;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

import java.util.Map;

public class CypherQueryBuilderResource extends AbstractCypherQueryBuilder {

  public static String createResourceAsChildOfId(FolderServerResource newResource) {
    return createFSResourceAsChildOfId(newResource);
  }

  public static String updateResourceById(Map<NodeProperty, String> updateFields) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{<PROP.ID>}})");
    sb.append(buildSetter("resource", NodeProperty.LAST_UPDATED_BY));
    sb.append(buildSetter("resource", NodeProperty.LAST_UPDATED_ON));
    sb.append(buildSetter("resource", NodeProperty.LAST_UPDATED_ON_TS));
    for (NodeProperty property : updateFields.keySet()) {
      sb.append(buildSetter("resource", property));
    }
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String deleteResourceById() {
    return "" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{<PROP.ID>}})" +
        " DETACH DELETE resource";
  }

  public static String unlinkResourceFromParent() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER>)" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " MATCH (parent)-[relation:<REL.CONTAINS>]->(resource)" +
        " DELETE relation" +
        " RETURN resource";
  }

  public static String linkResourceUnderFolder() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{parentFolderId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " CREATE (parent)-[:<REL.CONTAINS>]->(resource)" +
        " RETURN resource";
  }

  public static String setResourceOwner() {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " CREATE (user)-[:<REL.OWNS>]->(resource)" +
        " SET resource.<PROP.OWNED_BY> = {userId}" +
        " RETURN resource";
  }

  public static String removeResourceOwner() {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " MATCH (user)-[relation:<REL.OWNS>]->(resource)" +
        " DELETE relation" +
        " SET resource.<PROP.OWNED_BY> = null" +
        " RETURN resource";
  }

  public static String getResourceById() {
    return "" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{<PROP.ID>}})" +
        " RETURN resource";
  }

  public static String getResourceLookupQueryById() {
    return "" +
        " MATCH (root:<LABEL.FOLDER> {<PROP.NAME>:{<PROP.NAME>}})," +
        " (current:<LABEL.RESOURCE> {<PROP.ID>:{<PROP.ID>}})," +
        " path=shortestPath((root)-[:<REL.CONTAINS>*]->(current))" +
        " RETURN path";
  }

  public static String createResourceWithoutParent(FolderServerResource newResource) {
    return "" +
        createFSResource(ALIAS_FOO, newResource) +
        " RETURN " + ALIAS_FOO;
  }

  public static String setDerivedFrom() {
    return "" +
        " MATCH (nr:<LABEL.RESOURCE> {<PROP.ID>:{sourceId}})" +
        " MATCH (or:<LABEL.RESOURCE> {<PROP.ID>:{targetId}})" +
        " CREATE (nr)-[:<REL.DERIVEDFROM>]->(or)" +
        " SET nr.<PROP.DERIVED_FROM> = {targetId}" +
        " RETURN nr";
  }

  public static String unsetLatestVersion() {
    return "" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " SET resource.<PROP.IS_LATEST_VERSION> = false" +
        " RETURN resource";
  }

  public static String setLatestVersion() {
    return "" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " SET resource.<PROP.IS_LATEST_VERSION> = true" +
        " RETURN resource";
  }

  public static String getIsBasedOnCount() {
    return "" +
        " MATCH (instance:<LABEL.INSTANCE> {<PROP.IS_BASED_ON>:{resourceId}}) " +
        " RETURN COUNT(instance)";
  }

  public static String getVersionHistory() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})");
    sb.append(" MATCH p=(resnew:<LABEL.RESOURCE>)-[:<REL.PREVIOUSVERSION>*0..]->");
    sb.append("(resource)-[:<REL.PREVIOUSVERSION>*0..]->(resold:<LABEL.RESOURCE>)");
    sb.append(" RETURN p ORDER BY length(p) DESC LIMIT 1");
    return sb.toString();
  }

  public static String getVersionHistoryWithPermission() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})");
    sb.append(" MATCH p=(resnew:<LABEL.RESOURCE>)-[:<REL.PREVIOUSVERSION>*0..]->");
    sb.append("(resource)-[:<REL.PREVIOUSVERSION>*0..]->(resold:<LABEL.RESOURCE>)");
    sb.append(" WITH nodes(p) as ns, user");
    sb.append(" ORDER BY length(p) DESC LIMIT 1");
    sb.append(" RETURN FILTER(node in ns");
    sb.append(getResourcePermissionConditions(" WHERE ", "node"));
    sb.append(" )");
    return sb.toString();
  }
}
