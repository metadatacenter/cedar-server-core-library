package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

import java.util.Map;

public class CypherQueryBuilderResource extends AbstractCypherQueryBuilder {

  public static String createResourceAsChildOfId(FolderServerResource newResource) {
    return createFSResourceAsChildOfId(newResource);
  }

  public static String updateResourceById(Map<NodeProperty, String> updateFields) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{id}})");
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
        " MATCH (resource:<LABEL.RESOURCE> {id:{id}})" +
        " DETACH DELETE resource";
  }

  public static String unlinkResourceFromParent() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER>)" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})" +
        " MATCH (parent)-[relation:<REL.CONTAINS>]->(resource)" +
        " DELETE relation" +
        " RETURN resource";
  }

  public static String linkResourceUnderFolder() {
    return "" +
        " MATCH (parent:<LABEL.FOLDER> {id:{parentFolderId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})" +
        " CREATE (parent)-[:<REL.CONTAINS>]->(resource)" +
        " RETURN resource";
  }

  public static String setResourceOwner() {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})" +
        " CREATE (user)-[:<REL.OWNS>]->(resource)" +
        " SET resource.<PROP.OWNED_BY> = {userId}" +
        " RETURN resource";
  }

  public static String removeResourceOwner() {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})" +
        " MATCH (user)-[relation:<REL.OWNS>]->(resource)" +
        " DELETE relation" +
        " SET resource.<PROP.OWNED_BY> = null" +
        " RETURN resource";
  }

  public static String getResourceById() {
    return "" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{id}})" +
        " RETURN resource";
  }

  public static String getResourceLookupQueryById() {
    return "" +
        " MATCH (root:<LABEL.FOLDER> {name:{name}})," +
        " (current:<LABEL.RESOURCE> {id:{id}})," +
        " path=shortestPath((root)-[:<REL.CONTAINS>*]->(current))" +
        " RETURN path";
  }

  public static String createResourceWithoutParent(FolderServerResource newResource) {
    return "" +
        createFSResource(ALIAS_FOO, newResource) +
        " RETURN " + ALIAS_FOO;
  }

  public static String setPreviousVersion() {
    return "" +
        " MATCH (nr:<LABEL.RESOURCE> {id:{sourceId}})" +
        " MATCH (or:<LABEL.RESOURCE> {id:{targetId}})" +
        " CREATE (nr)-[:<REL.PREVIOUSVERSION>]->(or)" +
        " SET nr.<PROP.PREVIOUS_VERSION> = {targetId}" +
        " RETURN nr";
  }

  public static String setDerivedFrom() {
    return "" +
        " MATCH (nr:<LABEL.RESOURCE> {id:{sourceId}})" +
        " MATCH (or:<LABEL.RESOURCE> {id:{targetId}})" +
        " CREATE (nr)-[:<REL.DERIVEDFROM>]->(or)" +
        " SET nr.<PROP.DERIVED_FROM> = {targetId}" +
        " RETURN nr";
  }
}
