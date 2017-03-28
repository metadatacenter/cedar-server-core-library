package org.metadatacenter.server.neo4j;

import org.metadatacenter.server.neo4j.parameter.NodeProperty;

import java.util.*;

public class AbstractCypherQuery implements CypherQuery {

  protected final String query;
  protected String runnableQuery;

  protected static final Set<NodeLabel> labels;
  protected static final Set<RelationLabel> relations;
  protected static final Set<NodeProperty> properties;
  protected static final Map<String, String> replacementTable;

  static {
    labels = new HashSet<>();
    Collections.addAll(labels, NodeLabel.values());

    relations = new HashSet<>();
    Collections.addAll(relations, RelationLabel.values());

    properties = new HashSet<>();
    Collections.addAll(properties, NodeProperty.values());

    replacementTable = new HashMap<>();

    for (NodeLabel label : labels) {
      replacementTable.put("<LABEL." + label.name() + ">", label.getSimpleLabel());
      replacementTable.put("<COMPOSEDLABEL." + label.name() + ">", label.getComposedLabel());
    }

    for (RelationLabel relation : relations) {
      replacementTable.put("<REL." + relation.name() + ">", relation.getValue());
    }

    for (NodeProperty property : properties) {
      replacementTable.put("<PROP." + property.name() + ">", property.getValue());
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
