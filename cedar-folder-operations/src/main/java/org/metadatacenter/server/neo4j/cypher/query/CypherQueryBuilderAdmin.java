package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.server.neo4j.NodeLabel;

public class CypherQueryBuilderAdmin extends AbstractCypherQueryBuilder {

  public static String wipeAllData() {
    StringBuilder sb = new StringBuilder();
    sb.append(" MATCH (n:").append(NodeLabel.PlainLabels.SCOPE).append(") DETACH DELETE n");
    return sb.toString();
  }
}
