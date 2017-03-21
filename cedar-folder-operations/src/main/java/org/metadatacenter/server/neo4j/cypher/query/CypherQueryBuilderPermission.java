package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.server.neo4j.RelationLabel;
import org.metadatacenter.server.security.model.auth.NodePermission;

public class CypherQueryBuilderPermission extends AbstractCypherQueryBuilder {

  public static String addPermissionToFolderForUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (folder:<LABEL.FOLDER> {id:{folderId}})" +
        " CREATE (user)-[:" +
        RelationLabel.forNodePermission(permission) +
        "]->(folder)" +
        " RETURN user";
  }

  public static String addPermissionToResourceForGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {id:{groupId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})" +
        " CREATE (group)-[:" +
        RelationLabel.forNodePermission(permission) +
        "]->(resource)" +
        " RETURN group";
  }

  public static String addPermissionToFolderForGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {id:{groupId}})" +
        " MATCH (folder:<LABEL.FOLDER> {id:{folderId}})" +
        " CREATE (group)-[:" +
        RelationLabel.forNodePermission(permission) +
        "]->(folder)" +
        " RETURN group";
  }

  public static String addPermissionToResourceForUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})" +
        " CREATE (user)-[:" +
        RelationLabel.forNodePermission(permission) +
        "]->(resource)" +
        " RETURN user";
  }

  public static String removePermissionForFolderFromUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (folder:<LABEL.FOLDER> {id:{folderId}})" +
        " MATCH (user)-[relation:" +
        RelationLabel.forNodePermission(permission) +
        "]->(folder)" +
        " DELETE (relation)" +
        " RETURN folder";
  }

  public static String removePermissionForFolderFromGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {id:{groupId}})" +
        " MATCH (folder:<LABEL.FOLDER> {id:{folderId} })" +
        " MATCH (group)-[relation:" +
        RelationLabel.forNodePermission(permission) +
        "]->(folder)" +
        " DELETE (relation)" +
        " RETURN folder";
  }

  public static String removePermissionForResourceFromUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})" +
        " MATCH (user)-[relation:" +
        RelationLabel.forNodePermission(permission) +
        "]->(resource)" +
        " DELETE (relation)" +
        " RETURN resource";
  }

  public static String removePermissionForResourceFromGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {id:{groupId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})" +
        " MATCH (group)-[relation:" +
        RelationLabel.forNodePermission(permission) +
        "]->(resource)" +
        " DELETE (relation)" +
        " RETURN resource";
  }

  public static String userCanReadNode(FolderOrResource folderOrResource) {
    return userHasPermissionOnNode(RelationLabel.CANREAD, folderOrResource);
  }

  public static String userCanWriteNode(FolderOrResource folderOrResource) {
    return userHasPermissionOnNode(RelationLabel.CANWRITE, folderOrResource);
  }

  private static String userHasPermissionOnNode(RelationLabel relationLabel, FolderOrResource folderOrResource) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append(" MATCH (node:<LABEL.FOLDER> {id:{nodeId}})");
    } else {
      sb.append(" MATCH (node:<LABEL.RESOURCE> {id:{nodeId}})");
    }
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationOneStepDirectly(RelationLabel.OWNS, "node"));
    if (relationLabel == RelationLabel.CANREAD) {
      sb.append(" OR ");
      sb.append(getUserToResourceRelationOneStepThroughGroup(RelationLabel.CANREADTHIS, "node"));
      sb.append(" OR ");
      sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, "node"));
    }
    sb.append(" OR ");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, "node"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getUsersWithDirectPermissionOnNode(RelationLabel relationLabel) {
    return "" +
        " MATCH (user:<LABEL.USER>)" +
        " MATCH (node:<LABEL.FSNODE> {id:{nodeId}})" +
        " MATCH (user)-[:" + relationLabel + "]->(node)" +
        " RETURN user";
  }

  public static String getGroupsWithDirectPermissionOnNode(RelationLabel relationLabel) {
    return "" +
        " MATCH (group:<LABEL.GROUP>)" +
        " MATCH (node:<LABEL.FSNODE> {id:{nodeId}})" +
        " MATCH (group)-[:" + relationLabel + "]->(node)" +
        " RETURN group";
  }

  public static String getUsersWithTransitiveReadOnNode(FolderOrResource folderOrResource) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");

    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append(" MATCH (node:<LABEL.FOLDER> {id:{nodeId}})");
    } else {
      sb.append(" MATCH (node:<LABEL.RESOURCE> {id:{nodeId}})");
    }
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationOneStepThroughGroup(RelationLabel.CANREADTHIS, "node"));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, "node"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getUsersWithTransitiveWriteOnNode(FolderOrResource folderOrResource) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");

    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append(" MATCH (node:<LABEL.FOLDER> {id:{nodeId}})");
    } else {
      sb.append(" MATCH (node:<LABEL.RESOURCE> {id:{nodeId}})");
    }
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationOneStepDirectly(RelationLabel.OWNS, "node"));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, "node"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getGroupsWithTransitiveReadOnNode(FolderOrResource folderOrResource) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP>)");

    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append(" MATCH (node:<LABEL.FOLDER> {id:{nodeId}})");
    } else {
      sb.append(" MATCH (node:<LABEL.RESOURCE> {id:{nodeId}})");
    }
    sb.append(" WHERE");
    sb.append(" (");
    sb.append(" (group)-[:<REL.CANREADTHIS>]->(node)");
    sb.append(" OR ");
    sb.append(" (group)-[:<REL.CANREAD>]->()-[:<REL.CONTAINS>*0..]->(node)");
    sb.append(" )");
    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String getGroupsWithTransitiveWriteOnNode(FolderOrResource folderOrResource) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (group:<LABEL.GROUP>)");

    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append(" MATCH (node:<LABEL.FOLDER> {id:{nodeId}})");
    } else {
      sb.append(" MATCH (node:<LABEL.RESOURCE> {id:{nodeId}})");
    }
    sb.append(" WHERE");
    sb.append(" (");
    sb.append(" (group)-[:<REL.CANWRITE>]->()-[:<REL.CONTAINS>*0..]->(node)");
    sb.append(" )");
    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String findWritableNodes() {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (node:<LABEL.FSNODE>)" +
        " WHERE" +
        getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, "node") +
        " RETURN node";
  }

  public static String findReadableNodes() {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (node:<LABEL.FSNODE>)" +
        " WHERE" +
        getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, "node") +
        " RETURN node";
  }

  public static String findOwnedNodes() {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (node:<:LABEL.FSNODE>)" +
        " MATCH (user)-[:<REL>OWNS>]->(node)" +
        " RETURN node";
  }

}
