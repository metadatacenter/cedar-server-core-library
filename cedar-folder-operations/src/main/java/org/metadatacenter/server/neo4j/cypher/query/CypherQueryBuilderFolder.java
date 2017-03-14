package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.Map;

public class CypherQueryBuilderFolder extends AbstractCypherQueryBuilder {

  public static String createRootFolder(Map<NodeProperty, Object> extraParams) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(createNode("root", NodeLabel.SYSTEM_FOLDER, extraParams));
    sb.append(" CREATE (user)-[:<REL.OWNS>]->(root)");
    sb.append(" RETURN root");
    return sb.toString();
  }

  public static String getHomeFolderOf() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (folder:<LABEL.FOLDER> {<PROP.IS_USER_HOME>:true, <PROP.HOME_OF>:{userId}})");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String updateFolderById(Map<NodeProperty, String> updateFields) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{id}})");
    sb.append(buildSetter("folder", NodeProperty.LAST_UPDATED_BY));
    sb.append(buildSetter("folder", NodeProperty.LAST_UPDATED_ON));
    sb.append(buildSetter("folder", NodeProperty.LAST_UPDATED_ON_TS));
    for (NodeProperty property : updateFields.keySet()) {
      sb.append(buildSetter("folder", property));
    }
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String createFolderAsChildOfId(NodeLabel label, Map<NodeProperty, Object> extraProperties) {
    return createNodeAsChildOfId(label, extraProperties);
  }

  public static String unlinkFolderFromParent() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (parent:<LABEL.FOLDER>)");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId}})");
    sb.append(" MATCH (parent)-[relation:<REL.CONTAINS>]->(folder)");
    sb.append(" DELETE relation");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String deleteFolderContentsRecursivelyById() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{id}})");
    sb.append(" MATCH (folder)-[relation:<REL.CONTAINS>*0..]->(child)");
    sb.append(" DETACH DELETE child");
    sb.append(" DETACH DELETE folder");
    return sb.toString();
  }

  public static String getFolderLookupQueryById() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (root:<LABEL.FOLDER> {name:{name}}),");
    sb.append(" (current:<LABEL.FOLDER> {id:{id} }),");
    sb.append(" path=shortestPath((root)-[:<REL.CONTAINS>*]->(current))");
    sb.append(" RETURN path");
    return sb.toString();
  }

  public static String folderIsAncestorOf() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (parent:<LABEL.FOLDER> {id:{parentFolderId}})");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId} })");
    sb.append(" MATCH (parent)-[:<REL.CONTAINS>*0..]->(folder)");
    sb.append(" RETURN parent");
    return sb.toString();
  }

  public static String linkFolderUnderFolder() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (parent:<LABEL.FOLDER> {id:{parentFolderId} })");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId} })");
    sb.append(" CREATE (parent)-[:<REL.CONTAINS>]->(folder)");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String setFolderOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId}})");
    sb.append(" CREATE (user)-[:<REL.OWNS>]->(folder)");
    sb.append(" SET folder.<PROP.OWNED_BY> = {userId}");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String removeFolderOwner() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER>)");
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{folderId}})");
    sb.append(" MATCH (user)-[relation:<REL.OWNS>]->(folder)");
    sb.append(" DELETE (relation)");
    sb.append(" SET folder.<PROP.OWNED_BY> = null");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String getFolderById() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (folder:<LABEL.FOLDER> {id:{id}})");
    sb.append(" RETURN folder");
    return sb.toString();
  }

  public static String getFolderLookupQueryByDepth(int cnt) {
    StringBuilder sb = new StringBuilder();
    if (cnt >= 1) {
      sb.append(" MATCH (f0:<LABEL.FOLDER> {name:{f0} })");
    }
    for (int i = 2; i <= cnt; i++) {
      String parentAlias = "f" + (i - 2);
      String childAlias = "f" + (i - 1);
      sb.append(" MATCH (");
      sb.append(childAlias);
      sb.append(":<LABEL.FOLDER> {name:{");
      sb.append(childAlias);
      sb.append("} })");

      sb.append(" MATCH (");
      sb.append(parentAlias);
      sb.append(")-[:<REL.CONTAINS>]->(");
      sb.append(childAlias);
      sb.append(")");

    }
    sb.append(" RETURN *");
    return sb.toString();
  }

}
