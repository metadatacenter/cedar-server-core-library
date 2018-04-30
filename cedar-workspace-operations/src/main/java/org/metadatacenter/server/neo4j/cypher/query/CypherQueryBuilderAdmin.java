package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

public class CypherQueryBuilderAdmin extends AbstractCypherQueryBuilder {

  public static String wipeAllData() {
    return " MATCH (n:" + NodeLabel.SimpleLabel.SCOPE + ") DETACH DELETE n";
  }

  public static String createUniqueConstraint(NodeLabel nodeLabel, NodeProperty property) {
    return " CREATE CONSTRAINT ON (n:" + nodeLabel.getSimpleLabel() + ") ASSERT n."
        + property.getValue() + " IS UNIQUE";
  }

  public static String createIndex(NodeLabel nodeLabel, NodeProperty property) {
    return " CREATE INDEX ON :" + nodeLabel.getSimpleLabel() + "(" + property.getValue() + ")";
  }
}
