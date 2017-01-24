package org.metadatacenter.server.neo4j;

import org.metadatacenter.server.security.model.auth.NodePermission;

import java.util.List;
import java.util.Map;

import static org.metadatacenter.server.neo4j.Neo4JFields.*;

public class CypherQueryBuilder {

  public enum FolderOrResource {
    FOLDER, RESOURCE
  }

  private CypherQueryBuilder() {
  }

  private static String buildCreateAssignment(String propertyName) {
    StringBuilder sb = new StringBuilder();
    sb.append(propertyName).append(": {").append(propertyName).append("}");
    return sb.toString();
  }

  private static String buildUpdateAssignment(String propertyName) {
    StringBuilder sb = new StringBuilder();
    sb.append(propertyName).append("= {").append(propertyName).append("}");
    return sb.toString();
  }

  public static String createRootFolder(Map<String, Object> extraParams) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append(createFolder("root", NodeLabel.SYSTEM_FOLDER, extraParams));
    sb.append("CREATE");
    sb.append("(user)-[:").append(RelationLabel.OWNS).append("]->(root)");
    sb.append("RETURN root");
    return sb.toString();
  }

  private static String createFolder(String folderAlias, NodeLabel label, Map<String, Object>
      extraProperties) {
    return createNode(folderAlias, label, extraProperties, true);
  }

  public static String createResource(String resourceAlias, NodeLabel label, Map<String, Object>
      extraProperties) {
    return createNode(resourceAlias, label, extraProperties, false);
  }

  private static String createNode(String nodeAlias, NodeLabel label, Map<String, Object> extraProperties, boolean
      isFolder) {
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE (");
    sb.append(nodeAlias).append(":").append(label).append(" {");
    sb.append(buildCreateAssignment(ID)).append(",");
    sb.append(buildCreateAssignment(NAME)).append(",");
    sb.append(buildCreateAssignment(DISPLAY_NAME)).append(",");
    sb.append(buildCreateAssignment(DESCRIPTION)).append(",");
    sb.append(buildCreateAssignment(CREATED_BY)).append(",");
    sb.append(buildCreateAssignment(CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_BY)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(OWNED_BY)).append(",");
    sb.append(NODE_SORT_ORDER).append(":").append(isFolder ? 1 : 2).append(",");
    if (extraProperties != null && !extraProperties.isEmpty()) {
      extraProperties.forEach((key, value) -> sb.append(buildCreateAssignment(key)).append(","));
    }
    sb.append(buildCreateAssignment(NODE_TYPE));
    sb.append("}");
    sb.append(")");
    return sb.toString();
  }


  public static String createFolderAsChildOfId(NodeLabel label, Map<String, Object> extraProperties) {
    return createNodeAsChildOfId(label, extraProperties, true);
  }

  public static String createResourceAsChildOfId(NodeLabel label, Map<String, Object> extraProperties) {
    return createNodeAsChildOfId(label, extraProperties, false);
  }

  private static String createNodeAsChildOfId(NodeLabel label, Map<String, Object> extraProperties, boolean isFolder) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{parentId} })");
    sb.append(CypherQueryBuilder.createNode("child", label, extraProperties, isFolder));
    sb.append("CREATE");
    sb.append("(user)-[:").append(RelationLabel.OWNS).append("]->(child)");
    sb.append("CREATE");
    sb.append("(parent)-[:").append(RelationLabel.CONTAINS).append("]->(child)");
    sb.append("RETURN child");
    return sb.toString();
  }

  public static String getFolderLookupQueryByDepth(int cnt) {
    StringBuilder sb = new StringBuilder();
    if (cnt >= 1) {
      sb.append("MATCH (f0:").append(NodeLabel.FOLDER).append(" {name:{f0} })");
    }
    for (int i = 2; i <= cnt; i++) {
      String parentAlias = "f" + (i - 2);
      String childAlias = "f" + (i - 1);
      sb.append("MATCH (");
      sb.append(childAlias);
      sb.append(":").append(NodeLabel.FOLDER).append(" {name:{");
      sb.append(childAlias);
      sb.append("} })");

      sb.append("MATCH (");
      sb.append(parentAlias);
      sb.append(")");
      sb.append("-[:").append(RelationLabel.CONTAINS).append("]->");
      sb.append("(");
      sb.append(childAlias);
      sb.append(")");

    }
    sb.append("RETURN *");
    return sb.toString();
  }

  public static String getFolderContentsLookupQuery(List<String> sortList, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("\nMATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("\nMATCH (child)");
    sb.append("\nMATCH (parent)-[:").append(RelationLabel.CONTAINS).append("]->(child)");
    sb.append("\nWHERE child.nodeType in {nodeTypeList}");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions("\nAND\n", "parent"));
      sb.append(getResourcePermissionConditions("\nAND\n", "child"));
    }
    sb.append("\nRETURN child");
    sb.append("\nORDER BY child.").append(NODE_SORT_ORDER).append(",").append(getOrderByExpression("child", sortList));
    sb.append("\nSKIP {offset}");
    sb.append("\nLIMIT {limit}");
    return sb.toString();
  }

  private static String getResourcePermissionConditions(String relationPrefix, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append(" ").append(relationPrefix).append(" ");
    sb.append("(");
    sb.append(getUserToResourceRelationOneStepDirectly(RelationLabel.OWNS, nodeAlias));
    sb.append("\nOR\n");
    sb.append(getUserToResourceRelationOneStepThroughGroup(RelationLabel.CANREADTHIS, nodeAlias));
    sb.append("\nOR\n");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, nodeAlias));
    sb.append("\nOR\n");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, nodeAlias));
    sb.append(")");
    return sb.toString();
  }

  private static String getUserToResourceRelationOneStepDirectly(RelationLabel relationLabel, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append("(user)-[:").append(relationLabel).append("]->(").append(nodeAlias).append(")");
    return sb.toString();
  }

  private static String getUserToResourceRelationOneStepThroughGroup(RelationLabel relationLabel, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append("(user)-[:").append(RelationLabel.MEMBEROF).append("*0..]->").
        append("()-[:").append(relationLabel).append("]->(").append(nodeAlias).append(")");
    return sb.toString();
  }

  private static String getUserToResourceRelationTwoSteps(RelationLabel relationLabel, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append("(user)-[:").append(RelationLabel.MEMBEROF).append("*0..]->").
        append("()-[:").append(relationLabel).append("]->()-[:").
        append(RelationLabel.CONTAINS).append("*0..]->(").append(nodeAlias).append(")");
    return sb.toString();
  }

  public static String getAllNodesLookupQuery(List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (child)");
    sb.append(" WHERE child:").append(NodeLabel.FOLDER).append(" OR child:").append(NodeLabel.RESOURCE);
    sb.append(" RETURN child");
    sb.append(" ORDER BY ").append(getOrderByExpression("child", sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  public static String getAllNodesCountQuery() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (node)");
    sb.append(" WHERE node:").append(NodeLabel.FOLDER).append(" OR node:").append(NodeLabel.RESOURCE);
    sb.append(" RETURN count(node)");
    return sb.toString();
  }

  private static String getOrderByExpression(String nodeAlias, List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    String prefix = "";
    for (String s : sortList) {
      sb.append(prefix);
      sb.append(getOrderByExpression(nodeAlias, s));
      prefix = ", ";
    }
    return sb.toString();
  }

  private static String getOrderByExpression(String nodeAlias, String s) {
    StringBuilder sb = new StringBuilder();
    if (s != null) {
      if (s.startsWith("-")) {
        sb.append(getCaseInsensitiveSortExpression(nodeAlias, s.substring(1)));
        sb.append(" DESC");
      } else {
        sb.append(getCaseInsensitiveSortExpression(nodeAlias, s));
        sb.append(" ASC");
      }
    }
    return sb.toString();
  }

  private static String getCaseInsensitiveSortExpression(String nodeAlias, String fieldName) {
    if (FolderContentSortOptions.isTextual(fieldName)) {
      return new StringBuilder().append("LOWER(").append(nodeAlias).append(".")
          .append(FolderContentSortOptions.getFieldName(fieldName)).append(")").toString();
    } else {
      return new StringBuilder().append(nodeAlias).append(".")
          .append(FolderContentSortOptions.getFieldName(fieldName)).toString();
    }
  }

  public static String getFolderContentsFilteredCountQuery() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{id} })");
    sb.append("MATCH (child)");
    sb.append("MATCH (parent)");
    sb.append("-[:").append(RelationLabel.CONTAINS).append("]->");
    sb.append("(child)");
    sb.append("WHERE child.nodeType in {nodeTypeList}");
    sb.append("RETURN count(child)");
    return sb.toString();
  }

  public static String getFolderContentsCountQuery() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{id} })");
    sb.append("MATCH (child)");
    sb.append("MATCH (parent)");
    sb.append("-[:").append(RelationLabel.CONTAINS).append("]->");
    sb.append("(child)");
    sb.append("RETURN count(child)");
    return sb.toString();
  }

  public static String getFolderById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (folder:").append(NodeLabel.FOLDER).append(" {id:{id} })");
    sb.append("RETURN folder");
    return sb.toString();
  }

  public static String getResourceById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (resource:").append(NodeLabel.RESOURCE).append(" {id:{id} })");
    sb.append("RETURN resource");
    return sb.toString();
  }

  public static String getUserById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{id} })");
    sb.append("RETURN user");
    return sb.toString();
  }

  public static String getNodeByParentIdAndName() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (parent").append(" {id:{id} })");
    sb.append("MATCH (child)");
    sb.append("MATCH (parent)");
    sb.append("-[:").append(RelationLabel.CONTAINS).append("]->");
    sb.append("(child)");
    sb.append("WHERE child.name = {name}");
    sb.append(" AND (parent:").append(NodeLabel.FOLDER).append(" OR parent:").append(NodeLabel.RESOURCE).append(")");
    sb.append("RETURN child");
    return sb.toString();
  }

  public static String deleteFolderContentsRecursivelyById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (folder:").append(NodeLabel.FOLDER).append(" {id:{id} })");
    sb.append("MATCH (folder)-[relation:").append(RelationLabel.CONTAINS).append("*0..]->(child)");
    sb.append(" DETACH DELETE child");
    sb.append(" DETACH DELETE folder");
    return sb.toString();
  }

  public static String deleteResourceById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (resource:").append(NodeLabel.RESOURCE).append(" {id:{id} })");
    sb.append("DETACH DELETE resource");
    return sb.toString();
  }

  public static String updateFolderById(Map<String, String> updateFields) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (folder:").append(NodeLabel.FOLDER).append(" {id:{id} })");
    sb.append("SET folder.lastUpdatedBy= {lastUpdatedBy}");
    sb.append("SET folder.lastUpdatedOn= {lastUpdatedOn}");
    sb.append("SET folder.lastUpdatedOnTS= {lastUpdatedOnTS}");
    for (String propertyName : updateFields.keySet()) {
      sb.append("SET folder.").append(buildUpdateAssignment(propertyName));
    }
    sb.append("RETURN folder");
    return sb.toString();
  }

  public static String updateResourceById(Map<String, String> updateFields) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (resource:").append(NodeLabel.RESOURCE).append(" {id:{id} })");
    sb.append("SET resource.lastUpdatedBy= {lastUpdatedBy}");
    sb.append("SET resource.lastUpdatedOn= {lastUpdatedOn}");
    sb.append("SET resource.lastUpdatedOnTS= {lastUpdatedOnTS}");
    for (String propertyName : updateFields.keySet()) {
      sb.append("SET resource.").append(buildUpdateAssignment(propertyName));
    }
    sb.append("RETURN resource");
    return sb.toString();
  }

  public static String getFolderLookupQueryById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(root:").append(NodeLabel.FOLDER).append(" {name:{name} })").append(",");
    sb.append("(current:").append(NodeLabel.FOLDER).append(" {id:{id} })").append(",");
    sb.append("path=shortestPath((root)-[:").append(RelationLabel.CONTAINS).append("*]->(current))");
    sb.append("RETURN path");
    return sb.toString();
  }

  public static String getNodeLookupQueryById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(root:").append(NodeLabel.FOLDER).append(" {name:{name} })").append(",");
    sb.append("(current {id:{id} })").append(",");
    sb.append("path=shortestPath((root)-[:").append(RelationLabel.CONTAINS).append("*]->(current))");
    sb.append("RETURN path");
    return sb.toString();
  }

  public static String createUser() {
    String nodeAlias = "user";
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE (");
    sb.append(nodeAlias).append(":").append(NodeLabel.USER).append(" {");
    sb.append(buildCreateAssignment(ID)).append(",");
    sb.append(buildCreateAssignment(NAME)).append(",");
    sb.append(buildCreateAssignment(DISPLAY_NAME)).append(",");
    sb.append(buildCreateAssignment(FIRST_NAME)).append(",");
    sb.append(buildCreateAssignment(LAST_NAME)).append(",");
    sb.append(buildCreateAssignment(EMAIL)).append(",");
    sb.append(buildCreateAssignment(CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NODE_TYPE));
    sb.append("}");
    sb.append(")");
    sb.append("RETURN ").append(nodeAlias);
    return sb.toString();
  }

  public static String createGroup(Map<String, Object> extraProperties) {
    String nodeAlias = "group";
    StringBuilder sb = new StringBuilder();
    sb.append("CREATE (");
    sb.append(nodeAlias).append(":").append(NodeLabel.GROUP).append(" {");
    sb.append(buildCreateAssignment(ID)).append(",");
    sb.append(buildCreateAssignment(NAME)).append(",");
    sb.append(buildCreateAssignment(DISPLAY_NAME)).append(",");
    sb.append(buildCreateAssignment(DESCRIPTION)).append(",");
    sb.append(buildCreateAssignment(CREATED_BY)).append(",");
    sb.append(buildCreateAssignment(CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_BY)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_ON_TS)).append(",");
    if (extraProperties != null && !extraProperties.isEmpty()) {
      extraProperties.forEach((key, value) -> sb.append(buildCreateAssignment(key)).append(","));
    }
    sb.append(buildCreateAssignment(NODE_TYPE));
    sb.append("}");
    sb.append(")");

    sb.append("WITH ").append(nodeAlias);

    sb.append(" MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");

    sb.append(" MERGE (user)-[:").append(RelationLabel.ADMINISTERS).append("]->(").append(nodeAlias).append(")");
    sb.append(" MERGE (user)-[:").append(RelationLabel.MEMBEROF).append("]->(").append(nodeAlias).append(")");

    sb.append(" RETURN ").append(nodeAlias);
    return sb.toString();
  }

  public static String wipeAllData() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (n:").append(NodeLabel.PlainLabels.SCOPE).append(") DETACH DELETE n");
    return sb.toString();
  }

  public static String getGroupBySpecialValue() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (group:").append(NodeLabel.GROUP).append(" {specialGroup:{specialGroup} })");
    sb.append("RETURN group");
    return sb.toString();
  }

  public static String getGroupById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (group:").append(NodeLabel.GROUP).append(" {id:{id} })");
    sb.append("RETURN group");
    return sb.toString();
  }

  public static String getGroupByName() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (group:").append(NodeLabel.GROUP).append(" {name:{name} })");
    sb.append("RETURN group");
    return sb.toString();
  }

  public static String addGroupToUser() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH");
    sb.append("(group:").append(NodeLabel.GROUP).append(" {id:{groupId} })");
    sb.append("MERGE");
    sb.append("(user)-[:").append(RelationLabel.MEMBEROF).append("]->(group)");
    sb.append("RETURN user");
    return sb.toString();
  }

  public static String addPermissionToFolderForGroup(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(group:").append(NodeLabel.GROUP).append(" {id:{groupId} })");
    sb.append("MATCH");
    sb.append("(folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("CREATE");
    sb.append("(group)-[:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(folder)");
    sb.append("RETURN group");
    return sb.toString();
  }

  public static String addPermissionToFolderForUser(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH");
    sb.append("(folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("CREATE");
    sb.append("(user)-[:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(folder)");
    sb.append("RETURN user");
    return sb.toString();
  }

  public static String addPermissionToResourceForGroup(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(group:").append(NodeLabel.GROUP).append(" {id:{groupId} })");
    sb.append("MATCH");
    sb.append("(resource:").append(NodeLabel.RESOURCE).append(" {id:{resourceId} })");
    sb.append("CREATE");
    sb.append("(group)-[:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(resource)");
    sb.append("RETURN group");
    return sb.toString();
  }

  public static String addPermissionToResourceForUser(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH");
    sb.append("(resource:").append(NodeLabel.RESOURCE).append(" {id:{resourceId} })");
    sb.append("CREATE");
    sb.append("(user)-[:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(resource)");
    sb.append("RETURN user");
    return sb.toString();
  }

  public static String removePermissionForFolderFromUser(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH");
    sb.append("(folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("MATCH");
    sb.append("(user)-[relation:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(folder)");
    sb.append("DELETE (relation)");
    sb.append("RETURN folder");
    return sb.toString();
  }

  public static String removePermissionForFolderFromGroup(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(group:").append(NodeLabel.GROUP).append(" {id:{groupId} })");
    sb.append("MATCH");
    sb.append("(folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("MATCH");
    sb.append("(group)-[relation:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(folder)");
    sb.append("DELETE (relation)");
    sb.append("RETURN folder");
    return sb.toString();
  }

  public static String removePermissionForResourceFromUser(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH");
    sb.append("(resource:").append(NodeLabel.RESOURCE).append(" {id:{resourceId} })");
    sb.append("MATCH");
    sb.append("(user)-[relation:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(resource)");
    sb.append("DELETE (relation)");
    sb.append("RETURN resource");
    return sb.toString();
  }

  public static String removePermissionForResourceFromGroup(NodePermission permission) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(group:").append(NodeLabel.GROUP).append(" {id:{groupId} })");
    sb.append("MATCH");
    sb.append("(resource:").append(NodeLabel.RESOURCE).append(" {id:{resourceId} })");
    sb.append("MATCH");
    sb.append("(group)-[relation:")
        .append(RelationLabel.forNodePermission(permission))
        .append("]->(resource)");
    sb.append("DELETE (relation)");
    sb.append("RETURN resource");
    return sb.toString();
  }

  public static String getNodeOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(")");
    sb.append("MATCH (node:").append(NodeLabel.FSNODE).append(" {id:{nodeId} })");
    sb.append("MATCH (user)");
    sb.append("-[:").append(RelationLabel.OWNS).append("]->");
    sb.append("(node)");
    sb.append("RETURN user");
    return sb.toString();
  }

  public static String getUsersWithPermissionOnNode(RelationLabel relationLabel) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(")");
    sb.append("MATCH (node:").append(NodeLabel.FSNODE).append(" {id:{nodeId} })");
    sb.append("MATCH (user)");
    sb.append("-[:").append(relationLabel).append("]->");
    sb.append("(node)");
    sb.append("RETURN user");
    return sb.toString();
  }

  public static String getGroupsWithPermissionOnNode(RelationLabel relationLabel) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (group:").append(NodeLabel.GROUP).append(")");
    sb.append("MATCH (node:").append(NodeLabel.FSNODE).append(" {id:{nodeId} })");
    sb.append("MATCH (group)");
    sb.append("-[:").append(relationLabel).append("]->");
    sb.append("(node)");
    sb.append("RETURN group");
    return sb.toString();
  }

  public static String removeResourceOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(")");
    sb.append("MATCH");
    sb.append("(resource:").append(NodeLabel.RESOURCE).append(" {id:{resourceId} })");
    sb.append("MATCH (user)");
    sb.append("-[relation:").append(RelationLabel.OWNS).append("]->");
    sb.append("(resource)");
    sb.append("DELETE relation");
    sb.append(" SET resource.ownedBy = null");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String removeFolderOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(")");
    sb.append("MATCH");
    sb.append("(folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("MATCH (user)");
    sb.append("-[relation:").append(RelationLabel.OWNS).append("]->");
    sb.append("(folder)");
    sb.append("DELETE (relation)");
    sb.append(" SET folder.ownedBy = null");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String setResourceOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH");
    sb.append("(resource:").append(NodeLabel.RESOURCE).append(" {id:{resourceId} })");
    sb.append("CREATE");
    sb.append("(user)-[:").append(RelationLabel.OWNS).append("]->(resource)");
    sb.append(" SET resource.ownedBy = {userId}");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String setFolderOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH");
    sb.append("(folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("CREATE");
    sb.append("(user)-[:").append(RelationLabel.OWNS).append("]->(folder)");
    sb.append(" SET folder.ownedBy = {userId}");
    sb.append(" RETURN folder");
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
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    if (folderOrResource == FolderOrResource.FOLDER) {
      sb.append("\nMATCH (node:").append(NodeLabel.FOLDER).append(" {id:{nodeId} })");
    } else {
      sb.append("\nMATCH (node:").append(NodeLabel.RESOURCE).append(" {id:{nodeId} })");
    }
    sb.append("\nWHERE");

    sb.append("(");
    sb.append(getUserToResourceRelationOneStepDirectly(RelationLabel.OWNS, "node"));
    if (relationLabel == RelationLabel.CANREAD) {
      sb.append("\nOR\n");
      sb.append(getUserToResourceRelationOneStepThroughGroup(RelationLabel.CANREADTHIS, "node"));
      sb.append("\nOR\n");
      sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, "node"));
    }
    sb.append("\nOR\n");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, "node"));
    sb.append(")");
    sb.append("\nRETURN user");
    return sb.toString();
  }

  public static String findUsers() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(")");
    sb.append(" RETURN user");
    sb.append(" ORDER BY LOWER(user.displayName)");
    return sb.toString();
  }

  public static String findGroups() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (group:").append(NodeLabel.GROUP).append(")");
    sb.append(" RETURN group");
    sb.append(" ORDER BY LOWER(group.displayName)");
    return sb.toString();
  }


  public static String unlinkResourceFromParent() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(parent:").append(NodeLabel.FOLDER).append(")");
    sb.append("MATCH");
    sb.append("(resource:").append(NodeLabel.RESOURCE).append(" {id:{resourceId} })");
    sb.append("MATCH (parent)");
    sb.append("-[relation:").append(RelationLabel.CONTAINS).append("]->");
    sb.append("(resource)");
    sb.append(" DELETE relation");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String linkResourceUnderFolder() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{parentFolderId} })");
    sb.append("MATCH (resource:").append(NodeLabel.RESOURCE).append(" {id:{resourceId} })");
    sb.append("CREATE");
    sb.append("(parent)-[:").append(RelationLabel.CONTAINS).append("]->(resource)");
    sb.append(" RETURN resource");
    return sb.toString();
  }

  public static String unlinkFolderFromParent() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(parent:").append(NodeLabel.FOLDER).append(")");
    sb.append("MATCH");
    sb.append("(folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("MATCH (parent)");
    sb.append("-[relation:").append(RelationLabel.CONTAINS).append("]->");
    sb.append("(folder)");
    sb.append(" DELETE relation");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String linkFolderUnderFolder() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{parentFolderId} })");
    sb.append("MATCH (folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("CREATE");
    sb.append("(parent)-[:").append(RelationLabel.CONTAINS).append("]->(folder)");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String folderIsAncestorOf() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{parentFolderId} })");
    sb.append("MATCH (folder:").append(NodeLabel.FOLDER).append(" {id:{folderId} })");
    sb.append("MATCH (parent)-[:").append(RelationLabel.CONTAINS).append("*0..]->(folder)");
    sb.append(" RETURN parent");
    return sb.toString();
  }

  public static String findOwnedNodes() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH (node:").append(NodeLabel.FSNODE).append(")");
    sb.append("MATCH (user)-[:").append(RelationLabel.OWNS).append("]->(node)");
    sb.append("RETURN node");
    return sb.toString();
  }

  public static String findWritableNodes() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH (node:").append(NodeLabel.FSNODE).append(")");
    sb.append(" WHERE");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, "node"));
    sb.append(" RETURN node");
    return sb.toString();
  }

  public static String findReadableNodes() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH (node:").append(NodeLabel.FSNODE).append(")");
    sb.append(" WHERE");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, "node"));
    sb.append(" RETURN node");
    return sb.toString();
  }

  public static String updateGroupById(Map<String, String> updateFields) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (group:").append(NodeLabel.GROUP).append(" {id:{id} })");
    sb.append("SET group.lastUpdatedBy= {lastUpdatedBy}");
    sb.append("SET group.lastUpdatedOn= {lastUpdatedOn}");
    sb.append("SET group.lastUpdatedOnTS= {lastUpdatedOnTS}");
    for (String propertyName : updateFields.keySet()) {
      sb.append("SET group.").append(buildUpdateAssignment(propertyName));
    }
    sb.append("RETURN group");
    return sb.toString();
  }

  public static String deleteGroupById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (group:").append(NodeLabel.GROUP).append(" {id:{id} })");
    sb.append("DETACH DELETE group");
    return sb.toString();
  }

  public static String getGroupUsersWithRelation(RelationLabel relationLabel) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(")");
    sb.append("MATCH (group:").append(NodeLabel.GROUP).append(" {id:{groupId} })");
    sb.append("MATCH (user)");
    sb.append("-[:").append(relationLabel).append("]->");
    sb.append("(group)");
    sb.append("RETURN user");
    return sb.toString();
  }

  public static String addRelation(NodeLabel fromLabel, NodeLabel toLabel, RelationLabel relation) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (fromNode:").append(fromLabel).append(" {id:{fromId} })");
    sb.append(" MATCH (toNode:").append(toLabel).append(" {id:{toId} })");
    sb.append(" CREATE");
    sb.append(" (fromNode)-[:").append(relation).append("]->(toNode)");
    sb.append(" RETURN fromNode");
    return sb.toString();
  }

  public static String removeRelation(NodeLabel fromLabel, NodeLabel toLabel, RelationLabel relation) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (fromNode:").append(fromLabel).append(" {id:{fromId} })");
    sb.append(" MATCH (toNode:").append(toLabel).append(" {id:{toId} })");
    sb.append(" MATCH");
    sb.append(" (fromNode)-[relation:").append(relation).append("]->(toNode)");
    sb.append(" DELETE relation");
    sb.append(" RETURN fromNode");
    return sb.toString();
  }

  public static String getSharedWithMeLookupQuery(List<String> sortList, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("\nMATCH (node)");
    sb.append("\nWHERE node.nodeType in {nodeTypeList}");
    sb.append("\nAND node.ownedBy  <> {userId}");
    sb.append("\nAND (node.isUserHome IS NULL OR node.isUserHome <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions("\nAND\n", "node"));
    }
    sb.append("\nRETURN node");
    sb.append("\nORDER BY node.").append(NODE_SORT_ORDER).append(",").append(getOrderByExpression("node", sortList));
    sb.append("\nSKIP {offset}");
    sb.append("\nLIMIT {limit}");
    return sb.toString();
  }

  public static String getSharedWithMeCountQuery(boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("\nMATCH (node)");
    sb.append("\nWHERE node.nodeType in {nodeTypeList}");
    sb.append("\nAND node.ownedBy  <> {userId}");
    sb.append("\nAND (node.isUserHome IS NULL OR node.isUserHome <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions("\nAND\n", "node"));
    }
    sb.append("\nRETURN count(node)");
    return sb.toString();
  }

  public static String getAllLookupQuery(List<String> sortList, boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("\nMATCH (node)");
    sb.append("\nWHERE node.nodeType in {nodeTypeList}");
    sb.append("\nAND (node.isUserHome IS NULL OR node.isUserHome <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions("\nAND\n", "node"));
    }
    sb.append("\nRETURN node");
    sb.append("\nORDER BY node.").append(NODE_SORT_ORDER).append(",").append(getOrderByExpression("node", sortList));
    sb.append("\nSKIP {offset}");
    sb.append("\nLIMIT {limit}");
    return sb.toString();
  }

  public static String getAllCountQuery(boolean addPermissionConditions) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("\nMATCH (node)");
    sb.append("\nWHERE node.nodeType in {nodeTypeList}");
    sb.append("\nAND (node.isUserHome IS NULL OR node.isUserHome <> true) ");
    if (addPermissionConditions) {
      sb.append(getResourcePermissionConditions("\nAND\n", "node"));
    }
    sb.append("\nRETURN count(node)");
    return sb.toString();
  }

}
