package org.metadatacenter.server.search.elasticsearch.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.elasticsearch.client.Client;
import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.ResourceVersion;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerArtifact;
import org.metadatacenter.model.folderserver.basic.FolderServerSchemaArtifact;
import org.metadatacenter.model.folderserver.info.FolderServerNodeInfo;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.search.IndexingDocumentDocument;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.search.IndexedDocumentId;
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

  public IndexingDocumentDocument createIndexDocument(FileSystemResource node,
                                                      CedarNodeMaterializedPermissions permissions) {
    IndexingDocumentDocument ir = new IndexingDocumentDocument(node.getId());
    ir.setInfo(FolderServerNodeInfo.fromNode(node));
    ir.setMaterializedPermissions(permissions);
    ir.setSummaryText(getSummaryText(node));
    return ir;
  }

  public IndexedDocumentId indexDocument(FileSystemResource node, CedarNodeMaterializedPermissions permissions)
      throws CedarProcessingException {
    log.debug("Indexing resource (id = " + node.getId() + ")");
    IndexingDocumentDocument ir = createIndexDocument(node, permissions);
    JsonNode jsonResource = JsonMapper.MAPPER.convertValue(ir, JsonNode.class);
    return indexWorker.addToIndex(jsonResource);
  }

  public IndexedDocumentId indexDocument(FileSystemResource node, CedarRequestContext c)
      throws CedarProcessingException {
    log.debug("Indexing resource (id = " + node.getId() + ")");
    PermissionServiceSession permissionSession = CedarDataServices.getPermissionServiceSession(c);
    CedarNodeMaterializedPermissions permissions = permissionSession.getNodeMaterializedPermission(node.getId());
    return indexDocument(node, permissions);
  }

  public void indexBatch(List<IndexingDocumentDocument> currentBatch) {
    indexWorker.addBatch(currentBatch);
  }

  private String getSummaryText(FileSystemResource node) {
    StringBuilder sb = new StringBuilder();
    if (node.getName() != null) {
      sb.append(node.getName());
    }
    if (node.getDescription() != null && !node.getDescription().isBlank()) {
      if (sb.length() > 0) {
        sb.append(" ");
      }
      sb.append(node.getDescription().trim());
    }
    if (node instanceof FolderServerArtifact) {
      if (node instanceof FolderServerSchemaArtifact) {
        FolderServerSchemaArtifact resource = (FolderServerSchemaArtifact) node;
        ResourceVersion version = resource.getVersion();
        if (version != null && version.getValue() != null && !version.getValue().isBlank()) {
          if (sb.length() > 0) {
            sb.append(" ");
          }
          sb.append(version.getValue().trim());
        }
      }

      FolderServerArtifact resource = (FolderServerArtifact) node;
      String identifier = resource.getIdentifier();
      if (identifier != null && !identifier.isBlank()) {
        if (sb.length() > 0) {
          sb.append(" ");
        }
        sb.append(identifier.trim());
      }
    }
    return sb.toString();
  }

  public long removeDocumentFromIndex(String nodeId) throws CedarProcessingException {
    if (nodeId != null) {
      log.debug("Removing resource from index (id = " + nodeId);
      return indexWorker.removeAllFromIndex(nodeId);
    } else {
      return -1;
    }
  }

}
