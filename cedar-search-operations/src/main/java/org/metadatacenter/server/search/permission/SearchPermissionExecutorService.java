package org.metadatacenter.server.search.permission;

import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
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
  private PermissionSearchService permissionSearchService;

  public SearchPermissionExecutorService(CedarConfig cedarConfig, PermissionSearchService permissionSearchService) {
    userService = CedarDataServices.getUserService();
    this.permissionSearchService = permissionSearchService;
    String adminUserUUID = cedarConfig.getKeycloakConfig().getAdminUser().getUuid();
    CedarUser adminUser = null;
    try {
      adminUser = userService.findUser(adminUserUUID);
    } catch (Exception ex) {
      // TODO: log this
      ex.printStackTrace();
    }
    if (adminUser != null) {
      try {
        CedarRequestContext cedarRequestContext = CedarRequestContextFactory.fromUser(adminUser);
        folderSession = CedarDataServices.getFolderServiceSession(cedarRequestContext);
        permissionSession = CedarDataServices.getPermissionServiceSession(cedarRequestContext);
      } catch (CedarAccessException ex) {
        // TODO: log this
        ex.printStackTrace();
      }
    }
  }

  // Main entry point
  public void handleEvent(SearchPermissionQueueEvent event) {
    switch (event.getEventType()) {
      case RESOURCE_CREATED:
        createOneResource(event.getId());
        break;
      case RESOURCE_COPIED:
        createOneResource(event.getId());
        break;
      case RESOURCE_MOVED:
        updateOneResource(event.getId());
        break;
      case RESOURCE_DELETED:
        deleteOneResource(event.getId());
        break;
      case RESOURCE_PERMISSION_CHANGED:
        updateOneResource(event.getId());
        break;
      case FOLDER_CREATED:
        createOneFolder(event.getId());
        break;
      case FOLDER_MOVED:
        updateFolderRecursively(event.getId());
        break;
      case FOLDER_DELETED:
        deleteRecursiveFolder(event.getId());
        break;
      case FOLDER_PERMISSION_CHANGED:
        updateFolderRecursively(event.getId());
        break;
      case USER_CREATED:
        updateAllByCreatedUser(event.getId());
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
  private void createOneResource(String id) {
    FolderServerResource resource = folderSession.findResourceById(id);
    if (resource != null) {
      log.debug("Create one resource:" + resource.getDisplayName());
      upsertOnePermissions(Upsert.INSERT, id, FolderOrResource.RESOURCE);
    } else {
      log.error("Resource was not found:" + id);
    }
  }

  private void updateOneResource(String id) {
    FolderServerResource resource = folderSession.findResourceById(id);
    if (resource != null) {
      log.debug("Update one resource:" + resource.getDisplayName());
      upsertOnePermissions(Upsert.UPDATE, id, FolderOrResource.RESOURCE);
    } else {
      log.error("Resource was not found:" + id);
    }
  }

  private void deleteOneResource(String id) {
    log.debug("Delete one resource:" + id);
    deleteOnePermissions(id);
  }

  private void createOneFolder(String id) {
    FolderServerFolder folder = folderSession.findFolderById(id);
    if (folder != null) {
      log.debug("Create one folder:" + folder.getDisplayName());
      upsertOnePermissions(Upsert.INSERT, id, FolderOrResource.FOLDER);
    } else {
      log.error("Folder was not found:" + id);
    }
  }

  private void updateFolderRecursively(String id) {
    log.debug("Update recursive folder:");
    List<FolderServerNode> subtree = folderSession.findAllDescendantNodesById(id);
    for (FolderServerNode n : subtree) {
      upsertOnePermissions(Upsert.UPDATE, n.getId(), n.getType());
    }
  }

  private void deleteRecursiveFolder(String id) {
    // TODO
    // Get all the orphan permission type records from Elasticsearch
    // Delete them all
  }

  private void updateAllByCreatedUser(String id) {
    log.debug("Update all visible by user:");
    List<FolderServerNode> collection = folderSession.findAllNodesVisibleByUserId(id);
    for (FolderServerNode n : collection) {
      upsertOnePermissions(Upsert.UPDATE, n.getId(), n.getType());
    }
  }

  private void updateAllByUpdatedGroup(String id) {
    log.debug("Update all visible by group:");
    List<FolderServerNode> collection = folderSession.findAllNodesVisibleByGroupId(id);
    for (FolderServerNode n : collection) {
      upsertOnePermissions(Upsert.UPDATE, n.getId(), n.getType());
    }
  }

  private void updateAllByDeletedGroup(String id) {
    // TODO
    // Get all the ones from Elasticsearch that have this id in their group list
    // Update them all
  }

  // Executors
  private void upsertOnePermissions(Upsert upsert, String id, CedarNodeType nodeType) {
    upsertOnePermissions(upsert, id, nodeType == CedarNodeType.FOLDER ? FolderOrResource.FOLDER : FolderOrResource
        .RESOURCE);
  }

  private void upsertOnePermissions(Upsert upsert, String id, FolderOrResource folderOrResource) {
    log.debug("upsertOnePermissions:" + upsert.getValue() + ":" + folderOrResource + ":" + id);
    try {
      CedarNodeMaterializedPermissions perm = permissionSession.getNodeMaterializedPermission(id, folderOrResource);
      /*log.debug("Groups:");
      for (String gid : perm.getGroupPermissions().keySet()) {
        log.debug("   " + gid + " : " + perm.getGroupPermissions().get(gid).getValue());
      }
      log.debug("Users:");
      for (String uid : perm.getUserPermissions().keySet()) {
        log.debug("   " + uid + " : " + perm.getUserPermissions().get(uid).getValue());
      }*/
      if (upsert == Upsert.INSERT) {
        permissionSearchService.indexResource(perm);
      } else {
        permissionSearchService.updateIndexedResource(perm);
      }
    } catch (Exception e) {
      log.error("Error while upserting permissions", e);
    }
  }

  private void deleteOnePermissions(String id) {
    log.debug("deleteOnePermissions:" + id);
    try {
      permissionSearchService.removeResourceFromIndex(id);
    } catch (Exception e) {
      log.error("Error while deleting permissions", e);
    }
  }

  //TODO: add this to reindex as well
  // Reindex can run into errors, fix that


}
