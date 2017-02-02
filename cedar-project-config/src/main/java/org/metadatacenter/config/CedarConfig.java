package org.metadatacenter.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.*;
import io.dropwizard.jackson.Jackson;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;

public class CedarConfig extends Configuration {

  @JsonProperty("keycloak")
  private KeycloakConfig keycloakConfig;

  @JsonProperty("mongo")
  private MongoConfig mongoConfig;

  @JsonProperty("neo4j")
  private Neo4JConfig neo4jConfig;

  @JsonProperty("folderStructure")
  private FolderStructureConfig folderStructureConfig;

  @JsonProperty("folderRESTAPI")
  private FolderRESTAPI folderRESTAPI;

  @JsonProperty("linkedData")
  private LinkedDataConfig linkedDataConfig;

  @JsonProperty("blueprintUserProfile")
  private BlueprintUserProfile blueprintUserProfile;

  @JsonProperty("elasticsearch")
  private ElasticsearchConfig elasticsearchConfig;

  // This is read from a different config file
  private ElasticsearchSettingsMappingsConfig elasticsearchSettingsMappingsConfig;

  @JsonProperty("servers")
  private ServersConfig servers;

  @JsonProperty("searchSettings")
  private SearchSettings searchSettings;

  @JsonProperty("importExport")
  private ImportExportConfig importExportConfig;

  @JsonProperty("templateRESTAPI")
  private TemplateRESTAPI templateRESTAPI;

  @JsonProperty("templateRESTAPISummaries")
  private TemplateRESTAPISummaries templateRESTAPISummaries;

  @JsonProperty("test")
  private TestConfig testConfig;

  @JsonProperty("testUsers")
  private TestUsers testUsers;

  @JsonProperty("terminology")
  private TerminologyConfig terminologyConfig;

  @JsonProperty("cache")
  private CacheConfig cacheConfig;

  private final static CedarConfig instance;

  static {
    instance = buildInstance();
  }

  private static CedarConfig buildInstance() {

    CedarConfig config = null;

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    final SubstitutingSourceProvider substitutingSourceProvider = new SubstitutingSourceProvider(
        new ClasspathConfigurationSourceProvider(), new EnvironmentVariableSubstitutor());

    // Read main config
    final String mainConfigFileName = "cedar-main.yml";

    final ConfigurationFactory<CedarConfig> mainConfigurationFactory = new YamlConfigurationFactory<>(
        CedarConfig.class, validator, Jackson.newObjectMapper(), "cedar");

    try {
      config = mainConfigurationFactory.build(substitutingSourceProvider, mainConfigFileName);
    } catch (IOException | ConfigurationException e) {
      e.printStackTrace();
      System.exit(-1);
    }

    // Read search config
    final String elasticSearchSettingsMappingsConfigFileName = "cedar-search.json";

    ElasticsearchSettingsMappingsConfig elasticsearchSettingsMappings = null;

    final ConfigurationFactory<ElasticsearchSettingsMappingsConfig> searchConfigurationFactory = new YamlConfigurationFactory<>(
        ElasticsearchSettingsMappingsConfig.class, validator, Jackson.newObjectMapper(), "cedar");

    try {
      elasticsearchSettingsMappings = searchConfigurationFactory.build(substitutingSourceProvider, elasticSearchSettingsMappingsConfigFileName);
    } catch (IOException | ConfigurationException e) {
      e.printStackTrace();
      System.exit(-2);
    }

    config.elasticsearchSettingsMappingsConfig = elasticsearchSettingsMappings;

    return config;
  }

  public static CedarConfig getInstance() {
    return instance;
  }

  public KeycloakConfig getKeycloakConfig() {
    return keycloakConfig;
  }

  public MongoConfig getMongoConfig() {
    return mongoConfig;
  }

  public Neo4JConfig getNeo4jConfig() {
    return neo4jConfig;
  }

  public FolderStructureConfig getFolderStructureConfig() {
    return folderStructureConfig;
  }

  public FolderRESTAPI getFolderRESTAPI() {
    return folderRESTAPI;
  }

  public LinkedDataConfig getLinkedDataConfig() {
    return linkedDataConfig;
  }

  public BlueprintUserProfile getBlueprintUserProfile() {
    return blueprintUserProfile;
  }

  public ElasticsearchConfig getElasticsearchConfig() {
    return elasticsearchConfig;
  }

  public ElasticsearchSettingsMappingsConfig getElasticsearchSettingsMappingsConfig() {
    return elasticsearchSettingsMappingsConfig;
  }

  public ServersConfig getServers() {
    return servers;
  }

  public SearchSettings getSearchSettings() {
    return searchSettings;
  }

  public ImportExportConfig getImportExportConfig() {
    return importExportConfig;
  }

  public TemplateRESTAPI getTemplateRESTAPI() {
    return templateRESTAPI;
  }

  public TemplateRESTAPISummaries getTemplateRESTAPISummaries() {
    return templateRESTAPISummaries;
  }

  public TestConfig getTestConfig() {
    return testConfig;
  }

  public TestUsers getTestUsers() {
    return testUsers;
  }

  public TerminologyConfig getTerminologyConfig() {
    return terminologyConfig;
  }

  public CacheConfig getCacheConfig() {
    return cacheConfig;
  }

  // Utility methods

  public String getMongoCollectionName(CedarNodeType nt) {
    return getMongoConfig().getCollections().get(nt.getValue());
  }

  public String getLinkedDataPrefix(CedarNodeType nodeType) {
    return getLinkedDataConfig().getBase() + nodeType.getPrefix() + "/";
  }

  public LinkedDataUtil getLinkedDataUtil() {
    return new LinkedDataUtil(getLinkedDataConfig());
  }


}
