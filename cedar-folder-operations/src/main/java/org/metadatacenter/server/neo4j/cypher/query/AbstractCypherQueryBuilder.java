package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.folder.QuerySortOptions;
import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.RelationLabel;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractCypherQueryBuilder {

  protected final static int ORDER_FOLDER = 1;
  protected final static int ORDER_NON_FOLDER = 2;

  protected static List<NodeProperty> commonNodeProperties;

  static {
    commonNodeProperties = new ArrayList<>();
    commonNodeProperties.add(NodeProperty.ID);
    commonNodeProperties.add(NodeProperty.NAME);
    commonNodeProperties.add(NodeProperty.DISPLAY_NAME);
    commonNodeProperties.add(NodeProperty.DESCRIPTION);
    commonNodeProperties.add(NodeProperty.CREATED_BY);
    commonNodeProperties.add(NodeProperty.CREATED_ON);
    commonNodeProperties.add(NodeProperty.CREATED_ON_TS);
    commonNodeProperties.add(NodeProperty.LAST_UPDATED_BY);
    commonNodeProperties.add(NodeProperty.LAST_UPDATED_ON);
    commonNodeProperties.add(NodeProperty.LAST_UPDATED_ON_TS);
    commonNodeProperties.add(NodeProperty.OWNED_BY);
  }

  protected static String buildCreateAssignment(NodeProperty property) {
    StringBuilder sb = new StringBuilder();
    sb.append(property.getValue()).append(": {").append(property.getValue()).append("}");
    return sb.toString();
  }

  protected static String buildUpdateAssignment(NodeProperty property) {
    StringBuilder sb = new StringBuilder();
    sb.append(property.getValue()).append("= {").append(property.getValue()).append("}");
    return sb.toString();
  }

  protected static String buildSetter(String nodeAlias, NodeProperty property) {
    StringBuilder sb = new StringBuilder();
    sb.append(" SET").append(nodeAlias).append(".").append(buildUpdateAssignment(property));
    return sb.toString();
  }

  protected static String createNode(String nodeAlias, NodeLabel label, Map<NodeProperty, Object> extraProperties) {
    StringBuilder sb = new StringBuilder();
    sb.append(" CREATE (").append(nodeAlias).append(":").append(label).append(" {");

    for (NodeProperty property : commonNodeProperties) {
      sb.append(buildCreateAssignment(property)).append(",");
    }

    sb.append(NodeProperty.NODE_SORT_ORDER).append(":")
        .append(label.isFolder() ? ORDER_FOLDER : ORDER_NON_FOLDER).append(",");

    if (extraProperties != null && !extraProperties.isEmpty()) {
      for (NodeProperty property : extraProperties.keySet()) {
        sb.append(buildCreateAssignment(property)).append(",");
      }
    }

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
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (fromNode:").append(fromLabel).append(" {id:{fromId} })");
    sb.append(" MATCH (toNode:").append(toLabel).append(" {id:{toId} })");
    sb.append(" CREATE (fromNode)-[:").append(relation).append("]->(toNode)");
    sb.append(" RETURN fromNode");
    return sb.toString();
  }

  public static String removeRelation(NodeLabel fromLabel, NodeLabel toLabel, RelationLabel relation) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (fromNode:").append(fromLabel).append(" {id:{fromId} })");
    sb.append(" MATCH (toNode:").append(toLabel).append(" {id:{toId} })");
    sb.append(" MATCH (fromNode)-[relation:").append(relation).append("]->(toNode)");
    sb.append(" DELETE relation");
    sb.append(" RETURN fromNode");
    return sb.toString();
  }

  protected static String createNodeAsChildOfId(NodeLabel label, Map<NodeProperty, Object> extraProperties) {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (user:<LABEL.USER> {id:{userId}})");
    sb.append(" MATCH (parent:<LABEL.FOLDER {id:{parentId}})");
    sb.append(createNode("child", label, extraProperties));
    sb.append(" CREATE (user)-[:<REL.OWNS>]->(child)");
    sb.append(" CREATE (parent)-[:<REL.CONTAINS>]->(child)");
    sb.append(" RETURN child");
    return sb.toString();
  }

  protected static String getUserToResourceRelationOneStepDirectly(RelationLabel relationLabel, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append("(user)-[:").append(relationLabel).append("]->(").append(nodeAlias).append(")");
    return sb.toString();
  }

  protected static String getUserToResourceRelationOneStepThroughGroup(RelationLabel relationLabel, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append("(user)-[:<REL.MEMBEROF>*0..1]->").
        append("()-[:").append(relationLabel).append("]->(").append(nodeAlias).append(")");
    return sb.toString();
  }

  protected static String getUserToResourceRelationTwoSteps(RelationLabel relationLabel, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append("(user)-[:<REL.MEMBEROF>*0..1]->").
        append("()-[:").append(relationLabel).append("]->()-[:<REL.CONTAINS>*0..]->(").append(nodeAlias).append(")");
    return sb.toString();
  }

  protected static String getResourcePermissionConditions(String relationPrefix, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append(" ").append(relationPrefix).append(" ");
    sb.append("(");
    sb.append(getUserToResourceRelationOneStepDirectly(RelationLabel.OWNS, nodeAlias));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationOneStepThroughGroup(RelationLabel.CANREADTHIS, nodeAlias));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, nodeAlias));
    sb.append(" OR ");
    sb.append(getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, nodeAlias));
    sb.append(")");
    return sb.toString();
  }

  protected static String getSharedWithMeConditions(String relationPrefix, String nodeAlias) {
    StringBuilder sb = new StringBuilder();
    sb.append(" ").append(relationPrefix).append(" ");
    sb.append("(");
    sb.append("(user)-[:<REL.MEMBEROF>*0..1]->()-[:<REL.CANREAD>]->(").append(nodeAlias).append(")");
    sb.append(" OR ");
    sb.append("(user)-[:<REL.MEMBEROF>*0..1]->()-[:<REL.CANWRITE>]->(").append(nodeAlias).append(")");
    sb.append(")");
    return sb.toString();
  }

}
