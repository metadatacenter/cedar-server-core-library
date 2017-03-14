package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.Map;

public class CypherQueryBuilderResource extends AbstractCypherQueryBuilder {

  public static String createResourceAsChildOfId(NodeLabel label, Map<NodeProperty, Object> extraProperties) {
    return createNodeAsChildOfId(label, extraProperties);
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
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{id}})");
    sb.append(" DETACH DELETE resource");
    return sb.toString();
  }

  public static String unlinkResourceFromParent() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (parent:<LABEL.FOLDER>)");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})");
    sb.append(" MATCH (parent)-[relation:<REL.CONTAINS>]->(resource)");
    sb.append(" DELETE relation");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String linkResourceUnderFolder() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (parent:<LABEL.FOLDER> {id:{parentFolderId}})");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})");
    sb.append(" CREATE (parent)-[:<REL.CONTAINS>]->(resource)");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String setResourceOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})");
    sb.append(" CREATE (user)-[:<REL.OWNS>]->(resource)");
    sb.append(" SET resource.<PROP.OWNED_BY> = {userId}");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String removeResourceOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})");
    sb.append(" MATCH (user)-[relation:<REL.OWNS>]->(resource)");
    sb.append(" DELETE relation");
    sb.append(" SET resource.<PROP.OWNED_BY> = null");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String getResourceById() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{id}})");
    sb.append(" RETURN resource");
    return sb.toString();
  }

}
