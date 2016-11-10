package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.neo4j.Neo4jConfig;
import org.metadatacenter.server.neo4j.PathUtil;

public class Neo4JProxies {

  protected final Neo4jConfig config;
  protected final String genericIdPrefix;
  protected final String folderIdPrefix;
  protected final String userIdPrefix;
  protected final String groupIdPrefix;
  protected final PathUtil pathUtil;

  private Neo4JProxyAdmin adminProxy;
  private Neo4JProxyFolder folderProxy;
  private Neo4JProxyGroup groupProxy;
  private Neo4JProxyUser userProxy;
  private Neo4JProxyPermission permissionProxy;
  private Neo4JProxyResource resourceProxy;
  private Neo4JProxyNode nodeProxy;

  public Neo4JProxies(Neo4jConfig config, String genericIdPrefix, String userIdPrefix) {
    this.config = config;
    this.genericIdPrefix = genericIdPrefix;
    this.userIdPrefix = userIdPrefix;
    this.folderIdPrefix = getNodeTypeFullPrefix(CedarNodeType.FOLDER);
    this.groupIdPrefix = getNodeTypeFullPrefix(CedarNodeType.GROUP);
    this.pathUtil = new Neo4JPathUtil(config);

    this.adminProxy = new Neo4JProxyAdmin(this);
    this.folderProxy = new Neo4JProxyFolder(this);
    this.groupProxy = new Neo4JProxyGroup(this);
    this.userProxy = new Neo4JProxyUser(this);
    this.permissionProxy = new Neo4JProxyPermission(this);
    this.resourceProxy = new Neo4JProxyResource(this);
    this.nodeProxy = new Neo4JProxyNode(this);
  }

  public String getNodeTypeFullPrefix(CedarNodeType nodeType) {
    return genericIdPrefix + nodeType.getPrefix() + "/";
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

  String getUserIdPrefix() {
    return userIdPrefix;
  }

  String getGroupIdPrefix() {
    return groupIdPrefix;
  }
}
