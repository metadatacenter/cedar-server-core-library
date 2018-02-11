package org.metadatacenter.server.neo4j;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.neo4j.proxy.Neo4JProxies;
import org.metadatacenter.server.security.model.user.CedarUser;

public abstract class AbstractNeo4JUserSession {

  protected final Neo4JProxies proxies;
  protected final CedarUser cu;
  protected final CedarConfig cedarConfig;
  protected final LinkedDataUtil linkedDataUtil;

  public AbstractNeo4JUserSession(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu) {
    this.cedarConfig = cedarConfig;
    this.linkedDataUtil = cedarConfig.getLinkedDataUtil();
    this.proxies = proxies;
    this.cu = cu;
  }
}