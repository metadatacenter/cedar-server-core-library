package org.metadatacenter.server.neo4j.cypher.query;

public class CypherQueryBuilderGraph extends AbstractCypherQueryBuilder {


  public static String getOutgoingArcs() {
    return "" +
        "MATCH (s {id:{nodeId}})-[r]->(t) RETURN s.id, TYPE(r), t.id";
  }

  public static String getIncomingArcs() {
    return "" +
        "MATCH (s)-[r]->(t {id:{nodeId}}) RETURN s.id, TYPE(r), t.id";
  }
}
