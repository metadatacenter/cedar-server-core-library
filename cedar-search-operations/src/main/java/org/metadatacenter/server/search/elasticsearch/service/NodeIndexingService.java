package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.Client;
import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.info.FolderServerNodeInfo;
import org.metadatacenter.model.folderserver.basic.FolderServerResource;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.search.IndexingDocumentDocument;
import org.metadatacenter.server.search.elasticsearch.worker.ElasticsearchIndexingWorker;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.util.json.JsonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class NodeIndexingService extends AbstractIndexingService {

  private static final Logger log = LoggerFactory.getLogger(NodeIndexingService.class);

  private final ElasticsearchIndexingWorker indexWorker;

  NodeIndexingService(String indexName, Client client) {
    indexWorker = new ElasticsearchIndexingWorker(indexName, client);
  }

  public IndexingDocumentDocument createIndexDocument(FolderServerNode node, CedarNodeMaterializedPermissions permissions) throws CedarProcessingException {
    IndexingDocumentDocument ir = new IndexingDocumentDocument(node.getId());
    ir.setInfo(FolderServerNodeInfo.fromNode(node));
    ir.setMaterializedPermissions(permissions);
    ir.setSummaryText(getSummaryText(node));
    return ir;
  }

  public IndexedDocumentId indexDocument(FolderServerNode node, CedarNodeMaterializedPermissions permissions) throws CedarProcessingException {
    log.debug("Indexing node (id = " + node.getId() + ")");
    IndexingDocumentDocument ir = createIndexDocument(node, permissions);
    JsonNode jsonResource = JsonMapper.MAPPER.convertValue(ir, JsonNode.class);
    return indexWorker.addToIndex(jsonResource);
  }

  public IndexedDocumentId indexDocument(FolderServerNode node, CedarRequestContext c) throws CedarProcessingException {
    log.debug("Indexing node (id = " + node.getId() + ")");
    PermissionServiceSession permissionSession = CedarDataServices.getPermissionServiceSession(c);
    CedarNodeMaterializedPermissions permissions = permissionSession.getNodeMaterializedPermission(node.getId(),
        node.getType().asFolderOrResource());
    return indexDocument(node, permissions);
  }

  public void indexBatch(List<IndexingDocumentDocument> currentBatch) {
    indexWorker.addBatch(currentBatch);
  }

  private String getSummaryText(FolderServerNode node) {
    StringBuilder sb = new StringBuilder();
    sb.append(node.getName());
    sb.append(" ").append(node.getDescription());
    if (node instanceof FolderServerResource) {
      FolderServerResource resource = (FolderServerResource) node;
      ResourceVersion version = resource.getVersion();
      if (version != null) {
        sb.append(" ").append(version.getValue());
      }
      String identifier = resource.getIdentifier();
      if (identifier != null) {
        sb.append(" ").append(identifier);
      }
    }
    return sb.toString();
  }

  public long removeDocumentFromIndex(String nodeId) throws CedarProcessingException {
    if (nodeId != null) {
      log.debug("Removing node from index (id = " + nodeId);
      return indexWorker.removeAllFromIndex(nodeId);
    } else {
      return -1;
    }
  }

}
