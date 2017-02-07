package org.metadatacenter.bridge;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.*;
import org.metadatacenter.server.neo4j.Neo4jConfig;
import org.metadatacenter.server.neo4j.proxy.*;
import org.metadatacenter.server.service.UserService;
import org.metadatacenter.server.service.mongodb.UserServiceMongoDB;

public final class CedarDataServices {

  private UserService userService;
  private Neo4JProxies proxies;
  private CedarConfig cedarConfig;
  private static CedarDataServices instance = null;

  public static synchronized CedarDataServices getInstance(CedarConfig cedarConfig) {
    if (instance == null) {
      instance = buildCedarDataServices(cedarConfig);
    }
    return instance;
  }

  private CedarDataServices() {
  }

  private static CedarDataServices buildCedarDataServices(CedarConfig cedarConfig) {
    CedarDataServices cds = new CedarDataServices();
    cds.cedarConfig = cedarConfig;

    cds.userService = new UserServiceMongoDB(cedarConfig.getMongoConfig().getDatabaseName(),
        cedarConfig.getMongoCollectionName(CedarNodeType.USER));

    Neo4jConfig neo4jConfig = Neo4jConfig.fromCedarConfig(cedarConfig);

    cds.proxies = new Neo4JProxies(neo4jConfig, cedarConfig.buildLinkedDataUtil());
    return cds;
  }

  public static GroupServiceSession getGroupServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionGroupService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
  }

  public static PermissionServiceSession getPermissionServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionPermissionService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
  }

  public static AdminServiceSession getAdminServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionAdminService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
  }

  public static FolderServiceSession getFolderServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionFolderService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
  }

  public static UserServiceSession getUserServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionUserService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
  }

  public static UserService getUserService() {
    return instance.userService;
  }

}
