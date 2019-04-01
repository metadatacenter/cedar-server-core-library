package org.metadatacenter.bridge;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.config.MongoConnection;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.*;
import org.metadatacenter.server.neo4j.proxy.*;
import org.metadatacenter.server.service.UserService;
import org.metadatacenter.server.service.mongodb.UserServiceMongoDB;
import org.metadatacenter.server.service.neo4j.UserServiceNeo4j;
import org.metadatacenter.util.mongo.MongoClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class CedarDataServices {

  private static final Logger log = LoggerFactory.getLogger(CedarDataServices.class);

  private UserService mongoUserService;
  private UserService neoUserService;
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

  public static void initializeMongoUserService(CedarConfig cedarConfig) {
    instance.mongoUserService = new UserServiceMongoDB(
        instance.mongoClientFactoryForUsers.getClient(),
        cedarConfig.getUserServerConfig().getDatabaseName(),
        cedarConfig.getUserServerConfig().getMongoCollectionName(CedarNodeType.USER));
  }

  public static void initializeNeo4jServices(CedarConfig cedarConfig) {
    instance.cedarConfig = cedarConfig;
    instance.proxies = new Neo4JProxies(cedarConfig);
    instance.neoUserService = new UserServiceNeo4j(instance.proxies.user());
  }

  public static GroupServiceSession getGroupServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Neo4j services:CedarDataServices.initializeNeo4jServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionGroupService
          .get(instance.cedarConfig, instance.proxies, context.getCedarUser(), context.getGlobalRequestIdHeader(),
              context.getLocalRequestIdHeader());
    }
  }

  public static GraphServiceSession getGraphServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Neo4j services:CedarDataServices.initializeNeo4jServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionGraphService
          .get(instance.cedarConfig, instance.proxies, context.getCedarUser(), context.getGlobalRequestIdHeader(),
              context.getLocalRequestIdHeader());
    }
  }

  public static PermissionServiceSession getPermissionServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Neo4j services:CedarDataServices.initializeNeo4jServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionPermissionService
          .get(instance.cedarConfig, instance.proxies, context.getCedarUser(), context.getGlobalRequestIdHeader(),
              context.getLocalRequestIdHeader());
    }
  }

  public static VersionServiceSession getVersionServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Neo4j services:CedarDataServices.initializeNeo4jServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionVersionService
          .get(instance.cedarConfig, instance.proxies, context.getCedarUser(), context.getGlobalRequestIdHeader(),
              context.getLocalRequestIdHeader());
    }
  }

  public static AdminServiceSession getAdminServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Neo4j services:CedarDataServices.initializeNeo4jServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionAdminService
          .get(instance.cedarConfig, instance.proxies, context.getCedarUser(), context.getGlobalRequestIdHeader(),
              context.getLocalRequestIdHeader());
    }
  }

  public static FolderServiceSession getFolderServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Neo4j services:CedarDataServices.initializeNeo4jServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionFolderService.get(instance.cedarConfig, instance.proxies, context.getCedarUser(),
          context.getGlobalRequestIdHeader(),
          context.getLocalRequestIdHeader());
    }
  }

  public static UserServiceSession getUserServiceSession(CedarRequestContext context) {
    if (instance.proxies == null) {
      log.error("You need to initialize Neo4j services:CedarDataServices.initializeNeo4jServices(cedarConfig)");
      System.exit(-2);
      return null;
    } else {
      return Neo4JUserSessionUserService
          .get(instance.cedarConfig, instance.proxies, context.getCedarUser(), context.getGlobalRequestIdHeader(),
              context.getLocalRequestIdHeader());
    }
  }

  public static UserService getMongoUserService() {
    if (instance.mongoUserService == null) {
      log.error("You need to initialize mongo user service: CedarDataServices.initializeMongoUserService(cedarConfig)");
      System.exit(-1);
      return null;
    } else {
      return instance.mongoUserService;
    }
  }

  public static UserService getNeoUserService() {
    if (instance.neoUserService == null) {
      log.error("You need to initialize neo user service: CedarDataServices.initializeNeoUserService()");
      System.exit(-1);
      return null;
    } else {
      return instance.neoUserService;
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
