package org.metadatacenter.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.*;
import io.dropwizard.jackson.Jackson;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Validation;
import javax.validation.Validator;
import java.io.IOException;
import java.util.Map;

public class CedarConfig extends Configuration {

  @JsonProperty("home")
  private String home;

  @JsonProperty("host")
  private String host;

  @JsonProperty("adminUser")
  private AdminUserConfig adminUserConfig;

  @JsonProperty("keycloak")
  private KeycloakConfig keycloakConfig;

  @JsonProperty("templateServer")
  private MongoConfig templateServerConfig;

  @JsonProperty("userServer")
  private MongoConfig userServerConfig;

  @JsonProperty("neo4j")
  private Neo4JConfig neo4jConfig;

  @JsonProperty("folderStructure")
  private FolderStructureConfig folderStructureConfig;

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

  @JsonProperty("folderRESTAPI")
  private FolderRESTAPI folderRESTAPI;

  @JsonProperty("templateRESTAPI")
  private TemplateRESTAPI templateRESTAPI;

  @JsonProperty("submission")
  private SubmissionConfig submissionConfig;

  @JsonProperty("testUsers")
  private TestUsers testUsers;

  @JsonProperty("terminology")
  private TerminologyConfig terminologyConfig;

  @JsonProperty("cache")
  private CacheConfig cacheConfig;

  protected static final Logger log = LoggerFactory.getLogger(CedarConfig.class);

  private static CedarConfig instance;
  private static LinkedDataUtil linkedDataUtil;

  private static CedarConfig buildInstance(Map<String, String> environment) {

    CedarConfig config = null;

    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    final SubstitutingSourceProvider substitutingSourceProvider = new SubstitutingSourceProvider(
        new ClasspathConfigurationSourceProvider(), new CedarEnvironmentVariableSubstitutor(environment));

    // Read main config
    final String mainConfigFileName = "cedar-main.yml";

    final ConfigurationFactory<CedarConfig> mainConfigurationFactory = new YamlConfigurationFactory<>(
        CedarConfig.class, validator, Jackson.newObjectMapper(), "cedar");

    try {
      config = mainConfigurationFactory.build(substitutingSourceProvider, mainConfigFileName);
    } catch (IOException | ConfigurationException e) {
      log.error("Error while reading main config file", e);
      System.exit(-1);
    }

    // Read search config
    final String elasticSearchSettingsMappingsConfigFileName = "cedar-search.json";

    ElasticsearchSettingsMappingsConfig elasticsearchSettingsMappings = null;

    final ConfigurationFactory<ElasticsearchSettingsMappingsConfig> searchConfigurationFactory = new
        YamlConfigurationFactory<>(
        ElasticsearchSettingsMappingsConfig.class, validator, Jackson.newObjectMapper(), "cedar");

    try {
      elasticsearchSettingsMappings = searchConfigurationFactory.build(substitutingSourceProvider,
          elasticSearchSettingsMappingsConfigFileName);
    } catch (IOException | ConfigurationException e) {
      log.error("Error while reading search config file", e);
      System.exit(-2);
    }

    config.elasticsearchSettingsMappingsConfig = elasticsearchSettingsMappings;

    return config;
  }

  public static CedarConfig getInstance(Map<String, String> environment) {
    if (instance == null) {
      instance = buildInstance(environment);
      linkedDataUtil = new LinkedDataUtil(instance.getLinkedDataConfig());
    }
    return instance;
  }

  public String getHome() {
    return home;
  }

  public String getHost() {
    return host;
  }

  public AdminUserConfig getAdminUserConfig() {
    return adminUserConfig;
  }

  public KeycloakConfig getKeycloakConfig() {
    return keycloakConfig;
  }

  public MongoConfig getTemplateServerConfig() {
    return templateServerConfig;
  }

  public MongoConfig getUserServerConfig() {
    return userServerConfig;
  }

  public Neo4JConfig getNeo4jConfig() {
    return neo4jConfig;
  }

  public FolderStructureConfig getFolderStructureConfig() {
    return folderStructureConfig;
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

  public FolderRESTAPI getFolderRESTAPI() {
    return folderRESTAPI;
  }

  public TemplateRESTAPI getTemplateRESTAPI() {
    return templateRESTAPI;
  }

  public SubmissionConfig getSubmissionConfig() {
    return submissionConfig;
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

  public LinkedDataUtil getLinkedDataUtil() {
    return linkedDataUtil;
  }

}
