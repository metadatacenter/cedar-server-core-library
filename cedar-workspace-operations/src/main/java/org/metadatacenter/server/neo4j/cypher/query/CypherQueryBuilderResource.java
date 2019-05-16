package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.folderserver.basic.FolderServerArtifact;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

import java.util.Map;

public class CypherQueryBuilderResource extends AbstractCypherQueryBuilder {

  public static String createResourceAsChildOfId(FolderServerArtifact newResource) {
    return createFSResourceAsChildOfId(newResource);
  }

  public static String updateResourceById(Map<NodeProperty, String> updateFields) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{<PROP.ID>}})");
    sb.append(buildSetter("artifact", NodeProperty.LAST_UPDATED_BY));
    sb.append(buildSetter("artifact", NodeProperty.LAST_UPDATED_ON));
    sb.append(buildSetter("artifact", NodeProperty.LAST_UPDATED_ON_TS));
    for (NodeProperty property : updateFields.keySet()) {
      sb.append(buildSetter("artifact", property));
    }
    sb.append(" RETURN artifact");
    return sb.toString();
  }

  public static String deleteResourceById() {
    return "" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{<PROP.ID>}})" +
        " DETACH DELETE artifact";
  }

  public static String unlinkResourceFromParent() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER>)" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " MATCH (parent)-[relation:<REL.CONTAINS>]->(artifact)" +
        " DELETE relation" +
        " RETURN artifact";
  }

  public static String linkResourceUnderFolder() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{parentFolderId}})" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " CREATE (parent)-[:<REL.CONTAINS>]->(artifact)" +
        " RETURN artifact";
  }

  public static String setResourceOwner() {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " CREATE (user)-[:<REL.OWNS>]->(artifact)" +
        " SET artifact.<PROP.OWNED_BY> = {userId}" +
        " RETURN artifact";
  }

  public static String removeResourceOwner() {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " MATCH (user)-[relation:<REL.OWNS>]->(artifact)" +
        " DELETE relation" +
        " SET artifact.<PROP.OWNED_BY> = null" +
        " RETURN artifact";
  }

  public static String getResourceLookupQueryById() {
    return "" +
        " MATCH (root:<LABEL.FOLDER> {<PROP.NAME>:{<PROP.NAME>}})," +
        " (current:<LABEL.RESOURCE> {<PROP.ID>:{<PROP.ID>}})," +
        " path=shortestPath((root)-[:<REL.CONTAINS>*]->(current))" +
        " RETURN path";
  }

  public static String createResourceWithoutParent(FolderServerArtifact newResource) {
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
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " SET artifact.<PROP.IS_LATEST_VERSION> = false" +
        " RETURN artifact";
  }

  public static String setLatestVersion() {
    return "" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " SET artifact.<PROP.IS_LATEST_VERSION> = true" +
        " RETURN artifact";
  }

  public static String unsetLatestDraftVersion() {
    return "" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " SET artifact.<PROP.IS_LATEST_DRAFT_VERSION> = false" +
        " RETURN artifact";
  }

  public static String setLatestPublishedVersion() {
    return "" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " SET artifact.<PROP.IS_LATEST_PUBLISHED_VERSION> = true" +
        " RETURN artifact";
  }

  public static String unsetLatestPublishedVersion() {
    return "" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " SET artifact.<PROP.IS_LATEST_PUBLISHED_VERSION> = false" +
        " RETURN artifact";
  }

  public static String getIsBasedOnCount() {
    return "" +
        " MATCH (instance:<LABEL.INSTANCE> {<PROP.IS_BASED_ON>:{resourceId}}) " +
        " RETURN COUNT(instance)";
  }

  public static String getVersionHistory() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})");
    sb.append(" MATCH p=(resnew:<LABEL.RESOURCE>)-[:<REL.PREVIOUSVERSION>*0..]->");
    sb.append("(artifact)-[:<REL.PREVIOUSVERSION>*0..]->(resold:<LABEL.RESOURCE>)");
    sb.append(" RETURN p ORDER BY length(p) DESC LIMIT 1");
    return sb.toString();
  }

  public static String getVersionHistoryWithPermission() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    sb.append(" MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})");
    sb.append(" MATCH p=(resnew:<LABEL.RESOURCE>)-[:<REL.PREVIOUSVERSION>*0..]->");
    sb.append("(artifact)-[:<REL.PREVIOUSVERSION>*0..]->(resold:<LABEL.RESOURCE>)");
    sb.append(" WITH nodes(p) as ns, user");
    sb.append(" ORDER BY length(p) DESC LIMIT 1");
    sb.append(" RETURN FILTER(resource in ns");
    sb.append(getResourcePermissionConditions(" WHERE ", "resource"));
    sb.append(" )");
    return sb.toString();
  }

  public static String setOpen() {
    return "" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " SET artifact.<PROP.IS_OPEN> = true" +
        " RETURN artifact";
  }

  public static String setNotOpen() {
    return "" +
        " MATCH (artifact:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " REMOVE artifact.<PROP.IS_OPEN>" +
        " RETURN artifact";
  }

}
