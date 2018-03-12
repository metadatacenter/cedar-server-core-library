package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.Map;

public class CypherQueryBuilderGroup extends AbstractCypherQueryBuilder {

  public static String createGroup() {
    StringBuilder sb = new StringBuilder();
    sb.append(" CREATE (group:<COMPOSEDLABEL.GROUP> {");

    sb.append(buildCreateAssignment(NodeProperty.ID)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DESCRIPTION)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.SPECIAL_GROUP)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.NODE_TYPE));
    sb.append("})");

    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String createGroupWithAdministrator() {
    StringBuilder sb = new StringBuilder();
    sb.append(" CREATE (group:<COMPOSEDLABEL.GROUP> {");

    sb.append(buildCreateAssignment(NodeProperty.ID)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DESCRIPTION)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.SPECIAL_GROUP)).append(",");
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
    return "" +
        " MATCH (group:<LABEL.GROUP>)" +
        " RETURN group" +
        " ORDER BY LOWER(group.<PROP.NAME>)";
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
    return "" +
        " MATCH (group:<LABEL.GROUP> {id:{id}})" +
        " DETACH DELETE group";
  }

  public static String getGroupUsersWithRelation(RelationLabel relationLabel) {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (group:<LABEL.GROUP> {id:{groupId}})" +
        " MATCH (user)-[:" + relationLabel + "]->(group)" +
        " RETURN user";
  }

  public static String getGroupBySpecialValue() {
    return "" +
        " MATCH (group:<LABEL.GROUP> {specialGroup:{specialGroup}})" +
        " RETURN group";
  }

  public static String getGroupById() {
    return "" +
        " MATCH (group:<LABEL.GROUP> {id:{id}})" +
        " RETURN group";
  }

  public static String getGroupByName() {
    return "" +
        " MATCH (group:<LABEL.GROUP> {name:{name}})" +
        " RETURN group";
  }

}