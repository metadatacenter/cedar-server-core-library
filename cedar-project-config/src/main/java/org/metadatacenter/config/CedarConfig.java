package org.metadatacenter.config;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import net.jmob.guice.conf.core.BindConfig;
import net.jmob.guice.conf.core.ConfigurationModule;
import net.jmob.guice.conf.core.InjectConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;

import java.util.List;

@BindConfig(value = "cedar")
public class CedarConfig extends AbstractModule {


  @InjectConfig("keycloak")
  private KeycloakConfig keycloakConfig;

  @InjectConfig("mongo")
  private MongoConfig mongoConfig;

  @InjectConfig("neo4j")
  private Neo4JConfig neo4jConfig;

  @InjectConfig("folderStructure")
  private FolderStructureConfig folderStructureConfig;

  @InjectConfig("folderRESTAPI")
  private FolderRESTAPI folderRESTAPI;

  @InjectConfig("linkedData")
  private LinkedDataConfig linkedDataConfig;

  @InjectConfig("blueprintUserProfile")
  private BlueprintUserProfile blueprintUserProfile;

  @InjectConfig("blueprintUIPreferences")
  private BlueprintUIPreferences blueprintUIPreferences;

  @InjectConfig("elasticsearch")
  private ElasticsearchConfig elasticsearchConfig;

  @InjectConfig("servers")
  private ServersConfig servers;

  @InjectConfig("searchSettings")
  private SearchSettings searchSettings;

  @InjectConfig("importExport")
  private ImportExportConfig importExportConfig;

  @InjectConfig("templateRESTAPI")
  private TemplateRESTAPI templateRESTAPI;

  @InjectConfig("templateRESTAPISummaries")
  private TemplateRESTAPISummaries templateRESTAPISummaries;

  @InjectConfig("test")
  private TestConfig testConfig;

  @InjectConfig("testUsers")
  private TestUsers testUsers;

  @InjectConfig("terminology")
  private TerminologyConfig terminologyConfig;

  private static final CedarConfig instance;

  static {
    instance = buildInstance();
  }

  @Inject
  private CedarConfig() {
  }

  @Override
  protected void configure() {
    install(ConfigurationModule.create());
  }

  private static CedarConfig buildInstance() {
    Injector injector = Guice.createInjector(new CedarConfig());
    return injector.getInstance(CedarConfig.class);
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

  public BlueprintUIPreferences getBlueprintUIPreferences() {
    return blueprintUIPreferences;
  }

  public String getMongoCollectionName(CedarNodeType nt) {
    return getMongoConfig().getCollections().get(nt.getValue());
  }

  public String getLinkedDataPrefix(CedarNodeType nodeType) {
    return getLinkedDataConfig().getBase() + nodeType.getPrefix() + "/";
  }
  
  public LinkedDataUtil getLinkedDataUtil() {
    return new LinkedDataUtil(getLinkedDataConfig());
  }

  public ElasticsearchConfig getElasticsearchConfig() {
    return elasticsearchConfig;
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


}
