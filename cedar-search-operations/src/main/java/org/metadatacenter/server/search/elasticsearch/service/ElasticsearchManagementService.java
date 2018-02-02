package org.metadatacenter.server.search.elasticsearch.service;

import com.carrotsearch.hppc.cursors.ObjectCursor;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesResponse;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.metadatacenter.config.*;
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
  private final HashMap<String, Object> searchIndexSettings;
  private final ElasticsearchSearchIndexMappingsConfig searchIndexMappings;
  private final HashMap<String, Object> vrIndexSettings;
  private final ElasticsearchVrIndexMappingsConfig vrIndexMappings;
  private Client elasticClient = null;

  public ElasticsearchManagementService(ElasticsearchConfig config, ElasticsearchSearchIndexSettingsMappingsConfig
      searchIndexSettingsMappings, ElasticsearchVrIndexSettingsMappingsConfig vrIndexSettingsMappings) {
    this.config = config;
    this.searchIndexSettings = searchIndexSettingsMappings.getSettings();
    this.searchIndexMappings = searchIndexSettingsMappings.getMappings();
    this.vrIndexSettings = vrIndexSettingsMappings.getSettings();
    this.vrIndexMappings = vrIndexSettingsMappings.getMappings();
    this.settings = Settings.builder().put("cluster.name", config.getClusterName()).build();
  }

  Client getClient() {
    try {
      if (elasticClient == null) {
        elasticClient = new PreBuiltTransportClient(settings).addTransportAddress(new InetSocketTransportAddress
            (InetAddress.getByName(config.getHost()), config.getTransportPort()));
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

    // Search Index
    if (indexName.equals(config.getIndexes().getSearchIndex().getName())) {

      // Set settings
      if (searchIndexSettings != null) {
        createIndexRequestBuilder.setSettings(searchIndexSettings);
      }
      // Put mappings
      if (searchIndexMappings.getNode() != null) {
        createIndexRequestBuilder.addMapping(config.getIndexes().getSearchIndex().
            getType(IndexedDocumentType.NODE), searchIndexMappings.getNode());
      }
      if (searchIndexMappings.getUsers() != null) {
        createIndexRequestBuilder.addMapping(config.getIndexes().getSearchIndex().
            getType(IndexedDocumentType.USERS), searchIndexMappings.getUsers());
      }
      if (searchIndexMappings.getGroups() != null) {
        createIndexRequestBuilder.addMapping(config.getIndexes().getSearchIndex().
            getType(IndexedDocumentType.GROUPS), searchIndexMappings.getGroups());
      }
      if (searchIndexMappings.getContent() != null) {
        createIndexRequestBuilder.addMapping(config.getIndexes().getSearchIndex().
            getType(IndexedDocumentType.CONTENT), searchIndexMappings.getContent());
      }

    } else if (indexName.equals(config.getIndexes().getValueRecommenderIndex().getName())) {

      // Set settings
      if (vrIndexSettings != null) {
        createIndexRequestBuilder.setSettings(vrIndexSettings);
      }
      // Put mappings
      // (no mappings defined yet)

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
      indexNames.add(value.getIndex().getName());
    }
    return indexNames;
  }

}
