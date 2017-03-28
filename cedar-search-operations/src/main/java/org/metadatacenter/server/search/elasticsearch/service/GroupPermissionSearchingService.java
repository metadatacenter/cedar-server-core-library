package org.metadatacenter.server.search.elasticsearch.service;

import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchSearchingWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GroupPermissionSearchingService extends AbstractSearchingService {

  private static final Logger log = LoggerFactory.getLogger(GroupPermissionSearchingService.class);

  private final ElasticsearchSearchingWorker searchWorker;

  GroupPermissionSearchingService(CedarConfig cedarConfig, Client client) {
    Client client1 = client;
    ElasticsearchConfig config = cedarConfig.getElasticsearchConfig();
    searchWorker = new ElasticsearchSearchingWorker(cedarConfig.getElasticsearchConfig(), client, IndexedDocumentType
        .GROUPS);
  }

  public List<String> findAllCedarIdsForGroup(String groupId) throws CedarProcessingException {
    QueryBuilder queryBuilder = QueryBuilders.termQuery("groups.id", groupId);
    return searchWorker.findAllValuesForField("cid", queryBuilder);
  }

}
