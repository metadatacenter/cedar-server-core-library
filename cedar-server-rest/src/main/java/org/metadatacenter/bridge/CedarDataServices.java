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

  private static final CedarDataServices instance = new CedarDataServices();

  private final UserService userService;
  private final Neo4JProxies proxies;

  private CedarDataServices() {
    CedarConfig cedarConfig = CedarConfig.getInstance();

    userService = new UserServiceMongoDB(cedarConfig.getMongoConfig().getDatabaseName(),
        cedarConfig.getMongoCollectionName(CedarNodeType.USER));

    Neo4jConfig neo4jConfig = Neo4jConfig.fromCedarConfig(cedarConfig);
    String genericIdPrefix = cedarConfig.getLinkedDataConfig().getBase();
    String userIdPrefix = cedarConfig.getLinkedDataConfig().getUsersBase();

    proxies = new Neo4JProxies(neo4jConfig, genericIdPrefix, userIdPrefix);
  }

  public static GroupServiceSession getGroupServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionGroupService.get(instance.proxies, context.getCedarUser());
  }

  public static PermissionServiceSession getPermissionServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionPermissionService.get(instance.proxies, context.getCedarUser());
  }

  public static AdminServiceSession getAdminServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionAdminService.get(instance.proxies, context.getCedarUser());
  }

  public static FolderServiceSession getFolderServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionFolderService.get(instance.proxies, context.getCedarUser());
  }

  public static UserServiceSession getUserServiceSession(CedarRequestContext context) {
    return Neo4JUserSessionUserService.get(instance.proxies, context.getCedarUser());
  }

  public static UserService getUserService() {
    return instance.userService;
  }

}
