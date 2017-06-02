package org.metadatacenter.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.metadatacenter.config.environment.CedarEnvironmentVariable;
import org.metadatacenter.config.environment.CedarEnvironmentVariableProvider;
import org.metadatacenter.model.SystemComponent;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.util.test.TestUtil;

import java.util.HashMap;
import java.util.Map;

public class CedarConfigTest {

  public final static String CEDAR_ADMIN_USER_PASSWORD = "Password123";
  public final static String CEDAR_ADMIN_USER_API_KEY = "abcd-efgh";

  @Before
  public void setEnvironment() {
    Map<String, String> env = new HashMap<>();
    env.put(CedarEnvironmentVariable.CEDAR_VERSION.getName(), "1.0.0");
    env.put(CedarEnvironmentVariable.CEDAR_VERSION_MODIFIER.getName(), "");

    env.put(CedarEnvironmentVariable.CEDAR_HOME.getName(), "/home/cedar");
    env.put(CedarEnvironmentVariable.KEYCLOAK_HOME.getName(), "/home/cedar/keycloak");
    env.put(CedarEnvironmentVariable.NGINX_HOME.getName(), "/etc/nginx");

    env.put(CedarEnvironmentVariable.CEDAR_FRONTEND_BEHAVIOR.getName(), "server");
    env.put(CedarEnvironmentVariable.CEDAR_FRONTEND_TARGET.getName(), "local");
    env.put(CedarEnvironmentVariable.CEDAR_HOST.getName(), "metadatacenter.orgx");

    env.put(CedarEnvironmentVariable.CEDAR_BIOPORTAL_API_KEY.getName(), "apiKey-abcd");
    env.put(CedarEnvironmentVariable.CEDAR_ANALYTICS_KEY.getName(), "false");
    env.put(CedarEnvironmentVariable.CEDAR_NCBI_SRA_FTP_PASSWORD.getName(), "ftpPassword");

    env.put(CedarEnvironmentVariable.CEDAR_ADMIN_USER_PASSWORD.getName(), CEDAR_ADMIN_USER_PASSWORD);
    env.put(CedarEnvironmentVariable.CEDAR_ADMIN_USER_API_KEY.getName(), CEDAR_ADMIN_USER_API_KEY);

    env.put(CedarEnvironmentVariable.CEDAR_NEO4J_USER_NAME.getName(), "userName");
    env.put(CedarEnvironmentVariable.CEDAR_NEO4J_USER_PASSWORD.getName(), "userPassword");

    env.put(CedarEnvironmentVariable.CEDAR_RESOURCE_SERVER_USER_CALLBACK_URL.getName(), "http://");
    env.put(CedarEnvironmentVariable.CEDAR_RESOURCE_SERVER_ADMIN_CALLBACK_URL.getName(), "http://");
    env.put(CedarEnvironmentVariable.CEDAR_KEYCLOAK_CLIENT_ID.getName(), "cedar-angular-app");

    env.put(CedarEnvironmentVariable.CEDAR_MONGO_APP_USER_NAME.getName(), "cedarUser");
    env.put(CedarEnvironmentVariable.CEDAR_MONGO_APP_USER_PASSWORD.getName(), "cedarPassword");
    env.put(CedarEnvironmentVariable.CEDAR_MONGO_HOST.getName(), "localhost");
    env.put(CedarEnvironmentVariable.CEDAR_MONGO_PORT.getName(), "27017");

    env.put(CedarEnvironmentVariable.CEDAR_SALT_API_KEY.getName(), "salt");

    env.put(CedarEnvironmentVariable.CEDAR_LD_USER_BASE.getName(), "https://metadatacenter.org/users/");

    env.put(CedarEnvironmentVariable.CEDAR_EVERYBODY_GROUP_NAME.getName(), "Everybody");

    env.put(CedarEnvironmentVariable.CEDAR_BIOPORTAL_REST_BASE.getName(), "http://data.bioontology.org/");

    env.put(CedarEnvironmentVariable.CEDAR_NEO4J_HOST.getName(), "127.0.0.1");
    env.put(CedarEnvironmentVariable.CEDAR_NEO4J_REST_PORT.getName(), "7474");

    env.put(CedarEnvironmentVariable.CEDAR_ELASTICSEARCH_HOST.getName(), "127.0.0.1");
    env.put(CedarEnvironmentVariable.CEDAR_ELASTICSEARCH_TRANSPORT_PORT.getName(), "9200");

    env.put(CedarEnvironmentVariable.CEDAR_REDIS_PERSISTENT_HOST.getName(), "127.0.0.1");
    env.put(CedarEnvironmentVariable.CEDAR_REDIS_PERSISTENT_PORT.getName(), "6379");
    env.put(CedarEnvironmentVariable.CEDAR_REDIS_NONPERSISTENT_HOST.getName(), "127.0.0.1");
    env.put(CedarEnvironmentVariable.CEDAR_REDIS_NONPERSISTENT_PORT.getName(), "6380");

    env.put(CedarEnvironmentVariable.CEDAR_PORT_FOLDER.getName(), "9008");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_GROUP.getName(), "9009");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_REPO.getName(), "9002");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_RESOURCE.getName(), "9007");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_SCHEMA.getName(), "9003");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_SUBMISSION.getName(), "9010");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_TEMPLATE.getName(), "9001");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_TERMINOLOGY.getName(), "9004");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_USER.getName(), "9005");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_VALUERECOMMENDER.getName(), "9006");
    env.put(CedarEnvironmentVariable.CEDAR_PORT_WORKER.getName(), "9011");

    env.put(CedarEnvironmentVariable.CEDAR_TEST_USER1_ID.getName(), "https://metadatacenter.org/users/user1-uuid");
    env.put(CedarEnvironmentVariable.CEDAR_TEST_USER2_ID.getName(), "https://metadatacenter.org/users/user2-uuid");

    TestUtil.setEnv(env);
  }

  private CedarConfig getCedarConfig() {
    return CedarConfig.getInstance(CedarEnvironmentVariableProvider.getFor(SystemComponent.ALL));
  }

  @Test
  public void testGetInstance() throws Exception {
    CedarConfig instance = getCedarConfig();
    Assert.assertNotNull(instance);
  }

  @Test
  public void testKeycloakConfig() throws Exception {
    CedarConfig instance = getCedarConfig();
    KeycloakConfig keycloakConfig = instance.getKeycloakConfig();
    Assert.assertNotNull(keycloakConfig);
    Assert.assertEquals("admin-cli", keycloakConfig.getClientId());

    AdminUserConfig adminUser = instance.getAdminUserConfig();
    Assert.assertNotNull(adminUser);
    Assert.assertEquals("cedar-admin", adminUser.getUserName());
    Assert.assertEquals(CEDAR_ADMIN_USER_PASSWORD, adminUser.getPassword());
    Assert.assertEquals(CEDAR_ADMIN_USER_API_KEY, adminUser.getApiKey());
  }

  @Test
  public void testMongoConfig() throws Exception {
    CedarConfig instance = getCedarConfig();
    MongoConfig templateServerConfig = instance.getTemplateServerConfig();
    Assert.assertNotNull(templateServerConfig);
    Assert.assertEquals("cedar", templateServerConfig.getDatabaseName());

    Map<String, String> templateServerCollections = templateServerConfig.getCollections();
    Assert.assertNotNull(templateServerCollections);
    //Assert.assertEquals("template-fields", collections.get("field"));
    Assert.assertEquals("template-elements", templateServerCollections.get("element"));
    Assert.assertEquals("templates", templateServerCollections.get("template"));
    Assert.assertEquals("template-instances", templateServerCollections.get("instance"));

    MongoConfig userServerConfig = instance.getUserServerConfig();
    Assert.assertNotNull(userServerConfig);
    Assert.assertEquals("cedar", userServerConfig.getDatabaseName());

    Map<String, String> userServerCollections = userServerConfig.getCollections();
    Assert.assertNotNull(userServerCollections);

    Assert.assertEquals("users", userServerCollections.get("user"));
  }

  @Test
  public void testElasticSearchConfig() throws Exception {
    CedarConfig instance = getCedarConfig();
    ElasticsearchSettingsMappingsConfig elasticsearchSettingsMappingsConfig = instance
        .getElasticsearchSettingsMappingsConfig();
    Assert.assertNotNull(elasticsearchSettingsMappingsConfig);
    Assert.assertNotNull(elasticsearchSettingsMappingsConfig.getSettings());
    Assert.assertNotNull(elasticsearchSettingsMappingsConfig.getMappings());
    Assert.assertNotNull(elasticsearchSettingsMappingsConfig.getMappings().getNode());
    Assert.assertNotNull(elasticsearchSettingsMappingsConfig.getMappings().getContent());
    Assert.assertNotNull(elasticsearchSettingsMappingsConfig.getMappings().getUsers());
    Assert.assertNotNull(elasticsearchSettingsMappingsConfig.getMappings().getGroups());

    ElasticsearchConfig elasticsearchConfig = instance.getElasticsearchConfig();
    Assert.assertNotNull(elasticsearchConfig.getTypes());
    Assert.assertNotNull(elasticsearchConfig.getTypes().get(IndexedDocumentType.NODE));
    Assert.assertNotNull(elasticsearchConfig.getTypes().get(IndexedDocumentType.CONTENT));
    Assert.assertNotNull(elasticsearchConfig.getTypes().get(IndexedDocumentType.USERS));
    Assert.assertNotNull(elasticsearchConfig.getTypes().get(IndexedDocumentType.GROUPS));

    Map<String, Object> settings = elasticsearchSettingsMappingsConfig.getSettings();
    Map<String, Object> index = (Map<String, Object>) settings.get("index");
    Map<String, Object> analysis = (Map<String, Object>) index.get("analysis");
    Map<String, Object> tokenizer = (Map<String, Object>) analysis.get("tokenizer");
    Map<String, Object> ngram_tokenizer = (Map<String, Object>) tokenizer.get("ngram_tokenizer");

    Assert.assertEquals("ngram", ngram_tokenizer.get("type"));
  }

}