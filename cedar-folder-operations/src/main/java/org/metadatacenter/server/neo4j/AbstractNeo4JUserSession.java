package org.metadatacenter.server.neo4j;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.ServiceSession;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.neo4j.proxy.Neo4JProxies;
import org.metadatacenter.server.security.model.user.CedarUser;

public abstract class AbstractNeo4JUserSession implements ServiceSession {

  protected Neo4JProxies proxies;
  protected CedarUser cu;
  protected CedarConfig cedarConfig;
  protected LinkedDataUtil linkedDataUtil;

  public AbstractNeo4JUserSession(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu) {
    this.cedarConfig = cedarConfig;
    this.linkedDataUtil = cedarConfig.buildLinkedDataUtil();
    this.proxies = proxies;
    this.cu = cu;
  }

  @Override
  public String getUserId() {
    // let the NPE, something is really wrong if that happens
    return linkedDataUtil.getLinkedDataId(CedarNodeType.USER, cu.getId());
  }
}