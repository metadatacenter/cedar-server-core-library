package org.metadatacenter.config.environment;

import org.metadatacenter.model.SystemComponent;

import java.util.*;

public class CedarConfigEnvironmentDescriptor {

  private final static Map<CedarEnvironmentVariable, List<SystemComponent>> variableToComponent;
  private final static Map<SystemComponent, List<CedarEnvironmentVariable>> componentToVariable;

  static {
    List<SystemComponent> allMicroservices = new ArrayList<>();
    for (SystemComponent sc : SystemComponent.values()) {
      if (sc.getServerName() != null) {
        allMicroservices.add(sc);
      }
    }

    variableToComponent = new LinkedHashMap<>();
    for (CedarEnvironmentVariable env : CedarEnvironmentVariable.values()) {
      List<SystemComponent> systemComponents = new ArrayList<>();
      systemComponents.add(SystemComponent.ALL);
      variableToComponent.put(env, systemComponents);
    }

    List<SystemComponent> cedarVersion = variableToComponent.get(CedarEnvironmentVariable.CEDAR_VERSION);
    // gulpfile.js replaces in version.js
    cedarVersion.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarVersion.add(SystemComponent.FRONTEND_TEST);
    cedarVersion.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarVersionModifier = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_VERSION_MODIFIER);
    // gulpfile.js replaces in version.js
    cedarVersionModifier.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarVersionModifier.add(SystemComponent.FRONTEND_TEST);
    cedarVersionModifier.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarHome = variableToComponent.get(CedarEnvironmentVariable.CEDAR_HOME);
    cedarHome.add(SystemComponent.ADMIN_TOOL); // generate-nginx-config, export dir
    cedarHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    List<SystemComponent> keycloakHome = variableToComponent.get(CedarEnvironmentVariable.KEYCLOAK_HOME);
    keycloakHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    List<SystemComponent> nginxHome = variableToComponent.get(CedarEnvironmentVariable.NGINX_HOME);
    nginxHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    List<SystemComponent> cedarFrontendBeh = variableToComponent.get(CedarEnvironmentVariable.CEDAR_FRONTEND_BEHAVIOR);
    // gulpfile.js decides that it should or not start a server for the front end code
    cedarFrontendBeh.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarFrontendBeh.add(SystemComponent.FRONTEND_TEST);
    cedarFrontendBeh.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarFrontendTarget = variableToComponent.get(CedarEnvironmentVariable.CEDAR_FRONTEND_TARGET);
    // gulpfile.js replaces this in several files, frontend connects to this server
    cedarFrontendTarget.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarFrontendTarget.add(SystemComponent.FRONTEND_TEST);
    cedarFrontendTarget.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarHost = variableToComponent.get(CedarEnvironmentVariable.CEDAR_HOST);
    cedarHost.add(SystemComponent.ADMIN_TOOL); // generate-nginx-config, all Keycloak related tasks
    // gulpfile.js replaces values in url-service.conf.json and test-env.js
    cedarHost.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarHost.add(SystemComponent.FRONTEND_TEST);
    cedarHost.add(SystemComponent.FRONTEND_PRODUCTION);
    // all the microservices
    cedarHost.addAll(allMicroservices);

    List<SystemComponent> cedarBioportalApiKey = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_BIOPORTAL_API_KEY);
    cedarBioportalApiKey.add(SystemComponent.SERVER_TERMINOLOGY);

    List<SystemComponent> cedarAnalyticsKey = variableToComponent.get(CedarEnvironmentVariable.CEDAR_ANALYTICS_KEY);
    // gulpfile.js replaces int tracking-service.conf.json
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_TEST);
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarNcbiSraFtpPwd = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_NCBI_SRA_FTP_PASSWORD);
    cedarNcbiSraFtpPwd.add(SystemComponent.SERVER_SUBMISSION);

    List<SystemComponent> cedarNeo4jTrUrl = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_NEO4J_TRANSACTION_URL);
    cedarNeo4jTrUrl.add(SystemComponent.ADMIN_TOOL); // reset tasks
    cedarNeo4jTrUrl.add(SystemComponent.SERVER_FOLDER); // storage for file system
    cedarNeo4jTrUrl.add(SystemComponent.SERVER_GROUP); // storage for groups
    cedarNeo4jTrUrl.add(SystemComponent.SERVER_RESOURCE); // permission tests
    cedarNeo4jTrUrl.add(SystemComponent.SERVER_WORKER); // permission changes

    List<SystemComponent> cedarNeo4jAuthString = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_NEO4J_AUTH_STRING);
    cedarNeo4jAuthString.addAll(cedarNeo4jTrUrl);

    List<SystemComponent> cedarAdminUserApiKey = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_ADMIN_USER_API_KEY);
    cedarAdminUserApiKey.add(SystemComponent.ADMIN_TOOL); // all neo4j, keycloak related tasks + search reindex
    cedarAdminUserApiKey.add(SystemComponent.SERVER_RESOURCE); // index regeneration
    cedarAdminUserApiKey.add(SystemComponent.SERVER_WORKER); // SearchPermissionExecutorService

    List<SystemComponent> cedarAdminUserPasswd = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_ADMIN_USER_PASSWORD);
    cedarAdminUserPasswd.add(SystemComponent.ADMIN_TOOL); // all keycloak related tasks

    List<SystemComponent> resourceServerUserCallbackUrl = variableToComponent.get(
        CedarEnvironmentVariable.CEDAR_RESOURCE_SERVER_USER_CALLBACK_URL);
    resourceServerUserCallbackUrl.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);

    List<SystemComponent> resourceServerAdminCallbackUrl = variableToComponent.get(
        CedarEnvironmentVariable.CEDAR_RESOURCE_SERVER_ADMIN_CALLBACK_URL);
    resourceServerAdminCallbackUrl.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);

    List<SystemComponent> cedarKeycloakClientId = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_KEYCLOAK_CLIENT_ID);
    cedarKeycloakClientId.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);
    // all the microservices for Keycloak initialization
    cedarKeycloakClientId.addAll(allMicroservices);

    List<SystemComponent> cedarMongoUserName = variableToComponent.get(CedarEnvironmentVariable.CEDAR_MONGO_USER_NAME);
    cedarMongoUserName.add(SystemComponent.ADMIN_TOOL);
    // all the microservices for MongoDB initialization for user details
    cedarMongoUserName.addAll(allMicroservices);

    List<SystemComponent> cedarMongoUserPasswd = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_MONGO_USER_PASSWORD);
    cedarMongoUserPasswd.addAll(cedarMongoUserName);// used together with the username

    List<SystemComponent> cedarSaltApiKey = variableToComponent.get(CedarEnvironmentVariable.CEDAR_SALT_API_KEY);
    cedarSaltApiKey.add(SystemComponent.ADMIN_TOOL); //profile creation
    cedarSaltApiKey.add(SystemComponent.SERVER_RESOURCE); //profile creation triggered by event listener

    List<SystemComponent> cedarLdUserBase = variableToComponent.get(CedarEnvironmentVariable.CEDAR_LD_USER_BASE);
    cedarLdUserBase.add(SystemComponent.ADMIN_TOOL);
    // all the microservices for Keycloak user details reading from token
    cedarLdUserBase.addAll(allMicroservices);

    List<SystemComponent> cedarEverybodyGroup = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_EVERYBODY_GROUP_NAME);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_TEST);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_PRODUCTION);
    cedarEverybodyGroup.add(SystemComponent.ADMIN_TOOL);
    cedarEverybodyGroup.add(SystemComponent.SERVER_FOLDER);
    cedarEverybodyGroup.add(SystemComponent.SERVER_GROUP);
    cedarEverybodyGroup.add(SystemComponent.SERVER_RESOURCE);
    cedarEverybodyGroup.add(SystemComponent.SERVER_WORKER);

    List<SystemComponent> cedarTestUser1Id = variableToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER1_ID);
    cedarTestUser1Id.add(SystemComponent.SERVER_TERMINOLOGY);

    List<SystemComponent> cedarTestUser2Id = variableToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER2_ID);

    // Compute the reverse map
    componentToVariable = new LinkedHashMap<>();
    for (SystemComponent component : SystemComponent.values()) {
      List<CedarEnvironmentVariable> variables = new ArrayList<>();
      componentToVariable.put(component, variables);
      for (CedarEnvironmentVariable variable : CedarEnvironmentVariable.values()) {
        List<SystemComponent> componentsForVariable = variableToComponent.get(variable);
        if (componentsForVariable.contains(component)) {
          variables.add(variable);
        }
      }
    }
  }

  public static List<CedarEnvironmentVariable> getVariableNamesFor(SystemComponent useCase) {
    return componentToVariable.get(useCase);
  }
}
