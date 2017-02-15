package org.metadatacenter.config;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.util.test.TestUtil;

import java.util.HashMap;
import java.util.Map;

public class CedarConfigTest {

  public final static String CEDAR_ADMIN_USER_PASSWORD = "Password123";
  public final static String CEDAR_ADMIN_USER_UUID = "abcd-efgh";
  public final static String CEDAR_ADMIN_USER_API_KEY = "xyzW-abcd";

  @Before
  public void setEnvironment() {
    Map<String, String> env = new HashMap<>();
    env.put("CEDAR_ADMIN_USER_API_KEY", CEDAR_ADMIN_USER_API_KEY);
    env.put("CEDAR_ADMIN_USER_UUID", CEDAR_ADMIN_USER_UUID);
    env.put("CEDAR_ADMIN_USER_PASSWORD", CEDAR_ADMIN_USER_PASSWORD);
    env.put("CEDAR_NEO4J_TRANSACTION_URL", "http://");
    env.put("CEDAR_NEO4J_AUTH_STRING", "neo-auth");
    env.put("CEDAR_HOST", "metadatacenter.orgx");
    env.put("CEDAR_BIOPORTAL_API_KEY", "apiKey-abcd");
    env.put("CEDAR_HOME", "/Users/CEDAR/");
    env.put("CEDAR_TEST_USER1_UUID", "user1-uuid1");
    env.put("CEDAR_TEST_USER2_UUID", "user1-uuid2");
    env.put("CEDAR_TEST_USER3_UUID", "user1-uuid3");
    TestUtil.setEnv(env);
  }

  @Test
  public void testGetInstance() throws Exception {
    CedarConfig instance = CedarConfig.getInstance();
    Assert.assertNotNull(instance);
  }

  @Test
  public void testKeycloakConfig() throws Exception {
    CedarConfig instance = CedarConfig.getInstance();
    KeycloakConfig keycloakConfig = instance.getKeycloakConfig();
    Assert.assertNotNull(keycloakConfig);
    Assert.assertEquals("admin-cli", keycloakConfig.getClientId());

    AdminUserConfig adminUser = instance.getAdminUserConfig();
    Assert.assertNotNull(adminUser);
    Assert.assertEquals("cedar-admin", adminUser.getUserName());
    Assert.assertEquals(CEDAR_ADMIN_USER_API_KEY, adminUser.getApiKey());
    Assert.assertEquals(CEDAR_ADMIN_USER_PASSWORD, adminUser.getPassword());
    Assert.assertEquals(CEDAR_ADMIN_USER_UUID, adminUser.getUuid());
  }

  @Test
  public void testMongoConfig() throws Exception {
    CedarConfig instance = CedarConfig.getInstance();
    MongoConfig mongoConfig = instance.getMongoConfig();
    Assert.assertNotNull(mongoConfig);
    Assert.assertEquals("cedar", mongoConfig.getDatabaseName());
    Assert.assertEquals("cedar-test", mongoConfig.getDatabaseNameTest());

    Map<String, String> collections = mongoConfig.getCollections();
    Assert.assertNotNull(collections);
    //Assert.assertEquals("template-fields", collections.get("field"));
    Assert.assertEquals("template-elements", collections.get("element"));
    Assert.assertEquals("templates", collections.get("template"));
    Assert.assertEquals("template-instances", collections.get("instance"));
    Assert.assertEquals("users", collections.get("user"));
  }

  @Test
  public void testElasticSearchConfig() throws Exception {
    CedarConfig instance = CedarConfig.getInstance();
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