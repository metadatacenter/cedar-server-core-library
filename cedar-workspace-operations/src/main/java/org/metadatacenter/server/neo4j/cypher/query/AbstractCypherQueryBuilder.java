package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.*;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerInstance;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.neo4j.util.Neo4JUtil;
import org.metadatacenter.server.neo4j.cypher.sort.QuerySortOptions;

import java.util.List;

public abstract class AbstractCypherQueryBuilder {

  protected final static int ORDER_FOLDER = 1;
  protected final static int ORDER_NON_FOLDER = 2;
  protected static final String ALIAS_FOO = "foo";

  protected static String buildCreateAssignment(NodeProperty property) {
    String escaped = Neo4JUtil.escapePropertyName(property.getValue());
    return escaped + ": {" + escaped + "}";
  }

  protected static String buildUpdateAssignment(NodeProperty property) {
    String escaped = Neo4JUtil.escapePropertyName(property.getValue());
    return escaped + "= {" + escaped + "}";
  }

  protected static String buildSetter(String nodeAlias, NodeProperty property) {
    return " SET " + nodeAlias + "." + buildUpdateAssignment(property);
  }

  protected static String createFSResource(String nodeAlias, FolderServerResource newResource) {
    return createFSNode(nodeAlias, newResource);
  }

  protected static String createFSFolder(String nodeAlias, FolderServerFolder newFolder) {
    return createFSNode(nodeAlias, newFolder);
  }

  private static NodeLabel getFolderLabel(IsRoot isRoot, IsSystem isSystem, IsUserHome isUserHome) {
    if (isUserHome == IsUserHome.TRUE) {
      return NodeLabel.USER_HOME_FOLDER;
    } else if (isSystem == IsSystem.TRUE) {
      return NodeLabel.SYSTEM_FOLDER;
    } else {
      return NodeLabel.FOLDER;
    }
  }

  private static String createFSNode(String nodeAlias, FolderServerNode newNode) {

    NodeLabel label = NodeLabel.forCedarNodeType(newNode.getType());
    if (label == null) {
      FolderServerFolder f = (FolderServerFolder) newNode;
      label = getFolderLabel(IsRoot.forValue(f.isRoot()),
          IsSystem.forValue(f.isSystem()),
          IsUserHome.forValue(f.isUserHome()));
    }

    StringBuilder sb = new StringBuilder();
    sb.append(" CREATE (").append(nodeAlias).append(":").append(label).append(" {");

    sb.append(buildCreateAssignment(NodeProperty.ID)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DESCRIPTION)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.OWNED_BY)).append(",");
    if (newNode instanceof FolderServerResource) {
      FolderServerResource newResource = (FolderServerResource) newNode;
      if (newResource.getVersion() != null) {
        sb.append(buildCreateAssignment(NodeProperty.VERSION)).append(",");
      }
      if (newResource.getPublicationStatus() != null) {
        sb.append(buildCreateAssignment(NodeProperty.PUBLICATION_STATUS)).append(",");
      }
      if (newResource.getDerivedFrom() != null) {
        sb.append(buildCreateAssignment(NodeProperty.DERIVED_FROM)).append(",");
      }
      if (newResource.getPreviousVersion() != null) {
        sb.append(buildCreateAssignment(NodeProperty.PREVIOUS_VERSION)).append(",");
      }
      if (newResource.isLatestVersion() != null) {
        sb.append(buildCreateAssignment(NodeProperty.IS_LATEST_VERSION)).append(",");
      }
      if (newResource.getIdentifier() != null) {
        sb.append(buildCreateAssignment(NodeProperty.IDENTIFIER)).append(",");
      }
      if (newResource.getType() == CedarNodeType.INSTANCE) {
        FolderServerInstance newInstance = (FolderServerInstance) newResource;
        if (newInstance.getIsBasedOn() != null) {
          sb.append(buildCreateAssignment(NodeProperty.IS_BASED_ON)).append(",");
        }
      }
    } else if (newNode instanceof FolderServerFolder) {
      FolderServerFolder newFolder = (FolderServerFolder) newNode;
      if (newFolder.isRoot()) {
        sb.append(buildCreateAssignment(NodeProperty.IS_ROOT)).append(",");
      }
      if (newFolder.isSystem()) {
        sb.append(buildCreateAssignment(NodeProperty.IS_SYSTEM)).append(",");
      }
      if (newFolder.isUserHome()) {
        sb.append(buildCreateAssignment(NodeProperty.IS_USER_HOME)).append(",");
        sb.append(buildCreateAssignment(NodeProperty.HOME_OF)).append(",");
      }
    }

    sb.append(NodeProperty.NODE_SORT_ORDER).append(":")
        .append(label.isFolder() ? ORDER_FOLDER : ORDER_NON_FOLDER).append(",");

    sb.append(buildCreateAssignment(NodeProperty.NODE_TYPE));
    sb.append("})");
    return sb.toString();
  }

  protected static String getOrderByExpression(String nodeAlias, List<String> sortList) {
    StringBuilder sb = new StringBuilder();
    String prefix = "";
    for (String s : sortList) {
      sb.append(prefix);
      sb.append(getOrderByExpression(nodeAlias, s));
      prefix = ", ";
    }
    return sb.toString();
  }

  protected static String getOrderByExpression(String nodeAlias, String s) {
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
    StringBuilder sb = new StringBuilder();
    if (QuerySortOptions.isTextual(fieldName)) {
      sb.append(" LOWER(").append(nodeAlias).append(".").append(QuerySortOptions.getFieldName(fieldName)).append(")");
    } else {
      sb.append(nodeAlias).append(".").append(QuerySortOptions.getFieldName(fieldName));
    }
    return sb.toString();
  }

  public static String addRelation(NodeLabel fromLabel, NodeLabel toLabel, RelationLabel relation) {
    return "" +
        " MATCH (fromNode:" + fromLabel + " {<PROP.ID>:{fromId} })" +
        " MATCH (toNode:" + toLabel + " {<PROP.ID>:{toId} })" +
        " CREATE (fromNode)-[:" + relation + "]->(toNode)" +
        " RETURN fromNode";
  }

  public static String removeRelation(NodeLabel fromLabel, NodeLabel toLabel, RelationLabel relation) {
    return "" +
        " MATCH (fromNode:" + fromLabel + " {<PROP.ID>:{fromId} })" +
        " MATCH (toNode:" + toLabel + " {<PROP.ID>:{toId} })" +
        " MATCH (fromNode)-[relation:" + relation + "]->(toNode)" +
        " DELETE relation" +
        " RETURN fromNode";
  }

  protected static String createFSResourceAsChildOfId(FolderServerResource newResource) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})");
    sb.append(" MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{parentId}})");
    if (newResource.getPreviousVersion() != null) {
      sb.append(" MATCH (pvNode:<LABEL.RESOURCE> {<PROP.ID>:{<PROP.PREVIOUS_VERSION>}})");
    }
    sb.append(createFSResource("child", newResource));
    sb.append(" CREATE (user)-[:<REL.OWNS>]->(child)");
    sb.append(" CREATE (parent)-[:<REL.CONTAINS>]->(child)");
    if (newResource.getPreviousVersion() != null) {
      sb.append("CREATE (child)-[:<REL.PREVIOUSVERSION>]->(pvNode)");
    }
    sb.append(" RETURN child");
    return sb.toString();
  }

  protected static String createFSFolderAsChildOfId(FolderServerFolder newFolder) {
    return "" +
        " MATCH (user:<LABEL.USER> {<PROP.ID>:{userId}})" +
        " MATCH (parent:<LABEL.FOLDER> {<PROP.ID>:{parentId}})" +
        createFSFolder("child", newFolder) +
        " CREATE (user)-[:<REL.OWNS>]->(child)" +
        " CREATE (parent)-[:<REL.CONTAINS>]->(child)" +
        " RETURN child";
  }

  protected static String getUserToResourceRelationDirectly(RelationLabel relationLabel, String nodeAlias) {
    return "(user)-[:" + relationLabel + "]->(" + nodeAlias + ")";
  }

  protected static String getUserToResourceRelationWithContains(RelationLabel relationLabel, String nodeAlias) {
    return "(user)-[:" + relationLabel + "]->()-[:<REL.CONTAINS>*0..]->(" + nodeAlias + ")";
  }

  protected static String getUserToResourceRelationThroughGroup(RelationLabel relationLabel, String nodeAlias) {
    return "(user)-[:<REL.MEMBEROF>*0..1]->()-[:" + relationLabel + "]->(" + nodeAlias + ")";
  }

  protected static String getUserToResourceRelationThroughGroupWithContains(RelationLabel relationLabel, String nodeAlias) {
    return "(user)-[:<REL.MEMBEROF>*0..1]->()-[:" + relationLabel + "]->()-[:<REL.CONTAINS>*0..]->(" + nodeAlias + ")";
  }

  protected static String getResourcePermissionConditions(String relationPrefix, String nodeAlias) {
    return "" +
        " " + relationPrefix + " " +
        "(" +
        getUserToResourceRelationThroughGroup(RelationLabel.CANREADTHIS, nodeAlias) +
        " OR " +
        getUserToResourceRelationWithContains(RelationLabel.OWNS, nodeAlias) +
        " OR " +
        getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANREAD, nodeAlias) +
        " OR " +
        getUserToResourceRelationThroughGroupWithContains(RelationLabel.CANWRITE, nodeAlias) +
        ")";
  }

  protected static String getVersionConditions(String relationPrefix, String nodeAlias) {
    return "" +
        " " + relationPrefix + " " +
        "(" +
        nodeAlias + ".<PROP.IS_LATEST_VERSION> = true" +
        " OR " +
        nodeAlias + ".<PROP.IS_LATEST_VERSION> IS NULL" +
        ")";
  }

  protected static String getPublicationStatusConditions(String relationPrefix, String nodeAlias) {
    return "" +
        " " + relationPrefix + " " +
        "(" +
        nodeAlias + ".<PROP.PUBLICATION_STATUS> = {<PROP.PUBLICATION_STATUS>}" +
        " OR " +
        nodeAlias + ".<PROP.PUBLICATION_STATUS> IS NULL" +
        ")";
  }

}
