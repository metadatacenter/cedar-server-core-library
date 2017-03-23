package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderAdmin;

public class Neo4JProxyAdmin extends AbstractNeo4JProxy {

  Neo4JProxyAdmin(Neo4JProxies proxies) {
    super(proxies);
  }

  boolean wipeAllData() {
    String cypher = CypherQueryBuilderAdmin.wipeAllData();
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while deleting all data:");
  }

}
