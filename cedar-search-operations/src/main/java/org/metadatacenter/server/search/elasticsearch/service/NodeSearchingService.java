package org.metadatacenter.server.search.elasticsearch.service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.config.ElasticsearchConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerNodeInfo;
import org.metadatacenter.model.folderserverextract.FolderServerNodeExtract;
import org.metadatacenter.model.request.NodeListRequest;
import org.metadatacenter.model.response.FolderServerNodeListResponse;
import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.search.IndexedDocumentDocument;
import org.metadatacenter.search.IndexedDocumentType;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.permission.CurrentUserPermissionUpdaterForSearchFolder;
import org.metadatacenter.server.search.elasticsearch.permission.CurrentUserPermissionUpdaterForSearchResource;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchPermissionEnabledContentSearchingWorker;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchSearchingWorker;
import org.metadatacenter.server.search.elasticsearch.worker.SearchResponseResult;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.ResourcePublicationStatusFilter;
import org.metadatacenter.server.security.model.user.ResourceVersionFilter;
import org.metadatacenter.util.http.LinkHeaderUtil;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.metadatacenter.constant.ElasticsearchConstants.DOCUMENT_CEDAR_ID;

public class NodeSearchingService extends AbstractSearchingService {

  private static final Logger log = LoggerFactory.getLogger(NodeSearchingService.class);

  private final Client client;
  private final ElasticsearchConfig config;
  private final ElasticsearchPermissionEnabledContentSearchingWorker permissionEnabledSearchWorker;
  private final ElasticsearchSearchingWorker searchWorker;

  NodeSearchingService(CedarConfig cedarConfig, Client client) {
    this.client = client;
    this.config = cedarConfig.getElasticsearchConfig();
    permissionEnabledSearchWorker =
        new ElasticsearchPermissionEnabledContentSearchingWorker(cedarConfig.getElasticsearchConfig(), client);
    searchWorker = new ElasticsearchSearchingWorker(cedarConfig.getElasticsearchConfig(), client);
  }

  public IndexedDocumentId getByCedarId(String resourceId) throws CedarProcessingException {
    return getByCedarId(client, resourceId, config.getIndexName(), IndexedDocumentType.DOC.getValue());
  }

  public IndexedDocumentDocument getDocumentByCedarId(String resourceId) throws CedarProcessingException {
    try {
      // Get resources by resource id
      SearchResponse responseSearch =
          client.prepareSearch(config.getIndexName()).setTypes(IndexedDocumentType.DOC.getValue())
              .setQuery(QueryBuilders.matchQuery(DOCUMENT_CEDAR_ID, resourceId))
              .execute().actionGet();
      for (SearchHit hit : responseSearch.getHits()) {
        if (hit != null) {
          Map<String, Object> map = hit.getSourceAsMap();
          return new IndexedDocumentDocument((String) map.get(DOCUMENT_CEDAR_ID));
        }
      }
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
    return null;
  }

  public List<String> findAllValuesForField(String fieldName) throws CedarProcessingException {
    return searchWorker.findAllValuesForField(fieldName);
  }

  public List<String> findAllCedarIdsForGroup(String groupId) throws CedarProcessingException {
    QueryBuilder queryBuilder = QueryBuilders.termQuery("groups.id", groupId);
    return searchWorker.findAllValuesForField("cid", queryBuilder);
  }

  public FolderServerNodeListResponse search(CedarRequestContext rctx, String query, String id, List<String>
      resourceTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, String
                                                 isBasedOn, List<String> sortList, int limit, int offset, String
                                                 absoluteUrl, CedarConfig cedarConfig) throws CedarProcessingException {
    try {
      SearchResponseResult searchResult = permissionEnabledSearchWorker.search(rctx, query, resourceTypes, version,
          publicationStatus, sortList, isBasedOn, limit, offset);
      return assembleResponse(searchResult, query, id, resourceTypes, version, publicationStatus, isBasedOn, sortList,
          limit, offset, absoluteUrl, rctx.getCedarUser(), cedarConfig);
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
  }


  public FolderServerNodeListResponse searchDeep(CedarRequestContext rctx, String query, String id, List<String>
      resourceTypes, ResourceVersionFilter version, ResourcePublicationStatusFilter publicationStatus, String
                                                     isBasedOn, List<String> sortList, int limit, int offset, String
                                                     absoluteUrl, CedarConfig cedarConfig)
      throws CedarProcessingException {
    try {
      SearchResponseResult searchResult = permissionEnabledSearchWorker.searchDeep(rctx, query, resourceTypes, version,
          publicationStatus, sortList, isBasedOn, limit, offset);
      return assembleResponse(searchResult, query, id, resourceTypes, version, publicationStatus, isBasedOn, sortList,
          limit, offset, absoluteUrl, rctx.getCedarUser(), cedarConfig);
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
  }

  private FolderServerNodeListResponse assembleResponse(SearchResponseResult searchResult, String query, String id,
                                                        List<String> resourceTypes, ResourceVersionFilter version,
                                                        ResourcePublicationStatusFilter publicationStatus, String
                                                            templateId, List<String> sortList, int limit, int offset,
                                                        String absoluteUrl, CedarUser cedarUser,
                                                        CedarConfig cedarConfig) {
    List<FolderServerNodeExtract> resources = new ArrayList<>();

    // Get the object from the result
    for (SearchHit hit : searchResult.getHits()) {
      String hitJson = hit.getSourceAsString();
      try {
        IndexedDocumentDocument indexedDocument = JsonMapper.MAPPER.readValue(hitJson, IndexedDocumentDocument.class);

        FolderServerNodeInfo info = indexedDocument.getInfo();
        FolderServerNodeExtract folderServerNodeExtract = FolderServerNodeExtract.fromNodeInfo(info);
        CurrentUserPermissionUpdater cupu;
        if (folderServerNodeExtract.getType() == CedarNodeType.FOLDER) {
          cupu = CurrentUserPermissionUpdaterForSearchFolder.get(indexedDocument, cedarUser, cedarConfig);
        } else {
          cupu = CurrentUserPermissionUpdaterForSearchResource.get(indexedDocument, cedarUser, cedarConfig);
        }
        cupu.update(folderServerNodeExtract.getCurrentUserPermissions());

        resources.add(folderServerNodeExtract);
      } catch (IOException e) {
        log.error("Error while deserializing the search result document", e);
      }
    }

    FolderServerNodeListResponse response = new FolderServerNodeListResponse();

    long total = searchResult.getTotalCount();

    response.setTotalCount(total);
    response.setCurrentOffset(offset);
    response.setPaging(LinkHeaderUtil.getPagingLinkHeaders(absoluteUrl, total, limit, offset));
    response.setResources(resources);

    List<CedarNodeType> nodeTypeList = new ArrayList<>();
    if (resourceTypes != null) {
      for (String rt : resourceTypes) {
        nodeTypeList.add(CedarNodeType.forValue(rt));
      }
    }

    NodeListRequest req = new NodeListRequest();
    req.setNodeTypes(nodeTypeList);
    req.setVersion(version);
    req.setPublicationStatus(publicationStatus);
    req.setLimit(limit);
    req.setOffset(offset);
    req.setSort(sortList);
    req.setQ(query);
    req.setId(id);
    req.setIsBasedOn(templateId);
    response.setRequest(req);

    return response;
  }

}
