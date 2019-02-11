package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.server.security.model.auth.NodePermission;

public class CypherQueryBuilderPermission extends AbstractCypherQueryBuilder {

  public static String addPermissionToFolderForUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (folder:<LABEL.FOLDER> {<PROP.ID>:{folderId}})" +
        " CREATE (user)-[:" + RelationLabel.forNodePermission(permission) + "]->(folder)" +
        " RETURN user";
  }

  public static String addPermissionToResourceForGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " CREATE (group)-[:" + RelationLabel.forNodePermission(permission) + "]->(resource)" +
        " RETURN group";
  }

  public static String addPermissionToFolderForGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (folder:<LABEL.FOLDER> {<PROP.ID>:{folderId}})" +
        " CREATE (group)-[:" + RelationLabel.forNodePermission(permission) + "]->(folder)" +
        " RETURN group";
  }

  public static String addPermissionToResourceForUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " CREATE (user)-[:" + RelationLabel.forNodePermission(permission) + "]->(resource)" +
        " RETURN user";
  }

  public static String removePermissionForFolderFromUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (folder:<LABEL.FOLDER> {<PROP.ID>:{folderId}})" +
        " MATCH (user)-[relation:" + RelationLabel.forNodePermission(permission) + "]->(folder)" +
        " DELETE (relation)" +
        " RETURN folder";
  }

  public static String removePermissionForFolderFromGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (folder:<LABEL.FOLDER> {<PROP.ID>:{folderId} })" +
        " MATCH (group)-[relation:" + RelationLabel.forNodePermission(permission) + "]->(folder)" +
        " DELETE (relation)" +
        " RETURN folder";
  }

  public static String removePermissionForResourceFromUser(NodePermission permission) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " MATCH (user)-[relation:" + RelationLabel.forNodePermission(permission) + "]->(resource)" +
        " DELETE (relation)" +
        " RETURN resource";
  }

  public static String removePermissionForResourceFromGroup(NodePermission permission) {
    return "" +
        " MATCH (group:<LABEL.GROUP> {<PROP.ID>:{groupId}})" +
        " MATCH (resource:<LABEL.RESOURCE> {<PROP.ID>:{resourceId}})" +
        " MATCH (group)-[relation:" + RelationLabel.forNodePermission(permission) + "]->(resource)" +
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
    sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append(" MATCH (node:<LABEL.FOLDER> {<PROP.ID>:{nodeId}})");
    } else {
      sb.append(" MATCH (node:<LABEL.RESOURCE> {<PROP.ID>:{nodeId}})");
    }
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

  public static String getUsersWithTransitiveReadOnNode(FolderOrResource folderOrResource) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");

    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append(" MATCH (node:<LABEL.FOLDER> {<PROP.ID>:{nodeId}})");
    } else {
      sb.append(" MATCH (node:<LABEL.RESOURCE> {<PROP.ID>:{nodeId}})");
    }
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNS, "node"));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANREAD, "node"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getUsersWithTransitiveWriteOnNode(FolderOrResource folderOrResource) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");

    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append(" MATCH (node:<LABEL.FOLDER> {<PROP.ID>:{nodeId}})");
    } else {
      sb.append(" MATCH (node:<LABEL.RESOURCE> {<PROP.ID>:{nodeId}})");
    }
    sb.append(" WHERE");

    sb.append(" (");
    sb.append(getUserToResourceRelationWithContains(RelationLabel.OWNS, "node"));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANWRITE, "node"));
    sb.append(" )");
    sb.append(" RETURN user");
    return sb.toString();
  }

  public static String getGroupsWithTransitiveReadOnNode(FolderOrResource folderOrResource) {
    String node;
    if (folderOrResource == FolderOrResource.FOLDER) {
      node = "(node:<LABEL.FOLDER> {<PROP.ID>:{nodeId}})";
    } else {
      node = "(node:<LABEL.RESOURCE> {<PROP.ID>:{nodeId}})";
    }

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

  public static String getGroupsWithTransitiveWriteOnNode(FolderOrResource folderOrResource) {
    String node;
    if (folderOrResource == FolderOrResource.FOLDER) {
      node = "(node:<LABEL.FOLDER> {<PROP.ID>:{nodeId}})";
    } else {
      node = "(node:<LABEL.RESOURCE> {<PROP.ID>:{nodeId}})";
    }

    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (group:<LABEL.GROUP>)");
    sb.append(" -[:<REL.CANWRITE>]->()-[:<REL.CONTAINS>*0..]->");
    sb.append(node);
    sb.append(" RETURN group");
    return sb.toString();
  }
}
