package org.metadatacenter.bridge;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.*;
import org.metadatacenter.server.neo4j.Neo4jConfig;
import org.metadatacenter.server.neo4j.proxy.*;
import org.metadatacenter.server.service.UserService;
import org.metadatacenter.server.service.mongodb.UserServiceMongoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CedarDataServices {

  private static final Logger log = LoggerFactory.getLogger(CedarDataServices.class);

  private UserService userService;
  private Neo4JProxies proxies;
  private CedarConfig cedarConfig;
  private static CedarDataServices instance = new CedarDataServices();

  private CedarDataServices() {
  }

  public static void initializeUserService(CedarConfig cedarConfig) {
    instance.userService = new UserServiceMongoDB(cedarConfig.getMongoConfig().getDatabaseName(),
        cedarConfig.getMongoCollectionName(CedarNodeType.USER));
  }

  public static void initializeFolderServices(CedarConfig cedarConfig) {
    instance.cedarConfig = cedarConfig;
    Neo4jConfig neo4jConfig = Neo4jConfig.fromCedarConfig(cedarConfig);
    instance.proxies = new Neo4JProxies(neo4jConfig, cedarConfig.buildLinkedDataUtil());
  }

  public static GroupServiceSession getGroupServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize folder services: CedarDataServices.initializeFolderServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionGroupService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static PermissionServiceSession getPermissionServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize folder services: CedarDataServices.initializeFolderServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionPermissionService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static AdminServiceSession getAdminServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize folder services: CedarDataServices.initializeFolderServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionAdminService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static FolderServiceSession getFolderServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize folder services: CedarDataServices.initializeFolderServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionFolderService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static UserServiceSession getUserServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize folder services: CedarDataServices.initializeFolderServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionUserService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static UserService getUserService() {
    if (instance.userService == null) {
      log.error("You need to initialize user service: CedarDataServices.initializeUserService(cedarConfig)");
      System.exit(-1);
      return null;
    } else {
      return instance.userService;
    }
  }

}
