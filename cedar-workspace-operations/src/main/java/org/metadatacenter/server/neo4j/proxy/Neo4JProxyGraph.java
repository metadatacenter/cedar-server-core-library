package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.neo4j.CypherQuery;
import org.metadatacenter.server.neo4j.CypherQueryWithParameters;
import org.metadatacenter.server.neo4j.cypher.parameter.AbstractCypherParamBuilder;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderGraph;
import org.metadatacenter.server.neo4j.cypher.parameter.CypherParamBuilderUser;
import org.metadatacenter.server.neo4j.cypher.query.*;
import org.metadatacenter.server.neo4j.parameter.CypherParameters;

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

  public FolderServerUser createUser(JsonNode node) {
    String cypher = CypherQueryBuilderUser.createUser();
    CypherParameters params = CypherParamBuilderUser.mapAllProperties(node);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode userNode = jsonNode.at("/results/0/data/0/row/0");
    return proxies.user().buildUser(userNode);
  }

  public FolderServerGroup createGroup(JsonNode node) {
    String cypher = CypherQueryBuilderGroup.createGroup();
    CypherParameters params = CypherParamBuilderUser.mapAllProperties(node);
    CypherParamBuilderGraph.tweakGroupProperties(node, params);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode groupNode = jsonNode.at("/results/0/data/0/row/0");
    return proxies.user().buildGroup(groupNode);
  }

  public FolderServerNode createNode(JsonNode node) {
    FolderServerNode folderServerNode = proxies.folder().buildNode(node);
    CedarNodeType type = folderServerNode.getType();
    String cypher = null;

    if (type == CedarNodeType.FOLDER) {
      FolderServerFolder fsFolder = proxies.folder().buildFolder(node);
      cypher = CypherQueryBuilderFolder.createFolderWithoutParent(fsFolder);
    } else {
      FolderServerResource fsResource = proxies.resource().buildResource(node);
      cypher = CypherQueryBuilderResource.createResourceWithoutParent(fsResource);
    }
    CypherParameters params = CypherParamBuilderGraph.mapAllProperties(node);
    CypherParamBuilderGraph.tweakNodeProperties(node, params);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    JsonNode nodeNode = jsonNode.at("/results/0/data/0/row/0");
    return proxies.node().buildNode(nodeNode);
  }

  public boolean createArc(String sourceId, RelationLabel relationLabel, String targetId) {
    String cypher = CypherQueryBuilderGraph.createArc(relationLabel);
    CypherParameters params = AbstractCypherParamBuilder.matchSourceAndTarget(sourceId, targetId);
    CypherQuery q = new CypherQueryWithParameters(cypher, params);
    JsonNode jsonNode = executeCypherQueryAndCommit(q);
    return successOrLogAndThrowException(jsonNode, "Error while creating arc:");
  }
}
