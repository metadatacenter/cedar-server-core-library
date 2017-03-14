package org.metadatacenter.server.neo4j;

import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class AbstractCypherQuery implements CypherQuery {

  protected final String query;
  protected String runnableQuery;

  protected static Set<NodeLabel> labels;
  protected static Set<RelationLabel> relations;
  protected static Set<NodeProperty> properties;
  protected static Map<String, String> replacementTable;

  static {
    labels = new HashSet<>();
    for (NodeLabel label : NodeLabel.values()) {
      labels.add(label);
    }

    relations = new HashSet<>();
    for (RelationLabel label : RelationLabel.values()) {
      relations.add(label);
    }

    properties = new HashSet<>();
    for (NodeProperty property : NodeProperty.values()) {
      properties.add(property);
    }

    replacementTable = new HashMap<>();

    for (NodeLabel label : labels) {
      StringBuilder sb = new StringBuilder();
      sb.append("<LABEL.").append(label.name()).append(">");
      replacementTable.put(sb.toString(), label.getValue());
    }

    for (RelationLabel relation : relations) {
      StringBuilder sb = new StringBuilder();
      sb.append("<REL.").append(relation.name()).append(">");
      replacementTable.put(sb.toString(), relation.getValue());
    }

    for (NodeProperty property : properties) {
      StringBuilder sb = new StringBuilder();
      sb.append("<PROP.").append(property.name()).append(">");
      replacementTable.put(sb.toString(), property.getValue());
    }

  }

  public AbstractCypherQuery(String query) {
    this.query = query;
    this.resolveRunnableQuery();
  }

  @Override
  public String getOriginalQuery() {
    return query;
  }

  @Override
  public String getRunnableQuery() {
    return runnableQuery;
  }

  private void resolveRunnableQuery() {
    String q = query;
    if (q != null) {
      for (String from : replacementTable.keySet()) {
        q = q.replace(from, replacementTable.get(from));
      }
    }
    runnableQuery = q;
  }


}
