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

    Set<SystemComponent> cedarNetGateway = variableToComponent.get(CedarEnvironmentVariable.CEDAR_NET_GATEWAY);
    cedarNetGateway.add(SystemComponent.ADMIN_TOOL);
    // all the microservices
    cedarNetGateway.addAll(allMicroservices);

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

    Set<SystemComponent> cedarNeo4jUserPassword = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_NEO4J_USER_PASSWORD);
    cedarNeo4jUserPassword.add(SystemComponent.ADMIN_TOOL); // reset tasks
    cedarNeo4jUserPassword.add(SystemComponent.SERVER_WORKSPACE); // storage for file system
    cedarNeo4jUserPassword.add(SystemComponent.SERVER_GROUP); // storage for groups
    cedarNeo4jUserPassword.add(SystemComponent.SERVER_RESOURCE); // permission tests
    cedarNeo4jUserPassword.add(SystemComponent.SERVER_WORKER); // permission changes

    Set<SystemComponent> cedarNeo4jHost = variableToComponent.get(CedarEnvironmentVariable.CEDAR_NEO4J_HOST);
    cedarNeo4jHost.addAll(cedarNeo4jUserPassword);

    Set<SystemComponent> cedarNeo4jRestPort = variableToComponent.get(CedarEnvironmentVariable.CEDAR_NEO4J_REST_PORT);
    cedarNeo4jRestPort.addAll(cedarNeo4jUserPassword);

    Set<SystemComponent> cedarAdminUserApiKey = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_ADMIN_USER_API_KEY);
    cedarAdminUserApiKey.add(SystemComponent.ADMIN_TOOL); // all neo4j, keycloak related tasks + search reindex
    cedarAdminUserApiKey.add(SystemComponent.KEYCLOAK_EVENT_LISTENER); // user login callback, auth with this
    cedarAdminUserApiKey.add(SystemComponent.SERVER_RESOURCE); // index regeneration
    cedarAdminUserApiKey.add(SystemComponent.SERVER_WORKER); // SearchPermissionExecutorService

    Set<SystemComponent> cedarAdminUserPasswd = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_ADMIN_USER_PASSWORD);
    cedarAdminUserPasswd.add(SystemComponent.ADMIN_TOOL); // all keycloak related tasks

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

    Set<SystemComponent> redisPersistentHost = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_REDIS_PERSISTENT_HOST);
    redisPersistentHost.add(SystemComponent.SERVER_RESOURCE);
    redisPersistentHost.add(SystemComponent.SERVER_GROUP);
    redisPersistentHost.add(SystemComponent.SERVER_WORKER);

    Set<SystemComponent> redisPersistentPort = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_REDIS_PERSISTENT_PORT);
    redisPersistentPort.addAll(redisPersistentHost);

    Set<SystemComponent> cedarHttpPortWorkspace = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_WORKSPACE_HTTP_PORT);
    cedarHttpPortWorkspace.add(SystemComponent.SERVER_WORKSPACE);
    cedarHttpPortWorkspace.add(SystemComponent.SERVER_RESOURCE);
    cedarHttpPortWorkspace.add(SystemComponent.SERVER_REPO);
    cedarHttpPortWorkspace.add(SystemComponent.SERVER_WORKER);
    Set<SystemComponent> cedarAdminPortWorkspace = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_WORKSPACE_ADMIN_PORT);
    cedarAdminPortWorkspace.add(SystemComponent.SERVER_WORKSPACE);
    Set<SystemComponent> cedarStopPortWorkspace = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_WORKSPACE_STOP_PORT);
    cedarStopPortWorkspace.add(SystemComponent.SERVER_WORKSPACE);

    Set<SystemComponent> cedarHttpPortGroup = variableToComponent.get(CedarEnvironmentVariable.CEDAR_GROUP_HTTP_PORT);
    cedarHttpPortGroup.add(SystemComponent.SERVER_GROUP);
    Set<SystemComponent> cedarAdminPortGroup = variableToComponent.get(CedarEnvironmentVariable.CEDAR_GROUP_ADMIN_PORT);
    cedarAdminPortGroup.add(SystemComponent.SERVER_GROUP);
    Set<SystemComponent> cedarStopPortGroup = variableToComponent.get(CedarEnvironmentVariable.CEDAR_GROUP_STOP_PORT);
    cedarStopPortGroup.add(SystemComponent.SERVER_GROUP);

    Set<SystemComponent> cedarHttpPortMessaging = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_MESSAGING_HTTP_PORT);
    cedarHttpPortMessaging.add(SystemComponent.SERVER_MESSAGING);
    Set<SystemComponent> cedarAdminPortMessaging = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_MESSAGING_ADMIN_PORT);
    cedarAdminPortMessaging.add(SystemComponent.SERVER_MESSAGING);
    Set<SystemComponent> cedarStopPortMessaging = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_MESSAGING_STOP_PORT);
    cedarStopPortMessaging.add(SystemComponent.SERVER_MESSAGING);

    Set<SystemComponent> cedarHttpPortUser = variableToComponent.get(CedarEnvironmentVariable.CEDAR_USER_HTTP_PORT);
    cedarHttpPortUser.addAll(allMicroservices);
    Set<SystemComponent> cedarAdminPortUser = variableToComponent.get(CedarEnvironmentVariable.CEDAR_USER_ADMIN_PORT);
    cedarAdminPortUser.add(SystemComponent.SERVER_USER);
    Set<SystemComponent> cedarStopPortUser = variableToComponent.get(CedarEnvironmentVariable.CEDAR_USER_STOP_PORT);
    cedarStopPortUser.add(SystemComponent.SERVER_USER);

    Set<SystemComponent> cedarHttpPortRepo = variableToComponent.get(CedarEnvironmentVariable.CEDAR_REPO_HTTP_PORT);
    cedarHttpPortRepo.add(SystemComponent.SERVER_REPO);
    Set<SystemComponent> cedarAdminPortRepo = variableToComponent.get(CedarEnvironmentVariable.CEDAR_REPO_ADMIN_PORT);
    cedarAdminPortRepo.add(SystemComponent.SERVER_REPO);
    Set<SystemComponent> cedarStopPortRepo = variableToComponent.get(CedarEnvironmentVariable.CEDAR_REPO_STOP_PORT);
    cedarStopPortRepo.add(SystemComponent.SERVER_REPO);

    Set<SystemComponent> cedarHttpPortResource = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_RESOURCE_HTTP_PORT);
    cedarHttpPortResource.add(SystemComponent.SERVER_RESOURCE);
    cedarHttpPortResource.add(SystemComponent.ADMIN_TOOL);
    Set<SystemComponent> cedarAdminPortResource = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_RESOURCE_ADMIN_PORT);
    cedarAdminPortResource.add(SystemComponent.SERVER_RESOURCE);
    Set<SystemComponent> cedarStopPortResource = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_RESOURCE_STOP_PORT);
    cedarStopPortResource.add(SystemComponent.SERVER_RESOURCE);

    Set<SystemComponent> cedarHttpPortSchema = variableToComponent.get(CedarEnvironmentVariable.CEDAR_SCHEMA_HTTP_PORT);
    cedarHttpPortSchema.add(SystemComponent.SERVER_SCHEMA);
    Set<SystemComponent> cedarAdminPortSchema = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_SCHEMA_ADMIN_PORT);
    cedarAdminPortSchema.add(SystemComponent.SERVER_SCHEMA);
    Set<SystemComponent> cedarStopPortSchema = variableToComponent.get(CedarEnvironmentVariable.CEDAR_SCHEMA_STOP_PORT);
    cedarStopPortSchema.add(SystemComponent.SERVER_SCHEMA);

    Set<SystemComponent> cedarPortTemplate = variableToComponent.get(CedarEnvironmentVariable.CEDAR_TEMPLATE_HTTP_PORT);
    cedarPortTemplate.add(SystemComponent.SERVER_TEMPLATE);
    cedarPortTemplate.add(SystemComponent.SERVER_RESOURCE);
    cedarPortTemplate.add(SystemComponent.SERVER_WORKER);
    Set<SystemComponent> cedarAdminPortTemplate = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_TEMPLATE_ADMIN_PORT);
    cedarAdminPortTemplate.add(SystemComponent.SERVER_TEMPLATE);
    Set<SystemComponent> cedarStopPortTemplate = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_TEMPLATE_STOP_PORT);
    cedarStopPortTemplate.add(SystemComponent.SERVER_TEMPLATE);

    Set<SystemComponent> cedarHttpPortTerminology = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_TERMINOLOGY_HTTP_PORT);
    cedarHttpPortTerminology.add(SystemComponent.SERVER_TERMINOLOGY);
    Set<SystemComponent> cedarAdminPortTerminology = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_TERMINOLOGY_ADMIN_PORT);
    cedarAdminPortTerminology.add(SystemComponent.SERVER_TERMINOLOGY);
    Set<SystemComponent> cedarStopPortTerminology = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_TERMINOLOGY_STOP_PORT);
    cedarStopPortTerminology.add(SystemComponent.SERVER_TERMINOLOGY);

    Set<SystemComponent> cedarHttpPortValuerecommender = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_VALUERECOMMENDER_HTTP_PORT);
    cedarHttpPortValuerecommender.add(SystemComponent.SERVER_VALUERECOMMENDER);
    Set<SystemComponent> cedarAdminPortValuerecommender = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_VALUERECOMMENDER_ADMIN_PORT);
    cedarAdminPortValuerecommender.add(SystemComponent.SERVER_VALUERECOMMENDER);
    Set<SystemComponent> cedarStopPortValuerecommender = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_VALUERECOMMENDER_STOP_PORT);
    cedarStopPortValuerecommender.add(SystemComponent.SERVER_VALUERECOMMENDER);

    Set<SystemComponent> cedarHttpPortSubmission = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_SUBMISSION_HTTP_PORT);
    cedarHttpPortSubmission.add(SystemComponent.SERVER_SUBMISSION);
    Set<SystemComponent> cedarAdminPortSubmission = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_SUBMISSION_ADMIN_PORT);
    cedarAdminPortSubmission.add(SystemComponent.SERVER_SUBMISSION);
    Set<SystemComponent> cedarStopPortSubmission = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_SUBMISSION_STOP_PORT);
    cedarStopPortSubmission.add(SystemComponent.SERVER_SUBMISSION);

    Set<SystemComponent> cedarHttpPortWorker = variableToComponent.get(CedarEnvironmentVariable.CEDAR_WORKER_HTTP_PORT);
    cedarHttpPortWorker.add(SystemComponent.SERVER_WORKER);
    Set<SystemComponent> cedarAdminPortWorker = variableToComponent.get(CedarEnvironmentVariable
        .CEDAR_WORKER_ADMIN_PORT);
    cedarAdminPortWorker.add(SystemComponent.SERVER_WORKER);
    Set<SystemComponent> cedarStopPortWorker = variableToComponent.get(CedarEnvironmentVariable.CEDAR_WORKER_STOP_PORT);
    cedarStopPortWorker.add(SystemComponent.SERVER_WORKER);

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
