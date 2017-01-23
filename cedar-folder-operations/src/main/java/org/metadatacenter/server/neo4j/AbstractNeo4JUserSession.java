package org.metadatacenter.server.neo4j;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.server.ServiceSession;
import org.metadatacenter.server.neo4j.proxy.Neo4JProxies;
import org.metadatacenter.server.security.model.user.CedarUser;

public abstract class AbstractNeo4JUserSession implements ServiceSession {

  protected Neo4JProxies proxies;
  protected CedarUser cu;
  protected String userIdPrefix;
  protected String groupIdPrefix;
  protected CedarConfig cedarConfig;

  public AbstractNeo4JUserSession(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu, String userIdPrefix,
                                  String groupIdPrefix) {
    this.cedarConfig = cedarConfig;
    this.proxies = proxies;
    this.cu = cu;
    this.userIdPrefix = userIdPrefix;
    this.groupIdPrefix = groupIdPrefix;
  }

  @Override
  public String getUserId() {
    // let the NPE, something is really wrong if that happens
    return userIdPrefix + cu.getId();
  }

  protected String buildGroupId(String groupUUID) {
    return groupIdPrefix + groupUUID;
  }
}