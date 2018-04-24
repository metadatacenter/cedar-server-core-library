package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryLiteral;
import org.metadatacenter.server.neo4j.NodeLabel;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderAdmin;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;

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

  boolean createUniqueConstraint(NodeLabel nodeLabel, NodeProperty property) {
    String cypher = CypherQueryBuilderAdmin.createUniqueConstraint(nodeLabel, property);
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while creating unique constraint:");
  }

  boolean createIndex(NodeLabel nodeLabel, NodeProperty property) {
    String cypher = CypherQueryBuilderAdmin.createIndex(nodeLabel, property);
    CypherQuery q = new CypherQueryLiteral(cypher);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while creating index:");
  }
}
