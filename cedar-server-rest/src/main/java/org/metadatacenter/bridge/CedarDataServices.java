package org.metadatacenter.bridge;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.config.MongoConnection;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.*;
import org.metadatacenter.server.neo4j.Neo4jConfig;
import org.metadatacenter.server.neo4j.proxy.*;
import org.metadatacenter.server.service.UserService;
import org.metadatacenter.server.service.mongodb.UserServiceMongoDB;
import org.metadatacenter.util.mongo.MongoClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CedarDataServices {

  private static final Logger log = LoggerFactory.getLogger(CedarDataServices.class);

  private UserService userService;
  private Neo4JProxies proxies;
  private CedarConfig cedarConfig;
  private MongoClientFactory mongoClientFactoryForDocuments;
  private MongoClientFactory mongoClientFactoryForUsers;
  private static final CedarDataServices instance = new CedarDataServices();

  private CedarDataServices() {
  }

  public static void initializeMongoClientFactoryForDocuments(MongoConnection mongoConnection) {
    instance.mongoClientFactoryForDocuments = new MongoClientFactory(mongoConnection);
    instance.mongoClientFactoryForDocuments.buildClient();
  }

  public static void initializeMongoClientFactoryForUsers(MongoConnection mongoConnection) {
    instance.mongoClientFactoryForUsers = new MongoClientFactory(mongoConnection);
    instance.mongoClientFactoryForUsers.buildClient();
  }

  public static void initializeUserService(CedarConfig cedarConfig) {
    instance.userService = new UserServiceMongoDB(
        instance.mongoClientFactoryForUsers.getClient(),
        cedarConfig.getUserServerConfig().getDatabaseName(),
        cedarConfig.getUserServerConfig().getMongoCollectionName(CedarNodeType.USER));
  }

  public static void initializeWorkspaceServices(CedarConfig cedarConfig) {
    instance.cedarConfig = cedarConfig;
    Neo4jConfig neo4jConfig = Neo4jConfig.fromCedarConfig(cedarConfig);
    instance.proxies = new Neo4JProxies(neo4jConfig, cedarConfig.getLinkedDataUtil());
  }

  public static GroupServiceSession getGroupServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Workspace services: CedarDataServices.initializeWorkspaceServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionGroupService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static GraphServiceSession getGraphServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Workspace services: CedarDataServices.initializeWorkspaceServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionGraphService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static PermissionServiceSession getPermissionServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Workspace services: CedarDataServices.initializeWorkspaceServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionPermissionService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static AdminServiceSession getAdminServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Workspace services: CedarDataServices.initializeWorkspaceServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionAdminService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static FolderServiceSession getFolderServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Workspace services: CedarDataServices.initializeWorkspaceServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionFolderService.get(instance.cedarConfig, instance.proxies, context.getCedarUser());
    }
  }

  public static UserServiceSession getUserServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Workspace services: CedarDataServices.initializeWorkspaceServices(cedarConfig)");
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

  public static MongoClientFactory getMongoClientFactoryForDocuments() {
    if (instance.mongoClientFactoryForDocuments == null) {
      log.error("You need to initialize mongoClientFactory: " +
          "CedarDataServices.initializeMongoClientFactoryForDocuments(mongoConnection)");
      System.exit(-1);
      return null;
    } else {
      return instance.mongoClientFactoryForDocuments;
    }
  }

  public static MongoClientFactory getMongoClientFactoryForUsers() {
    if (instance.mongoClientFactoryForUsers == null) {
      log.error("You need to initialize mongoClientFactory: " +
          "CedarDataServices.initializeMongoClientFactoryForUsers(mongoConnection)");
      System.exit(-1);
      return null;
    } else {
      return instance.mongoClientFactoryForUsers;
    }
  }

}
