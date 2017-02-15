package org.metadatacenter.server.search.elasticsearch.service;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.AliasOrIndex;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.config.ElasticsearchMappingsConfig;
import org.metadatacenter.config.ElasticsearchSettingsMappingsConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ElasticsearchManagementService {

  private static final Logger log = LoggerFactory.getLogger(ElasticsearchManagementService.class);

  private final ElasticsearchConfig config;
  private final Settings settings;
  private final HashMap<String, Object> indexSettings;
  private final ElasticsearchMappingsConfig indexMappings;
  private Client elasticClient = null;

  public ElasticsearchManagementService(ElasticsearchConfig config, ElasticsearchSettingsMappingsConfig
      settingsMappings) {
    this.config = config;
    this.indexSettings = settingsMappings.getSettings();
    this.indexMappings = settingsMappings.getMappings();
    this.settings = Settings.settingsBuilder().put("cluster.name", config.getClusterName()).build();
  }

  Client getClient() {
    try {
      if (elasticClient == null) {
        elasticClient = TransportClient.builder().settings(settings).build().addTransportAddress(new
            InetSocketTransportAddress(InetAddress.getByName(config.getHost()), config.getTransportPort()));
      }
      return elasticClient;
    } catch (Exception e) {
      log.error("There was an error creating the elasticsearch client", e);
      return null;
    }
  }

  public void closeClient() {
    elasticClient.close();
  }

  public void createIndex(String indexName) throws CedarProcessingException {
    Client client = getClient();
    CreateIndexRequestBuilder createIndexRequestBuilder = client.admin().indices().prepareCreate(indexName);
    // Set settings
    if (indexSettings != null) {
      createIndexRequestBuilder.setSettings(indexSettings);
    }
    // Put mappings
    if (indexMappings.getNode() != null) {
      createIndexRequestBuilder.addMapping(config.getType(IndexedDocumentType.NODE), indexMappings.getNode());
    }
    if (indexMappings.getUsers() != null) {
      createIndexRequestBuilder.addMapping(config.getType(IndexedDocumentType.USERS), indexMappings.getUsers());
    }
    if (indexMappings.getGroups() != null) {
      createIndexRequestBuilder.addMapping(config.getType(IndexedDocumentType.GROUPS), indexMappings.getGroups());
    }
    if (indexMappings.getContent() != null) {
      createIndexRequestBuilder.addMapping(config.getType(IndexedDocumentType.CONTENT), indexMappings.getContent());
    }
    // Create index
    CreateIndexResponse response = createIndexRequestBuilder.execute().actionGet();
    if (!response.isAcknowledged()) {
      throw new CedarProcessingException("Failed to create the index " + indexName);
    } else {
      log.info("The index " + indexName + " has been created");
    }
  }

  public boolean indexExists(String indexName) {
    return getClient().admin().indices().prepareExists(indexName).execute().actionGet().isExists();
  }

  public boolean deleteIndex(String indexName) throws CedarProcessingException {
    DeleteIndexResponse deleteIndexResponse =
        getClient().admin().indices().delete(new DeleteIndexRequest(indexName)).actionGet();
    if (!deleteIndexResponse.isAcknowledged()) {
      throw new CedarProcessingException("Failed to delete index '" + indexName + "'");
    } else {
      log.info("The index '" + indexName + "' has been deleted");
      return true;
    }
  }

  public boolean addAlias(String indexName, String aliasName) throws CedarProcessingException {
    IndicesAliasesResponse response = getClient().admin().indices().prepareAliases()
        .addAlias(indexName, aliasName)
        .execute().actionGet();
    if (!response.isAcknowledged()) {
      throw new CedarProcessingException("Failed to add alias '" + aliasName + "' to index '" + indexName + "'");
    } else {
      log.info("The alias '" + aliasName + "' has been added to index '" + indexName + "'");
      return true;
    }
  }

  public boolean deleteAlias(String indexName, String aliasName) throws CedarProcessingException {
    IndicesAliasesResponse response = getClient().admin().indices().prepareAliases()
        .removeAlias(indexName, aliasName)
        .execute().actionGet();
    if (!response.isAcknowledged()) {
      throw new CedarProcessingException("Failed to remove alias '" + aliasName + "' from index '" + indexName + "'");
    } else {
      log.info("The alias '" + aliasName + "' has been removed from the index '" + indexName + "'");
      return true;
    }
  }

  public List<String> getAllIndices() {
    List<String> indexNames = new ArrayList<>();
    ImmutableOpenMap<String, IndexMetaData> indices = getClient().admin().cluster()
        .prepareState().execute()
        .actionGet().getState()
        .getMetaData().getIndices();
    for (ObjectCursor<IndexMetaData> indexMetaDataObjectCursor : indices.values()) {
      IndexMetaData value = indexMetaDataObjectCursor.value;
      indexNames.add(value.getIndex());
    }
    return indexNames;
  }

}
