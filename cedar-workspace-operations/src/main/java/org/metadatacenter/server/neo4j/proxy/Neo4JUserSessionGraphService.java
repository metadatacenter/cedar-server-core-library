package org.metadatacenter.server.neo4j.proxy;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.FolderServerArc;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.server.GraphServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.List;

public class Neo4JUserSessionGraphService extends AbstractNeo4JUserSession implements GraphServiceSession {

  private Neo4JUserSessionGraphService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu,
                                       String globalRequestId, String localRequestId) {
    super(cedarConfig, proxies, cu, globalRequestId, localRequestId);
  }

  public static GraphServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser,
                                        String globalRequestId, String localRequestId) {
    return new Neo4JUserSessionGraphService(cedarConfig, proxies, cedarUser, globalRequestId, localRequestId);
  }

  @Override
  public List<FolderServerArc> getOutgoingArcs(String nodeId) {
    return proxies.graph().getOutgoingArcs(nodeId);
  }

  @Override
  public List<FolderServerArc> getIncomingArcs(String nodeId) {
    return proxies.graph().getIncomingArcs(nodeId);
  }

  @Override
  public FolderServerUser createUser(JsonNode node) {
    return proxies.graph().createUser(node);
  }

  @Override
  public FolderServerGroup createGroup(JsonNode node) {
    return proxies.graph().createGroup(node);
  }

  @Override
  public FolderServerNode createNode(JsonNode node) {
    return proxies.graph().createNode(node);
  }

  @Override
  public boolean createArc(String sourceId, RelationLabel relationLabel, String targetId) {
    return proxies.graph().createArc(sourceId, relationLabel, targetId);
  }

}
