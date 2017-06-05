package org.metadatacenter.config.environment;

import org.metadatacenter.model.SystemComponent;

import java.util.*;

public class CedarConfigEnvironmentDescriptor {

  private final static Map<CedarEnvironmentVariable, Set<SystemComponent>> variableToComponent;
  private final static Map<SystemComponent, Set<CedarEnvironmentVariable>> componentToVariable;

  static {
    List<SystemComponent> allMicroservices = new ArrayList<>();
    for (SystemComponent sc : SystemComponent.values()) {
      if (sc.getServerName() != null) {
        allMicroservices.add(sc);
      }
    }

    variableToComponent = new LinkedHashMap<>();
    for (CedarEnvironmentVariable env : CedarEnvironmentVariable.values()) {
      Set<SystemComponent> systemComponents = new LinkedHashSet<>();
      systemComponents.add(SystemComponent.ALL);
      variableToComponent.put(env, systemComponents);
    }

    Set<SystemComponent> cedarVersion = variableToComponent.get(CedarEnvironmentVariable.CEDAR_VERSION);
    // gulpfile.js replaces in version.js
    cedarVersion.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarVersion.add(SystemComponent.FRONTEND_TEST);
    cedarVersion.add(SystemComponent.FRONTEND_PRODUCTION);

    Set<SystemComponent> cedarVersionModifier = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_VERSION_MODIFIER);
    // gulpfile.js replaces in version.js
    cedarVersionModifier.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarVersionModifier.add(SystemComponent.FRONTEND_TEST);
    cedarVersionModifier.add(SystemComponent.FRONTEND_PRODUCTION);

    Set<SystemComponent> cedarHome = variableToComponent.get(CedarEnvironmentVariable.CEDAR_HOME);
    cedarHome.add(SystemComponent.ADMIN_TOOL); // generate-nginx-config, export dir
    cedarHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    Set<SystemComponent> keycloakHome = variableToComponent.get(CedarEnvironmentVariable.KEYCLOAK_HOME);
    keycloakHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    Set<SystemComponent> nginxHome = variableToComponent.get(CedarEnvironmentVariable.NGINX_HOME);
    nginxHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    Set<SystemComponent> cedarFrontendBeh = variableToComponent.get(CedarEnvironmentVariable.CEDAR_FRONTEND_BEHAVIOR);
    // gulpfile.js decides that it should or not start a server for the front end code
    cedarFrontendBeh.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarFrontendBeh.add(SystemComponent.FRONTEND_TEST);
    cedarFrontendBeh.add(SystemComponent.FRONTEND_PRODUCTION);

    Set<SystemComponent> cedarFrontendTarget = variableToComponent.get(CedarEnvironmentVariable.CEDAR_FRONTEND_TARGET);
    // gulpfile.js replaces this in several files, frontend connects to this server
    cedarFrontendTarget.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarFrontendTarget.add(SystemComponent.FRONTEND_TEST);
    cedarFrontendTarget.add(SystemComponent.FRONTEND_PRODUCTION);

    Set<SystemComponent> cedarHost = variableToComponent.get(CedarEnvironmentVariable.CEDAR_HOST);
    cedarHost.add(SystemComponent.ADMIN_TOOL); // generate-nginx-config, all Keycloak related tasks
    // gulpfile.js replaces values in url-service.conf.json and test-env.js
    cedarHost.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarHost.add(SystemComponent.FRONTEND_TEST);
    cedarHost.add(SystemComponent.FRONTEND_PRODUCTION);
    // all the microservices
    cedarHost.addAll(allMicroservices);

    Set<SystemComponent> cedarBioportalApiKey = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_BIOPORTAL_API_KEY);
    cedarBioportalApiKey.add(SystemComponent.SERVER_TERMINOLOGY);

    Set<SystemComponent> cedarBioportalRestBase = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_BIOPORTAL_REST_BASE);
    cedarBioportalRestBase.add(SystemComponent.SERVER_TERMINOLOGY);

    Set<SystemComponent> cedarAnalyticsKey = variableToComponent.get(CedarEnvironmentVariable.CEDAR_ANALYTICS_KEY);
    // gulpfile.js replaces int tracking-service.conf.json
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_TEST);
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_PRODUCTION);

    Set<SystemComponent> cedarNcbiSraFtpPwd = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_NCBI_SRA_FTP_PASSWORD);
    cedarNcbiSraFtpPwd.add(SystemComponent.SERVER_SUBMISSION);

    Set<SystemComponent> cedarNeo4jUserName = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_NEO4J_USER_NAME);
    cedarNeo4jUserName.add(SystemComponent.ADMIN_TOOL); // reset tasks
    cedarNeo4jUserName.add(SystemComponent.SERVER_FOLDER); // storage for file system
    cedarNeo4jUserName.add(SystemComponent.SERVER_GROUP); // storage for groups
    cedarNeo4jUserName.add(SystemComponent.SERVER_RESOURCE); // permission tests
    cedarNeo4jUserName.add(SystemComponent.SERVER_WORKER); // permission changes

    Set<SystemComponent> cedarNeo4jUserPassword = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_NEO4J_USER_PASSWORD);
    cedarNeo4jUserPassword.addAll(cedarNeo4jUserName);

    Set<SystemComponent> cedarNeo4jHost = variableToComponent.get(CedarEnvironmentVariable.CEDAR_NEO4J_HOST);
    cedarNeo4jHost.addAll(cedarNeo4jUserName);

    Set<SystemComponent> cedarNeo4jRestPortd = variableToComponent.get(CedarEnvironmentVariable.CEDAR_NEO4J_REST_PORT);
    cedarNeo4jRestPortd.addAll(cedarNeo4jUserName);

    Set<SystemComponent> cedarAdminUserApiKey = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_ADMIN_USER_API_KEY);
    cedarAdminUserApiKey.add(SystemComponent.ADMIN_TOOL); // all neo4j, keycloak related tasks + search reindex
    cedarAdminUserApiKey.add(SystemComponent.KEYCLOAK_EVENT_LISTENER); // user login callback, auth with this
    cedarAdminUserApiKey.add(SystemComponent.SERVER_RESOURCE); // index regeneration
    cedarAdminUserApiKey.add(SystemComponent.SERVER_WORKER); // SearchPermissionExecutorService

    Set<SystemComponent> cedarAdminUserPasswd = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_ADMIN_USER_PASSWORD);
    cedarAdminUserPasswd.add(SystemComponent.ADMIN_TOOL); // all keycloak related tasks

    Set<SystemComponent> resourceServerUserCallbackUrl = variableToComponent.get(
        CedarEnvironmentVariable.CEDAR_RESOURCE_SERVER_USER_CALLBACK_URL);
    resourceServerUserCallbackUrl.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);

    Set<SystemComponent> resourceServerAdminCallbackUrl = variableToComponent.get(
        CedarEnvironmentVariable.CEDAR_RESOURCE_SERVER_ADMIN_CALLBACK_URL);
    resourceServerAdminCallbackUrl.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);

    Set<SystemComponent> cedarKeycloakClientId = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_KEYCLOAK_CLIENT_ID);
    cedarKeycloakClientId.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);
    // all the microservices for Keycloak initialization
    cedarKeycloakClientId.addAll(allMicroservices);

    Set<SystemComponent> cedarMongoUserName = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_MONGO_APP_USER_NAME);
    cedarMongoUserName.add(SystemComponent.ADMIN_TOOL);
    // all the microservices for MongoDB initialization for user details
    cedarMongoUserName.addAll(allMicroservices);

    Set<SystemComponent> cedarMongoUserPasswd = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_MONGO_APP_USER_PASSWORD);
    cedarMongoUserPasswd.addAll(cedarMongoUserName);// used together with the username

    Set<SystemComponent> cedarMongoHost = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_MONGO_HOST);
    cedarMongoHost.addAll(cedarMongoUserName);// used together with the username

    Set<SystemComponent> cedarPortMongo = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_MONGO_PORT);
    cedarPortMongo.addAll(cedarMongoUserName);// used together with the username

    Set<SystemComponent> cedarElasticsearchHost = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_ELASTICSEARCH_HOST);
    cedarElasticsearchHost.add(SystemComponent.SERVER_WORKER);
    cedarElasticsearchHost.add(SystemComponent.SERVER_VALUERECOMMENDER);
    cedarElasticsearchHost.add(SystemComponent.SERVER_RESOURCE);

    Set<SystemComponent> cedarElasticsearchTransportPort = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_ELASTICSEARCH_TRANSPORT_PORT);
    cedarElasticsearchTransportPort.addAll(cedarElasticsearchHost);

    Set<SystemComponent> cedarSaltApiKey = variableToComponent.get(CedarEnvironmentVariable.CEDAR_SALT_API_KEY);
    cedarSaltApiKey.add(SystemComponent.ADMIN_TOOL); //profile creation
    cedarSaltApiKey.add(SystemComponent.SERVER_RESOURCE); //profile creation triggered by event listener

    Set<SystemComponent> cedarLdUserBase = variableToComponent.get(CedarEnvironmentVariable.CEDAR_LD_USER_BASE);
    cedarLdUserBase.add(SystemComponent.ADMIN_TOOL);
    // all the microservices for Keycloak user details reading from token
    cedarLdUserBase.addAll(allMicroservices);

    Set<SystemComponent> redisPersistentHost = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_REDIS_PERSISTENT_HOST);
    redisPersistentHost.add(SystemComponent.SERVER_RESOURCE);
    redisPersistentHost.add(SystemComponent.SERVER_GROUP);
    redisPersistentHost.add(SystemComponent.SERVER_WORKER);

    Set<SystemComponent> redisPersistentPort = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_REDIS_PERSISTENT_PORT);
    redisPersistentPort.addAll(redisPersistentHost);

    Set<SystemComponent> cedarEverybodyGroup = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_EVERYBODY_GROUP_NAME);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_TEST);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_PRODUCTION);
    cedarEverybodyGroup.add(SystemComponent.ADMIN_TOOL);
    cedarEverybodyGroup.add(SystemComponent.SERVER_FOLDER);
    cedarEverybodyGroup.add(SystemComponent.SERVER_GROUP);
    cedarEverybodyGroup.add(SystemComponent.SERVER_RESOURCE);
    cedarEverybodyGroup.add(SystemComponent.SERVER_WORKER);

    Set<SystemComponent> cedarPortFolder = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_FOLDER);
    cedarPortFolder.add(SystemComponent.SERVER_FOLDER);

    Set<SystemComponent> cedarPortGroup = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_GROUP);
    cedarPortGroup.add(SystemComponent.SERVER_GROUP);

    Set<SystemComponent> cedarPortUser = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_USER);
    cedarPortUser.add(SystemComponent.SERVER_USER);

    Set<SystemComponent> cedarPortRepo = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_REPO);
    cedarPortRepo.add(SystemComponent.SERVER_REPO);

    Set<SystemComponent> cedarPortResource = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_RESOURCE);
    cedarPortResource.add(SystemComponent.SERVER_RESOURCE);

    Set<SystemComponent> cedarPortSchema = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_SCHEMA);
    cedarPortSchema.add(SystemComponent.SERVER_SCHEMA);

    Set<SystemComponent> cedarPortTemplate = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_TEMPLATE);
    cedarPortTemplate.add(SystemComponent.SERVER_TEMPLATE);

    Set<SystemComponent> cedarPortTerminology = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_PORT_TERMINOLOGY);
    cedarPortTerminology.add(SystemComponent.SERVER_TERMINOLOGY);

    Set<SystemComponent> cedarPortValuerecommender = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_PORT_VALUERECOMMENDER);
    cedarPortValuerecommender.add(SystemComponent.SERVER_VALUERECOMMENDER);

    Set<SystemComponent> cedarPortSubmission = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_SUBMISSION);
    cedarPortSubmission.add(SystemComponent.SERVER_SUBMISSION);

    Set<SystemComponent> cedarPortWorker = variableToComponent.get(CedarEnvironmentVariable.CEDAR_PORT_WORKER);
    cedarPortWorker.add(SystemComponent.SERVER_WORKER);

    Set<SystemComponent> cedarTestUser1Id = variableToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER1_ID);
    cedarTestUser1Id.add(SystemComponent.SERVER_TEMPLATE);
    cedarTestUser1Id.add(SystemComponent.SERVER_TERMINOLOGY);

    Set<SystemComponent> cedarTestUser2Id = variableToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER2_ID);

    // Compute the reverse map
    componentToVariable = new LinkedHashMap<>();
    for (SystemComponent component : SystemComponent.values()) {
      Set<CedarEnvironmentVariable> variables = new LinkedHashSet<>();
      componentToVariable.put(component, variables);
      for (CedarEnvironmentVariable variable : CedarEnvironmentVariable.values()) {
        Set<SystemComponent> componentsForVariable = variableToComponent.get(variable);
        if (componentsForVariable.contains(component)) {
          variables.add(variable);
        }
      }
    }
  }

  public static Set<CedarEnvironmentVariable> getVariableNamesFor(SystemComponent useCase) {
    return componentToVariable.get(useCase);
  }
}
