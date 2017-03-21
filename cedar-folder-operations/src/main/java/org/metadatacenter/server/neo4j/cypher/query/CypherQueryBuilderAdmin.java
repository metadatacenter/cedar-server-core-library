package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.NodeLabel;

public class CypherQueryBuilderAdmin extends AbstractCypherQueryBuilder {

  public static String wipeAllData() {
    return " MATCH (n:" + NodeLabel.PlainLabels.SCOPE + ") DETACH DELETE n";
  }
}
