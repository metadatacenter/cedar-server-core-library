package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.folder.QuerySortOptions;
import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.RelationLabel;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.List;
import java.util.Map;

public abstract class AbstractCypherQueryBuilder {

  protected final static int ORDER_FOLDER = 1;
  protected final static int ORDER_NON_FOLDER = 2;

  protected static String buildCreateAssignment(NodeProperty property) {
    return property.getValue() + ": {" + property.getValue() + "}";
  }

  protected static String buildUpdateAssignment(NodeProperty property) {
    return property.getValue() + "= {" + property.getValue() + "}";
  }

  protected static String buildSetter(String nodeAlias, NodeProperty property) {
    return " SET" + nodeAlias + "." + buildUpdateAssignment(property);
  }

  protected static String createNode(String nodeAlias, NodeLabel label, Map<NodeProperty, Object> extraProperties) {
    StringBuilder sb = new StringBuilder();
    sb.append(" CREATE (").append(nodeAlias).append(":").append(label).append(" {");

    sb.append(buildCreateAssignment(NodeProperty.ID)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DISPLAY_NAME)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.DESCRIPTION)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.CREATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_BY)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.LAST_UPDATED_ON_TS)).append(",");
    sb.append(buildCreateAssignment(NodeProperty.OWNED_BY));

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
    return "" +
        " MATCH (fromNode:" + fromLabel + " {id:{fromId} })" +
        " MATCH (toNode:" + toLabel + " {id:{toId} })" +
        " CREATE (fromNode)-[:" + relation + "]->(toNode)" +
        " RETURN fromNode";
  }

  public static String removeRelation(NodeLabel fromLabel, NodeLabel toLabel, RelationLabel relation) {
    return "" +
        " MATCH (fromNode:" + fromLabel + " {id:{fromId} })" +
        " MATCH (toNode:" + toLabel + " {id:{toId} })" +
        " MATCH (fromNode)-[relation:" + relation + "]->(toNode)" +
        " DELETE relation" +
        " RETURN fromNode";
  }

  protected static String createNodeAsChildOfId(NodeLabel label, Map<NodeProperty, Object> extraProperties) {
    return "" +
        " MATCH (user:<LABEL.USER> {id:{userId}})" +
        " MATCH (parent:<LABEL.FOLDER> {id:{parentId}})" +
        createNode("child", label, extraProperties) +
        " CREATE (user)-[:<REL.OWNS>]->(child)" +
        " CREATE (parent)-[:<REL.CONTAINS>]->(child)" +
        " RETURN child";
  }

  protected static String getUserToResourceRelationOneStepDirectly(RelationLabel relationLabel, String nodeAlias) {
    return "(user)-[:" + relationLabel + "]->(" + nodeAlias + ")";
  }

  protected static String getUserToResourceRelationOneStepThroughGroup(RelationLabel relationLabel, String nodeAlias) {
    return "(user)-[:<REL.MEMBEROF>*0..1]->()-[:" + relationLabel + "]->(" + nodeAlias + ")";
  }

  protected static String getUserToResourceRelationTwoSteps(RelationLabel relationLabel, String nodeAlias) {
    return "(user)-[:<REL.MEMBEROF>*0..1]->()-[:" + relationLabel + "]->()-[:<REL.CONTAINS>*0..]->(" + nodeAlias + ")";
  }

  protected static String getResourcePermissionConditions(String relationPrefix, String nodeAlias) {
    return "" +
        " " + relationPrefix + " " +
        "(" +
        getUserToResourceRelationOneStepDirectly(RelationLabel.OWNS, nodeAlias) +
        " OR " +
        getUserToResourceRelationOneStepThroughGroup(RelationLabel.CANREADTHIS, nodeAlias) +
        " OR " +
        getUserToResourceRelationTwoSteps(RelationLabel.CANREAD, nodeAlias) +
        " OR " +
        getUserToResourceRelationTwoSteps(RelationLabel.CANWRITE, nodeAlias) +
        ")";
  }

  protected static String getSharedWithMeConditions(String relationPrefix, String nodeAlias) {
    return "" +
        " " + relationPrefix + " " +
        "(" +
        "(user)-[:<REL.MEMBEROF>*0..1]->()-[:<REL.CANREAD>]->(" + nodeAlias + ")" +
        " OR " +
        "(user)-[:<REL.MEMBEROF>*0..1]->()-[:<REL.CANWRITE>]->(" + nodeAlias + ")" +
        ")";
  }

}
