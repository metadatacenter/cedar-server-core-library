package org.metadatacenter.config.environment;

import org.metadatacenter.model.SystemComponent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CedarConfigEnvironmentDescriptor {

  private final static Map<CedarEnvironmentVariable, List<SystemComponent>> varToComponent;

  static {
    List<SystemComponent> allMicroservices = new ArrayList<>();
    for (SystemComponent sc : SystemComponent.values()) {
      if (sc.getServerName() != null) {
        allMicroservices.add(sc);
      }
    }

    varToComponent = new HashMap<>();
    for (CedarEnvironmentVariable env : CedarEnvironmentVariable.values()) {
      varToComponent.put(env, new ArrayList<>());
    }

    List<SystemComponent> cedarVersion = varToComponent.get(CedarEnvironmentVariable.CEDAR_VERSION);
    // gulpfile.js replaces in version.js
    cedarVersion.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarVersion.add(SystemComponent.FRONTEND_TEST);
    cedarVersion.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarVersionModifier = varToComponent.get(CedarEnvironmentVariable.CEDAR_VERSION_MODIFIER);
    // gulpfile.js replaces in version.js
    cedarVersionModifier.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarVersionModifier.add(SystemComponent.FRONTEND_TEST);
    cedarVersionModifier.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarHome = varToComponent.get(CedarEnvironmentVariable.CEDAR_HOME);
    cedarHome.add(SystemComponent.ADMIN_TOOL); // generate-nginx-config, export dir
    cedarHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    List<SystemComponent> keycloakHome = varToComponent.get(CedarEnvironmentVariable.KEYCLOAK_HOME);
    keycloakHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    List<SystemComponent> nginxHome = varToComponent.get(CedarEnvironmentVariable.NGINX_HOME);
    nginxHome.add(SystemComponent.UTIL_BIN); // utility shell scrips

    List<SystemComponent> cedarProfile = varToComponent.get(CedarEnvironmentVariable.CEDAR_PROFILE);
    // gulpfile.js decides that it should or not start a server for the front end code
    cedarProfile.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarProfile.add(SystemComponent.FRONTEND_TEST);
    cedarProfile.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarHost = varToComponent.get(CedarEnvironmentVariable.CEDAR_HOST);
    cedarHost.add(SystemComponent.ADMIN_TOOL); // generate-nginx-config, all Keycloak related tasks
    // gulpfile.js replaces values in url-service.conf.json and test-env.js
    cedarHost.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarHost.add(SystemComponent.FRONTEND_TEST);
    cedarHost.add(SystemComponent.FRONTEND_PRODUCTION);
    // all the microservices
    cedarHost.addAll(allMicroservices);

    List<SystemComponent> cedarBioportalApiKey = varToComponent.get(CedarEnvironmentVariable.CEDAR_BIOPORTAL_API_KEY);
    cedarBioportalApiKey.add(SystemComponent.SERVER_TERMINOLOGY);

    List<SystemComponent> cedarAnalyticsKey = varToComponent.get(CedarEnvironmentVariable.CEDAR_ANALYTICS_KEY);
    // gulpfile.js replaces int tracking-service.conf.json
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_TEST);
    cedarAnalyticsKey.add(SystemComponent.FRONTEND_PRODUCTION);

    List<SystemComponent> cedarNcbiSraFtpPwd = varToComponent.get(CedarEnvironmentVariable.CEDAR_NCBI_SRA_FTP_PASSWORD);
    cedarNcbiSraFtpPwd.add(SystemComponent.SERVER_SUBMISSION);

    List<SystemComponent> cedarNeo4jTrUrl = varToComponent.get(CedarEnvironmentVariable.CEDAR_NEO4J_TRANSACTION_URL);
    cedarNeo4jTrUrl.add(SystemComponent.ADMIN_TOOL); // reset tasks
    cedarNeo4jTrUrl.add(SystemComponent.SERVER_FOLDER); // storage for file system
    cedarNeo4jTrUrl.add(SystemComponent.SERVER_GROUP); // storage for groups
    cedarNeo4jTrUrl.add(SystemComponent.SERVER_RESOURCE); // permission tests
    cedarNeo4jTrUrl.add(SystemComponent.SERVER_WORKER); // permission changes

    List<SystemComponent> cedarNeo4jAuthString = varToComponent.get(CedarEnvironmentVariable.CEDAR_NEO4J_AUTH_STRING);
    cedarNeo4jAuthString.addAll(cedarNeo4jTrUrl);

    List<SystemComponent> cedarAdminUserApiKey = varToComponent.get(CedarEnvironmentVariable.CEDAR_ADMIN_USER_API_KEY);
    cedarAdminUserApiKey.add(SystemComponent.ADMIN_TOOL); // all neo4j, keycloak related tasks + search reindex
    cedarAdminUserApiKey.add(SystemComponent.SERVER_RESOURCE); // index regeneration
    cedarAdminUserApiKey.add(SystemComponent.SERVER_WORKER); // SearchPermissionExecutorService

    List<SystemComponent> cedarAdminUserPasswd = varToComponent.get(CedarEnvironmentVariable.CEDAR_ADMIN_USER_PASSWORD);
    cedarAdminUserPasswd.add(SystemComponent.ADMIN_TOOL); // all keycloak related tasks

    List<SystemComponent> resourceServerUserCallbackUrl = varToComponent.get(
        CedarEnvironmentVariable.CEDAR_RESOURCE_SERVER_USER_CALLBACK_URL);
    resourceServerUserCallbackUrl.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);

    List<SystemComponent> resourceServerAdminCallbackUrl = varToComponent.get(
        CedarEnvironmentVariable.CEDAR_RESOURCE_SERVER_ADMIN_CALLBACK_URL);
    resourceServerAdminCallbackUrl.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);

    List<SystemComponent> cedarKeycloakClientId = varToComponent.get(CedarEnvironmentVariable.CEDAR_KEYCLOAK_CLIENT_ID);
    cedarKeycloakClientId.add(SystemComponent.KEYCLOAK_EVENT_LISTENER);
    // all the microservices for Keycloak initialization
    cedarKeycloakClientId.addAll(allMicroservices);

    List<SystemComponent> cedarMongoUserName = varToComponent.get(CedarEnvironmentVariable.CEDAR_MONGO_USER_NAME);
    cedarMongoUserName.add(SystemComponent.ADMIN_TOOL);
    cedarMongoUserName.add(SystemComponent.SERVER_REPO);
    cedarMongoUserName.add(SystemComponent.SERVER_TEMPLATE);
    cedarMongoUserName.add(SystemComponent.SERVER_RESOURCE);
    cedarMongoUserName.add(SystemComponent.SERVER_USER);

    List<SystemComponent> cedarMongoUserPasswd = varToComponent.get(CedarEnvironmentVariable.CEDAR_MONGO_USER_PASSWORD);
    cedarMongoUserPasswd.addAll(cedarMongoUserName);// used together with the username

    List<SystemComponent> cedarSaltApiKey = varToComponent.get(CedarEnvironmentVariable.CEDAR_SALT_API_KEY);
    cedarSaltApiKey.add(SystemComponent.ADMIN_TOOL); //profile creation
    cedarSaltApiKey.add(SystemComponent.SERVER_RESOURCE); //profile creation triggered by event listener

    List<SystemComponent> cedarLdUserBase = varToComponent.get(CedarEnvironmentVariable.CEDAR_LD_USER_BASE);
    cedarLdUserBase.add(SystemComponent.ADMIN_TOOL);
    // all the microservices for Keycloak user details reading from token
    cedarLdUserBase.addAll(allMicroservices);

    List<SystemComponent> cedarEverybodyGroup = varToComponent.get(CedarEnvironmentVariable.CEDAR_EVERYBODY_GROUP_NAME);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_DEVELOPMENT);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_TEST);
    cedarEverybodyGroup.add(SystemComponent.FRONTEND_PRODUCTION);
    cedarEverybodyGroup.add(SystemComponent.ADMIN_TOOL);
    cedarEverybodyGroup.add(SystemComponent.SERVER_FOLDER);
    cedarEverybodyGroup.add(SystemComponent.SERVER_GROUP);
    cedarEverybodyGroup.add(SystemComponent.SERVER_RESOURCE);
    cedarEverybodyGroup.add(SystemComponent.SERVER_WORKER);

    List<SystemComponent> cedarTestUser1Email = varToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER1_EMAIL);
    List<SystemComponent> cedarTestUser1Passwd = varToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER1_PASSWORD);
    List<SystemComponent> cedarTestUser1Id = varToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER1_ID);
    List<SystemComponent> cedarTestUser1Name = varToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER1_NAME);

    List<SystemComponent> cedarTestUser2Email = varToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER2_EMAIL);
    List<SystemComponent> cedarTestUser2Passwd = varToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER2_PASSWORD);
    List<SystemComponent> cedarTestUser2Id = varToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER2_ID);
    List<SystemComponent> cedarTestUser2Name = varToComponent.get(CedarEnvironmentVariable.CEDAR_TEST_USER2_NAME);

  }
}
