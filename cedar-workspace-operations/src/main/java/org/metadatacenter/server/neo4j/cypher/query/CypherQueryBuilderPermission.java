package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.server.security.model.auth.NodePermission;

public class CypherQueryBuilderPermission extends AbstractCypherQueryBuilder {

  public static String addPermissionToNodeForUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " CREATE (user)-[:" + RelationLabel.forNodePermission(permission) + "]->(node)" +
        " RETURN user";
  }

  public static String addPermissionToNodeForGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " CREATE (group)-[:" + RelationLabel.forNodePermission(permission) + "]->(node)" +
        " RETURN group";
  }

  public static String removePermissionForNodeFromUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " MATCH (user)-[relation:" + RelationLabel.forNodePermission(permission) + "]->(node)" +
        " DELETE (relation)" +
        " RETURN node";
  }

  public static String removePermissionForNodeFromGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId} })" +
        " MATCH (group)-[relation:" + RelationLabel.forNodePermission(permission) + "]->(node)" +
        " DELETE (relation)" +
        " RETURN node";
  }

  public static String userCanReadNode() {
    return userHasPermissionOnNode(RelationLabel.CANREAD);
  }

  public static String userCanWriteNode() {
    return userHasPermissionOnNode(RelationLabel.CANWRITE);
  }

  private static String userHasPermissionOnNode(RelationLabel relationLabel) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    sb.append(" MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})");
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNS, "node"));
    if (relationLabel == RelationLabel.CANREAD) {
      sb.append(" OR ");
      sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANREAD, "node"));
    }
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANWRITE, "node"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getUsersWithDirectPermissionOnNode(RelationLabel relationLabel) {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " MATCH (user)-[:" + relationLabel + "]->(node)" +
        " RETURN user";
  }

  public static String getGroupsWithDirectPermissionOnNode(RelationLabel relationLabel) {
    return "" +
        " MATCH (group:<LABEL.GROUP>)" +
        " MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " MATCH (group)-[:" + relationLabel + "]->(node)" +
        " RETURN group";
  }

  public static String getUsersWithTransitiveReadOnNode() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})");
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNS, "node"));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANREAD, "node"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getUsersWithTransitiveWriteOnNode() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})");
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNS, "node"));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANWRITE, "node"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getGroupsWithTransitiveReadOnNode() {
    String node = "(node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})";

    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP>)");
    sb.append(" -[:<REL.CANREADTHIS>]->");
    sb.append(node);
    sb.append(" RETURN group");

    sb.append(" UNION");

    sb.append(" MATCH (group:<LABEL.GROUP>)");
    sb.append(" -[:<REL.CANREAD>]->()-[:<REL.CONTAINS>*0..]->");
    sb.append(node);
    sb.append(" RETURN group");

    return sb.toString();
  }

  public static String getGroupsWithTransitiveWriteOnNode() {
    String node = "(node:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})";

    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP>)");
    sb.append(" -[:<REL.CANWRITE>]->()-[:<REL.CONTAINS>*0..]->");
    sb.append(node);
    sb.append(" RETURN group");
    return sb.toString();
  }
}
