package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.parameter.NodeProperty;

public class CypherQueryBuilderUser extends AbstractCypherQueryBuilder {

  public static String createUser() {
    String nodeAlias = "user";
    StringBuilder sb = new StringBuilder();
    sb.append(" CREATE (");
    sb.append(nodeAlias).append(":<LABEL.USER> {");
    sb.append(buildCreateAssignment(NodeProperty.ID)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DISPLAY_NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.FIRST_NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.EMAIL)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.NODE_TYPE));
    sb.append(" }");
    sb.append(" )");
    sb.append(" RETURN ").append(nodeAlias);
    return sb.toString();
  }

  public static String findUsers() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" RETURN user");
    sb.append(" ORDER BY LOWER(user.<PROP.DISPLAY_NAME>)");
    return sb.toString();
  }

  public static String getUserById() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{id}})");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String addGroupToUser() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{groupId}})");
    sb.append(" MERGE (user)-[:<REL.MEMBEROF>]->(group)");
    sb.append(" RETURN user");
    return sb.toString();
  }


}
