package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.RelationLabel;
import org.metadatacenter.model.folderserver.FolderServerArc;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.GraphServiceSession;
import org.metadatacenter.server.GroupServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.CedarGroupUser;
import org.metadatacenter.server.security.model.auth.CedarGroupUsers;
import org.metadatacenter.server.security.model.auth.CedarGroupUsersRequest;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.*;

public class Neo4JUserSessionGraphService extends AbstractNeo4JUserSession implements GraphServiceSession {

  public Neo4JUserSessionGraphService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu) {
    super(cedarConfig, proxies, cu);
  }

  public static GraphServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser) {
    return new Neo4JUserSessionGraphService(cedarConfig, proxies, cedarUser);
  }

  @Override
  public List<FolderServerArc> getOutgoingArcs(String nodeId) {
    return proxies.graph().getOutgoingArcs(nodeId);
  }

  @Override
  public List<FolderServerArc> getIncomingArcs(String nodeId) {
    return proxies.graph().getIncomingArcs(nodeId);
  }
}
