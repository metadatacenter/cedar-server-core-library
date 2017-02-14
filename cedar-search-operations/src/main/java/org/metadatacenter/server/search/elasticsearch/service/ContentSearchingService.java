package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.server.search.elasticsearch.document.IndexingDocumentContent;
import org.metadatacenter.model.request.NodeListRequest;
import org.metadatacenter.model.response.FolderServerNodeListResponse;
import org.metadatacenter.model.search.IndexedDocumentType;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchSearchingWorker;
import org.metadatacenter.server.search.util.FolderServerUtil;
import org.metadatacenter.util.http.LinkHeaderUtil;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ContentSearchingService extends AbstractSearchingService {

  private static final Logger log = LoggerFactory.getLogger(ContentSearchingService.class);

  private ElasticsearchSearchingWorker searchWorker;

  ContentSearchingService(CedarConfig cedarConfig, Client client) {
    searchWorker = new ElasticsearchSearchingWorker(cedarConfig.getElasticsearchConfig(), client, IndexedDocumentType
        .CONTENT);
  }


  public FolderServerNodeListResponse ¡search(String folderBase, String query, List<String> resourceTypes,
                                             String templateId, List<String> sortList, int limit, int offset, String
                                                 absoluteUrl, CedarRequestContext context) throws
      CedarProcessingException {

    // Accessible node ids
    HashMap<String, String> accessibleNodeIds = new HashMap(FolderServerUtil.getAccessibleNodeIds(folderBase, context));

    try {

      // The offset for the resources accessible by the user needs to be translated to the offset at the index level
      int offsetIndex = 0;
      int count = 0;
      while (count < offset) {
        SearchResponse esResults = searchWorker.search(query, resourceTypes, sortList, templateId, limit, offsetIndex);
        if (esResults.getHits().getHits().length == 0) {
          break;
        }
        for (SearchHit hit : esResults.getHits()) {
          String hitJson = hit.sourceAsString();
          IndexingDocumentContent resource = JsonMapper.MAPPER.readValue(hitJson, IndexingDocumentContent.class);
          // The resource is taken into account only if the user has access to it
          if (accessibleNodeIds.containsKey(resource.getInfo().getId())) {
            count++;
          }
          offsetIndex++;
          if (count == offset) {
            break;
          }
        }
      }

      // Retrieve resources
      FolderServerNodeListResponse response = new FolderServerNodeListResponse();
      List<FolderServerNode> resources = new ArrayList<>();
      SearchResponse esResults = null;
      while (resources.size() < limit) {
        esResults = searchWorker.search(query, resourceTypes, sortList, templateId, limit, offsetIndex);
        if (esResults.getHits().getHits().length == 0) {
          break;
        }
        for (SearchHit hit : esResults.getHits()) {
          String hitJson = hit.sourceAsString();
          IndexingDocumentContent resource = JsonMapper.MAPPER.readValue(hitJson, IndexingDocumentContent.class);
          // The resource is added to the results only if the user has access to it
          if (accessibleNodeIds.containsKey(resource.getInfo().getId())) {
            resources.add(resource.getInfo());
            if (resources.size() == limit) {
              break;
            }
          }
          offsetIndex++;
        }
      }

      // The total number of resources accessible by the user is sometimes too expensive to compute with our current
      // (temporal)
      // approach, so we set the total to -1 unless offsetIndex = totalHits, which means that there are no more
      // resources
      // in the index that match the query and that we can be sure about the number of resources that the user has
      // access to.
      long total = -1;
      if (offsetIndex == esResults.getHits().getTotalHits()) {
        total = resources.size();
      }
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
      req.setLimit(limit);
      req.setOffset(offset);
      req.setSort(sortList);
      req.setQ(query);
      req.setDerivedFromId(templateId);
      response.setRequest(req);

      return response;
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
  }

  // TODO: Update to take into account the user's access permissions to resources
  public FolderServerNodeListResponse searchDeep(String query, List<String> resourceTypes, String templateId,
                                                 List<String> sortList, int limit) throws CedarProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    try {
      List<SearchHit> esHits = searchWorker.searchDeep(query, resourceTypes, sortList, templateId, limit);

      // Apply limit
      if (esHits.size() >= limit) {
        esHits = esHits.subList(0, limit);
      }

      FolderServerNodeListResponse response = new FolderServerNodeListResponse();
      List<FolderServerNode> resources = new ArrayList<>();
      for (SearchHit hit : esHits) {
        String hitJson = hit.sourceAsString();
        IndexingDocumentContent resource = mapper.readValue(hitJson, IndexingDocumentContent.class);
        resources.add(resource.getInfo());
      }

      response.setTotalCount(resources.size());
      response.setResources(resources);

      List<CedarNodeType> nodeTypeList = new ArrayList<>();
      if (resourceTypes != null) {
        for (String rt : resourceTypes) {
          nodeTypeList.add(CedarNodeType.forValue(rt));
        }
      }
      NodeListRequest req = new NodeListRequest();
      req.setNodeTypes(nodeTypeList);
      req.setLimit(limit);
      req.setSort(sortList);
      response.setRequest(req);

      return response;
    } catch (Exception e) {
      throw new CedarProcessingException(e);
    }
  }

  public List<String> findAllValuesForField(String fieldName) throws CedarProcessingException {
    return searchWorker.findAllValuesForField(fieldName);
  }

}
