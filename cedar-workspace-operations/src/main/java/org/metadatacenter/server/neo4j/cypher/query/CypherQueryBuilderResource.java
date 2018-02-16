package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.Map;

public class CypherQueryBuilderResource extends AbstractCypherQueryBuilder {

  public static String createResourceAsChildOfId(NodeLabel label) {
    return createFSResourceAsChildOfId(label);
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


}
