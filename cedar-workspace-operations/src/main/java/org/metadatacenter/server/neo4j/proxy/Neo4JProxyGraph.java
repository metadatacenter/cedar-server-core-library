package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.model.folderserver.basic.*;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderGraph;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderUser;
import org.metadatacenter.server.neo4j.cypher.query.*;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

import java.util.List;

public class Neo4JProxyGraph extends AbstractNeo4JProxy {

  Neo4JProxyGraph(Neo4JProxies proxies, CedarConfig cedarConfig) {
    super(proxies, cedarConfig);
  }

  public List<FolderServerArc> getOutgoingArcs(String nodeId) {
    String cypher = CypherQueryBuilderGraph.getOutgoingArcs();
    CypherParameters params = CypherParamBuilderGraph.matchNodeId(nodeId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetArcList(q);
  }

  public List<FolderServerArc> getIncomingArcs(String nodeId) {
    String cypher = CypherQueryBuilderGraph.getIncomingArcs();
    CypherParameters params = CypherParamBuilderGraph.matchNodeId(nodeId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeReadGetArcList(q);
  }

  public FolderServerUser createUser(JsonNode node) {
    String cypher = CypherQueryBuilderUser.createUser();
    CypherParameters params = CypherParamBuilderUser.mapAllProperties(node);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerUser.class);
  }

  public FolderServerGroup createGroup(JsonNode node) {
    String cypher = CypherQueryBuilderGroup.createGroup();
    CypherParameters params = CypherParamBuilderUser.mapAllProperties(node);
    CypherParamBuilderGraph.tweakGroupProperties(node, params);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerGroup.class);
  }

  public FolderServerNode createNode(JsonNode node) {
    FolderServerNode folderServerNode = buildNode(node);
    CedarNodeType type = folderServerNode.getType();
    String cypher = null;

    if (type == CedarNodeType.FOLDER) {
      FolderServerFolder fsFolder = buildFolder(node);
      cypher = CypherQueryBuilderFolder.createFolderWithoutParent(fsFolder);
    } else {
      FolderServerResource fsResource = buildResource(node);
      cypher = CypherQueryBuilderResource.createResourceWithoutParent(fsResource);
    }
    CypherParameters params = CypherParamBuilderGraph.mapAllProperties(node);
    CypherParamBuilderGraph.tweakNodeProperties(node, params);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWriteGetOne(q, FolderServerNode.class);
  }

  public boolean createArc(String sourceId, RelationLabel relationLabel, String targetId) {
    String cypher = CypherQueryBuilderGraph.createArc(relationLabel);
    CypherParameters params = AbstractCypherParamBuilder.matchSourceAndTarget(sourceId, targetId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    return executeWrite(q, "creating arc");
  }
}
