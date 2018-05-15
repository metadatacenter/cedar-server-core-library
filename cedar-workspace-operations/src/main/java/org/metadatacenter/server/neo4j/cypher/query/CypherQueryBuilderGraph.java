package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.RelationLabel;

public class CypherQueryBuilderGraph extends AbstractCypherQueryBuilder {


  public static String getOutgoingArcs() {
    return "" +
        "MATCH" +
        " (s {id:{nodeId}})-[r]->(t) RETURN s.id AS sid, TYPE(r) AS type, t.id AS tid ORDER BY s.id, t.id, type(r)";
  }

  public static String getIncomingArcs() {
    return "" +
        "MATCH" +
        " (s)-[r]->(t {id:{nodeId}}) RETURN s.id AS sid, TYPE(r) AS type, t.id AS tid ORDER BY s.id, t.id, type(r)";
  }

  public static String createArc(RelationLabel relationLabel) {
    return "" +
        " MATCH (source {id:{sourceId}})" +
        " MATCH (target {id:{targetId}})" +
        " CREATE (source)-[:" + relationLabel.getValue() + "]->(target)" +
        " RETURN source";
  }
}
