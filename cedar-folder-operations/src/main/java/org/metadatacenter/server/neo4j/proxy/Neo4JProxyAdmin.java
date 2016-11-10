package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryBuilder;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;

public class Neo4JProxyAdmin extends AbstractNeo4JProxy {

  Neo4JProxyAdmin(Neo4JProxies proxies) {
    super(proxies);
  }

  boolean wipeAllData() {
    String cypher = CypherQueryBuilder.wipeAllData();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode errorsNode = jsonNode.at("/errors");
    if (errorsNode.size() != 0) {
      JsonNode error = errorsNode.path(0);
      log.warn("Error while deleting all data:", error);
    }
    return errorsNode.size() == 0;
  }

}
