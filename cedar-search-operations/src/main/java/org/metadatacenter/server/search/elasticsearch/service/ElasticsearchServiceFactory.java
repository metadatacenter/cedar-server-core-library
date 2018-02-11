package org.metadatacenter.server.search.elasticsearch.service;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;

public class ElasticsearchServiceFactory {

  private static ElasticsearchServiceFactory instance;

  private CedarConfig cedarConfig;
  private ElasticsearchManagementService managementService;

  private ElasticsearchServiceFactory() {
  }

  public static synchronized ElasticsearchServiceFactory getInstance(CedarConfig cedarConfig) {
    if (instance == null) {
      instance = new ElasticsearchServiceFactory();
      instance.init(cedarConfig);
    }
    return instance;
  }

  private void init(CedarConfig cedarConfig) {
    this.cedarConfig = cedarConfig;
    this.managementService = new ElasticsearchManagementService(cedarConfig.getElasticsearchConfig(), cedarConfig
        .getElasticsearchSearchIndexSettingsMappingsConfig(), cedarConfig.getElasticsearchVrIndexSettingsMappingsConfig());
  }

  public ElasticsearchManagementService getManagementService() {
    return managementService;
  }

  public NodeIndexingService nodeIndexingService() throws CedarProcessingException {
    return nodeIndexingService(instance.cedarConfig.getElasticsearchConfig().getIndexes().getSearchIndex().getName());
  }

  public NodeIndexingService nodeIndexingService(String indexName) throws CedarProcessingException {
    return new NodeIndexingService(indexName, instance.cedarConfig, instance.managementService.getClient());
  }

  public UserPermissionIndexingService userPermissionsIndexingService() {
    return userPermissionsIndexingService(instance.cedarConfig.getElasticsearchConfig().getIndexes().getSearchIndex().getName());
  }

  public UserPermissionIndexingService userPermissionsIndexingService(String indexName) {
    return new UserPermissionIndexingService(indexName, instance.cedarConfig, instance.managementService.getClient());
  }

  public GroupPermissionIndexingService groupPermissionsIndexingService() {
    return groupPermissionsIndexingService(instance.cedarConfig.getElasticsearchConfig().getIndexes().getSearchIndex().getName());
  }

  public GroupPermissionIndexingService groupPermissionsIndexingService(String indexName) {
    return new GroupPermissionIndexingService(indexName, instance.cedarConfig, instance.managementService.getClient());
  }

  public ContentIndexingService contentIndexingService() {
    return contentIndexingService(instance.cedarConfig.getElasticsearchConfig().getIndexes().getSearchIndex().getName());
  }

  public ContentIndexingService contentIndexingService(String indexName) {
    return new ContentIndexingService(indexName, instance.cedarConfig, instance.managementService.getClient());
  }

  public ContentSearchingService contentSearchingService() {
    return new ContentSearchingService(instance.cedarConfig, instance.managementService.getClient());
  }

  public NodeSearchingService nodeSearchingService() {
    return new NodeSearchingService(instance.cedarConfig, instance.managementService.getClient());
  }

  public GroupPermissionSearchingService groupPermissionSearchingService() {
    return new GroupPermissionSearchingService(instance.cedarConfig, instance.managementService.getClient());
  }

  public ValueRecommenderIndexingService valueRecommenderIndexingService() {
    return valueRecommenderIndexingService(instance.cedarConfig.getElasticsearchConfig().getIndexes().getValueRecommenderIndex().getName());
  }

  public ValueRecommenderIndexingService valueRecommenderIndexingService(String indexName) {
    return new ValueRecommenderIndexingService(indexName, instance.cedarConfig, instance.managementService.getClient());
  }

}
