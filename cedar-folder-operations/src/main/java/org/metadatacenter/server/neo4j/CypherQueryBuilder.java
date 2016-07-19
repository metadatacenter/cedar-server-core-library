package org.metadatacenter.server.neo4j;

import org.metadatacenter.server.security.model.auth.NodePermission;

import java.util.List;
import java.util.Map;

import static org.metadatacenter.server.neo4j.Neo4JFields.*;

public class CypherQueryBuilder {

  private static String groupBySpecialValue;

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

  public static String createFolder(String folderAlias, NodeLabel label, Map<String, Object>
      extraProperties) {
    return createNode(folderAlias, label, extraProperties);
  }

  public static String createResource(String resourceAlias, NodeLabel label, Map<String, Object>
      extraProperties) {
    return createNode(resourceAlias, label, extraProperties);
  }

  private static String createNode(String nodeAlias, NodeLabel label, Map<String, Object> extraProperties) {
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
    if (extraProperties != null && !extraProperties.isEmpty()) {
      extraProperties.forEach((key, value) -> sb.append(buildCreateAssignment(key)).append(","));
    }
    sb.append(buildCreateAssignment(NODE_TYPE));
    sb.append("}");
    sb.append(")");
    return sb.toString();
  }


  public static String createFolderAsChildOfId(NodeLabel label, Map<String, Object> extraProperties) {
    return createNodeAsChildOfId(label, extraProperties);
  }

  public static String createResourceAsChildOfId(NodeLabel label, Map<String, Object> extraProperties) {
    return createNodeAsChildOfId(label, extraProperties);
  }

  private static String createNodeAsChildOfId(NodeLabel label, Map<String, Object> extraProperties) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{parentId} })");
    sb.append(CypherQueryBuilder.createNode("child", label, extraProperties));
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
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{id} })");
    sb.append("MATCH (child)");
    sb.append("MATCH (parent)");
    sb.append("-[:").append(RelationLabel.CONTAINS).append("]->");
    sb.append("(child)");
    sb.append("WHERE child.nodeType in {nodeTypeList}");
    if (addPermissionConditions) {
      sb.append(getPermissionConditions("AND", "parent"));
      sb.append(getPermissionConditions("AND", "child"));
    }
    sb.append("RETURN child");
    sb.append(" ORDER BY ").append(getOrderByExpression(sortList));
    sb.append(" SKIP {offset}");
    sb.append(" LIMIT {limit}");
    return sb.toString();
  }

  private static String getPermissionConditions(String prefix, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append(" ").append(prefix).append(" ");
    sb.append("(");
    sb.append(nodeAlias).append(".").append(Neo4JFields.OWNED_BY);
    sb.append("= {").append(Neo4JFields.OWNED_BY).append("}");
    sb.append(")");
    return sb.toString();
  }

  public static String getAllNodesLookupQuery(List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (child)");
    sb.append(" WHERE child:").append(NodeLabel.FOLDER).append(" OR child:").append(NodeLabel.RESOURCE);
    sb.append(" RETURN child");
    sb.append(" ORDER BY ").append(getOrderByExpression(sortList));
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

  private static String getOrderByExpression(List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    String prefix = "";
    for (String s : sortList) {
      sb.append(prefix);
      sb.append(getOrderByExpression(s));
      prefix = ", ";
    }
    ;
    return sb.toString();
  }

  private static String getOrderByExpression(String s) {
    StringBuilder sb = new StringBuilder();
    if (s != null) {
      sb.append("child.");
      if (s.startsWith("-")) {
        sb.append(s.substring(1));
        sb.append(" DESC");
      } else {
        sb.append(s);
        sb.append(" ASC");
      }
    }
    return sb.toString();
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

  public static String getFolderByParentIdAndName() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (parent:").append(NodeLabel.FOLDER).append(" {id:{id} })");
    sb.append("MATCH (child)");
    sb.append("MATCH (parent)");
    sb.append("-[:").append(RelationLabel.CONTAINS).append("]->");
    sb.append("(child)");
    sb.append("WHERE child.name = {name}");
    sb.append("RETURN child");
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

  public static String deleteFolderById() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH (folder:").append(NodeLabel.FOLDER).append(" {id:{id} })");
    sb.append("DETACH DELETE folder");
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
    sb.append(buildCreateAssignment(CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(LAST_UPDATED_ON_TS)).append(",");
    if (extraProperties != null && !extraProperties.isEmpty()) {
      extraProperties.forEach((key, value) -> sb.append(buildCreateAssignment(key)).append(","));
    }
    sb.append(buildCreateAssignment(NODE_TYPE));
    sb.append("}");
    sb.append(")");
    sb.append("RETURN ").append(nodeAlias);
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

  public static String addGroupToUser() {
    StringBuilder sb = new StringBuilder();
    sb.append("MATCH");
    sb.append("(user:").append(NodeLabel.USER).append(" {id:{userId} })");
    sb.append("MATCH");
    sb.append("(group:").append(NodeLabel.GROUP).append(" {id:{groupId} })");
    sb.append("CREATE");
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
        .append(permission == NodePermission.READ ? RelationLabel.CANREAD : RelationLabel.CANWRITE)
        .append("]->(folder)");
    sb.append("RETURN group");
    return sb.toString();
  }
}
