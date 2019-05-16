package org.metadatacenter.server.search.permission;

import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.Upsert;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerArtifact;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.rest.context.CedarRequestContextFactory;
import org.metadatacenter.server.FolderServiceSession;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.search.SearchPermissionQueueEvent;
import org.metadatacenter.server.search.elasticsearch.service.NodeIndexingService;
import org.metadatacenter.server.search.elasticsearch.service.NodeSearchingService;
import org.metadatacenter.server.search.util.IndexUtils;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchPermissionExecutorService {

  private static final Logger log = LoggerFactory.getLogger(SearchPermissionExecutorService.class);

  private final FolderServiceSession folderSession;
  private final PermissionServiceSession permissionSession;
  private final NodeSearchingService nodeSearchingService;
  private final NodeIndexingService nodeIndexingService;
  private final IndexUtils indexUtils;
  private final CedarRequestContext cedarRequestContext;

  public SearchPermissionExecutorService(CedarConfig cedarConfig,
                                         IndexUtils indexUtils,
                                         NodeSearchingService nodeSearchingService,
                                         NodeIndexingService nodeIndexingService) {
    UserService userService = CedarDataServices.getNeoUserService();
    this.nodeSearchingService = nodeSearchingService;
    this.nodeIndexingService = nodeIndexingService;
    this.indexUtils = indexUtils;

    this.cedarRequestContext = CedarRequestContextFactory.fromAdminUser(cedarConfig, userService);

    folderSession = CedarDataServices.getFolderServiceSession(cedarRequestContext);
    permissionSession = CedarDataServices.getPermissionServiceSession(cedarRequestContext);
  }

  // Main entry point
  public void handleEvent(SearchPermissionQueueEvent event) {
    switch (event.getEventType()) {
      case RESOURCE_MOVED:
        updateOneResource(event.getId());
        break;
      case RESOURCE_PERMISSION_CHANGED:
        updateOneResource(event.getId());
        break;
      case FOLDER_MOVED:
        updateFolderRecursively(event.getId());
        break;
      case FOLDER_PERMISSION_CHANGED:
        updateFolderRecursively(event.getId());
        break;
      case GROUP_MEMBERS_UPDATED:
        updateAllByUpdatedGroup(event.getId());
        break;
      case GROUP_DELETED:
        updateAllByDeletedGroup(event.getId());
        break;
    }
  }

  private void updateOneResource(String id) {
    FolderServerArtifact resource = folderSession.findArtifactById(id);
    if (resource != null) {
      log.debug("Update one artifact:" + resource.getName());
      upsertOnePermissions(Upsert.UPDATE, id);
    } else {
      log.error("Resource was not found:" + id);
    }
  }

  private void updateFolderRecursively(String id) {
    log.debug("Update recursive folder:");
    List<FileSystemResource> subtree = folderSession.findAllDescendantNodesById(id);
    for (FileSystemResource n : subtree) {
      upsertOnePermissions(Upsert.UPDATE, n.getId());
    }
  }

  private void updateAllByUpdatedGroup(String id) {
    log.debug("Update all visible by group:");
    List<FileSystemResource> collection = folderSession.findAllNodesVisibleByGroupId(id);
    for (FileSystemResource n : collection) {
      if (indexUtils.needsIndexing(n)) {
        upsertOnePermissions(Upsert.UPDATE, n.getId());
      } else {
        log.info("The resource was skipped from indexing:" + n.getId());
      }
    }
  }

  private void updateAllByDeletedGroup(String id) {
    log.debug("Update all visible by group:");
    List<String> allCedarIdsForGroup = null;
    try {
      allCedarIdsForGroup = nodeSearchingService.findAllCedarIdsForGroup(id);
    } catch (CedarProcessingException e) {
      log.error("Error while retrieving all the affected documents for group:" + id);
      return;
    }

    for (String cid : allCedarIdsForGroup) {
      log.info("Need to update permissions for:" + cid);
      upsertOnePermissions(Upsert.UPDATE, cid);
    }
  }

  private void upsertOnePermissions(Upsert upsert, String id) {
    log.debug("upsertOneDocument for permissions:" + upsert.getValue() + ":" + id);
    try {
      FileSystemResource node = folderSession.findResourceById(id);
      CedarNodeMaterializedPermissions perm = permissionSession.getNodeMaterializedPermission(id);
      if (upsert == Upsert.UPDATE) {
        nodeIndexingService.removeDocumentFromIndex(id);
      }
      nodeIndexingService.indexDocument(node, perm, cedarRequestContext);
    } catch (Exception e) {
      log.error("Error while upserting permissions", e);
    }
  }
}
