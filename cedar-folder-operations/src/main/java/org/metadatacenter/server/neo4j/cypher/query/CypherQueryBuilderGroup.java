package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.RelationLabel;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.Map;

public class CypherQueryBuilderGroup extends AbstractCypherQueryBuilder {

  public static String createGroup(Map<NodeProperty, Object> extraProperties) {
    StringBuilder sb = new StringBuilder();
    sb.append(" CREATE (group:<LABEL.GROUP> {");

    sb.append(buildCreateAssignment(NodeProperty.ID)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DISPLAY_NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DESCRIPTION)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON_TS)).append(",");

    if (extraProperties != null && !extraProperties.isEmpty()) {
      extraProperties.forEach((key, value) -> sb.append(buildCreateAssignment(key)).append(","));
    }
    sb.append(buildCreateAssignment(NodeProperty.NODE_TYPE));
    sb.append("})");

    sb.append(" WITH group");

    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");

    sb.append(" MERGE (user)-[:<REL.ADMINISTERS>]->(group)");
    sb.append(" MERGE (user)-[:<REL.MEMBEROF>]->(group)");

    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String findGroups() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP>)");
    sb.append(" RETURN group");
    sb.append(" ORDER BY LOWER(group.<PROP.DISPLAY_NAME>)");
    return sb.toString();
  }

  public static String updateGroupById(Map<NodeProperty, String> updateFields) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{id}})");
    sb.append(buildSetter("group", NodeProperty.LAST_UPDATED_BY));
    sb.append(buildSetter("group", NodeProperty.LAST_UPDATED_ON));
    sb.append(buildSetter("group", NodeProperty.LAST_UPDATED_ON_TS));
    for (NodeProperty property : updateFields.keySet()) {
      sb.append(buildSetter("group", property));
    }
    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String deleteGroupById() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{id}})");
    sb.append(" DETACH DELETE group");
    return sb.toString();
  }

  public static String getGroupUsersWithRelation(RelationLabel relationLabel) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{groupId}})");
    sb.append(" MATCH (user)-[:").append(relationLabel).append("]->(group)");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getGroupBySpecialValue() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {specialGroup:{specialGroup}})");
    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String getGroupById() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{id}})");
    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String getGroupByName() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {name:{name}})");
    sb.append(" RETURN group");
    return sb.toString();
  }

}