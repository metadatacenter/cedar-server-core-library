package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.server.security.model.auth.NodePermission;

public class CypherQueryBuilderPermission extends AbstractCypherQueryBuilder {

  public static String addPermissionToNodeForUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " CREATE (user)-[:" + RelationLabel.forNodePermission(permission) + "]->(resource)" +
        " RETURN user";
  }

  public static String addPermissionToNodeForGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " CREATE (group)-[:" + RelationLabel.forNodePermission(permission) + "]->(resource)" +
        " RETURN group";
  }

  public static String removePermissionForNodeFromUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " MATCH (user)-[relation:" + RelationLabel.forNodePermission(permission) + "]->(resource)" +
        " DELETE (relation)" +
        " RETURN resource";
  }

  public static String removePermissionForNodeFromGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId} })" +
        " MATCH (group)-[relation:" + RelationLabel.forNodePermission(permission) + "]->(resource)" +
        " DELETE (relation)" +
        " RETURN resource";
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
    sb.append(" MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})");
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNS, "resource"));
    if (relationLabel == RelationLabel.CANREAD) {
      sb.append(" OR ");
      sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANREAD, "resource"));
    }
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANWRITE, "resource"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getUsersWithDirectPermissionOnNode(RelationLabel relationLabel) {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " MATCH (user)-[:" + relationLabel + "]->(resource)" +
        " RETURN user";
  }

  public static String getGroupsWithDirectPermissionOnNode(RelationLabel relationLabel) {
    return "" +
        " MATCH (group:<LABEL.GROUP>)" +
        " MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})" +
        " MATCH (group)-[:" + relationLabel + "]->(resource)" +
        " RETURN group";
  }

  public static String getUsersWithTransitiveReadOnNode() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})");
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNS, "resource"));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANREAD, "resource"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getUsersWithTransitiveWriteOnNode() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})");
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNS, "resource"));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANWRITE, "resource"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getGroupsWithTransitiveReadOnNode() {
    String node = "(resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})";

    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP>)");
    sb.append(" -[:<REL.CANREAD>]->()-[:<REL.CONTAINS>*0..]->");
    sb.append(node);
    sb.append(" RETURN group");

    return sb.toString();
  }

  public static String getGroupsWithTransitiveWriteOnNode() {
    String node = "(resource:<LABEL.FSNODE> {<PROP.ID>:{nodeId}})";

    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP>)");
    sb.append(" -[:<REL.CANWRITE>]->()-[:<REL.CONTAINS>*0..]->");
    sb.append(node);
    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String userCanWriteCategory() {
    return userHasPermissionOnCategory(RelationLabel.CANWRITECATEGORY);
  }

  public static String userCanAttachCategory() {
    return userHasPermissionOnCategory(RelationLabel.CANATTACHCATEGORY);
  }

  private static String userHasPermissionOnCategory(RelationLabel relationLabel) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    sb.append(" MATCH (category:<LABEL.CATEGORY> {<PROP.ID>:{categoryId}})");
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNSCATEGORY, "category"));
    if (relationLabel == RelationLabel.CANATTACHCATEGORY) {
      sb.append(" OR ");
      sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANATTACHCATEGORY, "category"));
    }
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANWRITECATEGORY, "category"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }


}
