package org.metadatacenter.server.search.permission;

import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.Upsert;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerResource;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.rest.context.CedarRequestContextFactory;
import org.metadatacenter.server.FolderServiceSession;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.search.SearchPermissionQueueEvent;
import org.metadatacenter.server.search.IndexedDocumentId;
import org.metadatacenter.server.search.elasticsearch.document.IndexedDocumentNode;
import org.metadatacenter.server.search.elasticsearch.document.IndexingDocumentNode;
import org.metadatacenter.server.search.elasticsearch.service.GroupPermissionIndexingService;
import org.metadatacenter.server.search.elasticsearch.service.GroupPermissionSearchingService;
import org.metadatacenter.server.search.elasticsearch.service.NodeSearchingService;
import org.metadatacenter.server.search.elasticsearch.service.UserPermissionIndexingService;
import org.metadatacenter.server.search.util.IndexUtils;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SearchPermissionExecutorService {

  private static final Logger log = LoggerFactory.getLogger(SearchPermissionExecutorService.class);

  private final UserService userService;
  private FolderServiceSession folderSession;
  private PermissionServiceSession permissionSession;
  private final UserPermissionIndexingService userPermissionIndexingService;
  private final GroupPermissionIndexingService groupPermissionIndexingService;
  private final NodeSearchingService nodeSearchingService;
  private final GroupPermissionSearchingService groupPermissionSearchingService;
  private final IndexUtils indexUtils;

  public SearchPermissionExecutorService(CedarConfig cedarConfig,
                                         IndexUtils indexUtils,
                                         UserPermissionIndexingService userPermissionIndexingService,
                                         GroupPermissionIndexingService groupPermissionIndexingService,
                                         NodeSearchingService nodeSearchingService,
                                         GroupPermissionSearchingService groupPermissionSearchingService) {
    userService = CedarDataServices.getUserService();
    this.indexUtils = indexUtils;
    this.userPermissionIndexingService = userPermissionIndexingService;
    this.groupPermissionIndexingService = groupPermissionIndexingService;
    this.nodeSearchingService = nodeSearchingService;
    this.groupPermissionSearchingService = groupPermissionSearchingService;
    String adminUserUUID = cedarConfig.getAdminUserConfig().getUuid();
    CedarUser adminUser = null;
    try {
      adminUser = userService.findUser(adminUserUUID);
    } catch (Exception ex) {
      log.error("Error while looking up admin user", ex);
    }
    if (adminUser != null) {
      try {
        CedarRequestContext cedarRequestContext = CedarRequestContextFactory.fromUser(adminUser);
        folderSession = CedarDataServices.getFolderServiceSession(cedarRequestContext);
        permissionSession = CedarDataServices.getPermissionServiceSession(cedarRequestContext);
      } catch (CedarAccessException ex) {
        log.error("Error while building folder service sessions", ex);
      }
    }
  }

  // Main entry point
  public void handleEvent(SearchPermissionQueueEvent event) {
    IndexedDocumentId parent = event.getParentId();
    switch (event.getEventType()) {
      case RESOURCE_CREATED:
        createOneResource(event.getId(), parent);
        break;
      case RESOURCE_MOVED:
        updateOneResource(event.getId());
        break;
      case RESOURCE_PERMISSION_CHANGED:
        updateOneResource(event.getId());
        break;
      case FOLDER_CREATED:
        createOneFolder(event.getId(), parent);
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

  //Routers for individual cases
  private void createOneResource(String id, IndexedDocumentId parent) {
    FolderServerResource resource = folderSession.findResourceById(id);
    if (resource != null) {
      log.debug("Create one resource:" + resource.getDisplayName());
      upsertOnePermissions(Upsert.INSERT, id, FolderOrResource.RESOURCE, parent);
    } else {
      log.error("Resource was not found:" + id);
    }
  }

  private void updateOneResource(String id) {
    FolderServerResource resource = folderSession.findResourceById(id);
    if (resource != null) {
      try {
        log.debug("Update one resource:" + resource.getDisplayName());
        IndexedDocumentId parentId = nodeSearchingService.getByCedarId(id);
        upsertOnePermissions(Upsert.UPDATE, id, FolderOrResource.RESOURCE, parentId);
      } catch (CedarProcessingException e) {
        log.error("There was an error while updating permissions for resource:" + id, e);
      }
    } else {
      log.error("Resource was not found:" + id);
    }
  }

  private void createOneFolder(String id, IndexedDocumentId parent) {
    FolderServerFolder folder = folderSession.findFolderById(id);
    if (folder != null) {
      log.debug("Create one folder:" + folder.getDisplayName());
      upsertOnePermissions(Upsert.INSERT, id, FolderOrResource.FOLDER, parent);
    } else {
      log.error("Folder was not found:" + id);
    }
  }

  private void updateFolderRecursively(String id) {
    log.debug("Update recursive folder:");
    List<FolderServerNode> subtree = folderSession.findAllDescendantNodesById(id);
    for (FolderServerNode n : subtree) {
      try {
        IndexedDocumentId parentId = nodeSearchingService.getByCedarId(n.getId());
        upsertOnePermissions(Upsert.UPDATE, n.getId(), n.getType(), parentId);
      } catch (CedarProcessingException e) {
        log.error("There was an error while updating permissions recursively for folder:" + id
            + " node:" + n.getId(), e);
      }
    }
  }

  private void updateAllByUpdatedGroup(String id) {
    log.debug("Update all visible by group:");
    List<FolderServerNode> collection = folderSession.findAllNodesVisibleByGroupId(id);
    for (FolderServerNode n : collection) {
      if (indexUtils.needsIndexing(n)) {
        try {
          IndexedDocumentId parentId = nodeSearchingService.getByCedarId(n.getId());
          upsertOnePermissions(Upsert.UPDATE, n.getId(), n.getType(), parentId);
        } catch (CedarProcessingException e) {
          log.error("There was an error while updating permissions for updated group:" + id
              + " node:" + n.getId(), e);
        }
      } else {
        log.info("The node was skipped from indexing:" + n.getId());
      }
    }
  }

  private void updateAllByDeletedGroup(String id) {
    log.debug("Update all visible by group:");
    List<String> allCedarIdsForGroup = null;
    try {
      allCedarIdsForGroup = groupPermissionSearchingService.findAllCedarIdsForGroup(id);
    } catch (CedarProcessingException e) {
      log.error("Error while retrieving all the affected documents for group:" + id);
      return;
    }

    for (String cid : allCedarIdsForGroup) {
      log.info("Need to update permissions for:" + cid);
      try {
        IndexedDocumentNode parentNode = nodeSearchingService.getDocumentByCedarId(cid);
        upsertOnePermissions(Upsert.UPDATE, cid, parentNode.getNodeType(), parentNode.buildDocumentId());
      } catch (CedarProcessingException e) {
        log.error("There was an error while updating permissions for updated group:" + id
            + " node:" + cid, e);
      }
    }
  }

  // Executors
  private void upsertOnePermissions(Upsert upsert, String id, CedarNodeType nodeType, IndexedDocumentId parent) {
    upsertOnePermissions(upsert, id, nodeType == CedarNodeType.FOLDER ? FolderOrResource.FOLDER : FolderOrResource
        .RESOURCE, parent);
  }

  private void upsertOnePermissions(Upsert upsert, String id, FolderOrResource folderOrResource, IndexedDocumentId
      parent) {
    log.debug("upsertOnePermissions:" + upsert.getValue() + ":" + folderOrResource + ":" + id);
    try {
      CedarNodeMaterializedPermissions perm = permissionSession.getNodeMaterializedPermission(id, folderOrResource);
      if (perm != null) {
        if (upsert == Upsert.INSERT) {
          userPermissionIndexingService.indexDocument(perm, parent);
          groupPermissionIndexingService.indexDocument(perm, parent);
        } else {
          userPermissionIndexingService.updateIndexedDocument(perm, parent);
          groupPermissionIndexingService.updateIndexedDocument(perm, parent);
        }
      } else {
        log.error("Permissions not found for " + folderOrResource + ":" + id);
      }
    } catch (Exception e) {
      log.error("Error while upserting permissions", e);
    }
  }
}
