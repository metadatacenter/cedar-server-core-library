package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.folderserver.FolderServerArc;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderGraph;
import org.metadatacenter.server.neo4j.cypher.query.CypherQueryBuilderGraph;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;
import org.metadatacenter.util.json.JsonMapper;

import java.util.ArrayList;
import java.util.List;

public class Neo4JProxyGraph extends AbstractNeo4JProxy {

  Neo4JProxyGraph(Neo4JProxies proxies) {
    super(proxies);
  }


  public List<FolderServerArc> getOutgoingArcs(String nodeId) {
    String cypher = CypherQueryBuilderGraph.getOutgoingArcs();
    CypherParameters params = CypherParamBuilderGraph.matchNodeId(nodeId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    List<FolderServerArc> arcList = new ArrayList<>();

    JsonNode resourceListJsonNode = jsonNode.at("/results/0/data");
    if (resourceListJsonNode != null && !resourceListJsonNode.isMissingNode()) {
      resourceListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row");
        FolderServerArc arc = buildArc(nodeNode);
        if (arc != null) {
          arcList.add(arc);
        }
      });
    }

    return arcList;
  }

  public List<FolderServerArc> getIncomingArcs(String nodeId) {
    String cypher = CypherQueryBuilderGraph.getIncomingArcs();
    CypherParameters params = CypherParamBuilderGraph.matchNodeId(nodeId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    List<FolderServerArc> arcList = new ArrayList<>();

    JsonNode resourceListJsonNode = jsonNode.at("/results/0/data");
    if (resourceListJsonNode != null && !resourceListJsonNode.isMissingNode()) {
      resourceListJsonNode.forEach(f -> {
        JsonNode nodeNode = f.at("/row");
        FolderServerArc arc = buildArc(nodeNode);
        if (arc != null) {
          arcList.add(arc);
        }
      });
    }

    return arcList;
  }
}
