package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.server.neo4j.RelationLabel;
import org.metadatacenter.server.security.model.auth.NodePermission;

public class CypherQueryBuilderPermission extends AbstractCypherQueryBuilder {

  public static String addPermissionToFolderForUser(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId}})");
    sb.append(" CREATE (user)-[:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(folder)");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String addPermissionToResourceForGroup(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{groupId}})");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})");
    sb.append(" CREATE (group)-[:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(resource)");
    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String addPermissionToFolderForGroup(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{groupId}})");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId}})");
    sb.append(" CREATE (group)-[:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(folder)");
    sb.append(" RETURN group");
    return sb.toString();
  }

  public static String addPermissionToResourceForUser(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})");
    sb.append(" CREATE (user)-[:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(resource)");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String removePermissionForFolderFromUser(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId}})");
    sb.append(" MATCH (user)-[relation:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(folder)");
    sb.append(" DELETE (relation)");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String removePermissionForFolderFromGroup(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{groupId}})");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId} })");
    sb.append(" MATCH (group)-[relation:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(folder)");
    sb.append(" DELETE (relation)");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String removePermissionForResourceFromUser(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})");
    sb.append(" MATCH (user)-[relation:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(resource)");
    sb.append(" DELETE (relation)");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String removePermissionForResourceFromGroup(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP> {id:{groupId}})");
    sb.append(" MATCH (resource:<LABEL.RESOURCE> {id:{resourceId}})");
    sb.append(" MATCH (group)-[relation:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(resource)");
    sb.append(" DELETE (relation)");
    sb.append(" RETURN resource");
    return sb.toString();
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
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (node:<LABEL.FSNODE> {id:{nodeId}})");
    sb.append(" MATCH (user)-[:").append(relationLabel).append("]->(node)");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getGroupsWithDirectPermissionOnNode(RelationLabel relationLabel) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP>)");
    sb.append(" MATCH (node:<LABEL.FSNODE> {id:{nodeId}})");
    sb.append(" MATCH (group)-[:").append(relationLabel).append("]->(node)");
    sb.append(" RETURN group");
    return sb.toString();
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
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (node:<LABEL.FSNODE>)");
    sb.append(" WHERE");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, "node"));
    sb.append(" RETURN node");
    return sb.toString();
  }

  public static String findReadableNodes() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (node:<LABEL.FSNODE>)");
    sb.append(" WHERE");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, "node"));
    sb.append(" RETURN node");
    return sb.toString();
  }

  public static String findOwnedNodes() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (node:<:LABEL.FSNODE>)");
    sb.append(" MATCH (user)-[:<REL>OWNS>]->(node)");
    sb.append(" RETURN node");
    return sb.toString();
  }

}
