package org.metadatacenter.server.neo4j.cypher.query;

import org.metadatacenter.model.RelationLabel;

public class CypherQueryBuilderGraph extends AbstractCypherQueryBuilder {


  public static String getOutgoingArcs() {
    return "" +
        "MATCH" +
        " (s {<PROP.ID>:{<PROP.ID>}})-[r]->(t) RETURN s.id AS sid, TYPE(r) AS type, t.id AS tid ORDER BY s.id, t.id, type(r)";
  }

  public static String getIncomingArcs() {
    return "" +
        "MATCH" +
        " (s)-[r]->(t {<PROP.ID>:{<PROP.ID>}}) RETURN s.id AS sid, TYPE(r) AS type, t.id AS tid ORDER BY s.id, t.id, type(r)";
  }

  public static String createArc(RelationLabel relationLabel) {
    return "" +
        " MATCH (source {<PROP.ID>:{sourceId}})" +
        " MATCH (target {<PROP.ID>:{targetId}})" +
        " CREATE (source)-[:" + relationLabel.getValue() + "]->(target)" +
        " RETURN source";
  }
}
