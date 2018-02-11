package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.neo4j.Neo4jConfig;
import org.metadatacenter.server.neo4j.PathUtil;

public class Neo4JProxies {

  protected final Neo4jConfig config;
  protected final PathUtil pathUtil;
  protected final LinkedDataUtil linkedDataUtil;

  private final Neo4JProxyAdmin adminProxy;
  private final Neo4JProxyFolder folderProxy;
  private final Neo4JProxyGroup groupProxy;
  private final Neo4JProxyUser userProxy;
  private final Neo4JProxyPermission permissionProxy;
  private final Neo4JProxyResource resourceProxy;
  private final Neo4JProxyNode nodeProxy;
  private final Neo4JProxyGraph graphProxy;

  public Neo4JProxies(Neo4jConfig config, LinkedDataUtil linkedDataUtil) {
    this.config = config;
    this.linkedDataUtil = linkedDataUtil;
    this.pathUtil = new Neo4JPathUtil(config);

    this.adminProxy = new Neo4JProxyAdmin(this);
    this.folderProxy = new Neo4JProxyFolder(this);
    this.groupProxy = new Neo4JProxyGroup(this);
    this.userProxy = new Neo4JProxyUser(this);
    this.permissionProxy = new Neo4JProxyPermission(this);
    this.resourceProxy = new Neo4JProxyResource(this);
    this.nodeProxy = new Neo4JProxyNode(this);
    this.graphProxy = new Neo4JProxyGraph(this);
  }

  public Neo4JProxyAdmin admin() {
    return adminProxy;
  }

  public Neo4JProxyFolder folder() {
    return folderProxy;
  }

  public Neo4JProxyGroup group() {
    return groupProxy;
  }

  public Neo4JProxyUser user() {
    return userProxy;
  }

  public Neo4JProxyPermission permission() {
    return permissionProxy;
  }

  public Neo4JProxyResource resource() {
    return resourceProxy;
  }

  public Neo4JProxyNode node() {
    return nodeProxy;
  }

  public Neo4JProxyGraph graph() {
    return graphProxy;
  }

  public LinkedDataUtil getLinkedDataUtil() {
    return linkedDataUtil;
  }
}
