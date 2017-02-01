package org.metadatacenter.server.search.util;

import org.metadatacenter.bridge.CedarDataServices;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.security.CedarAccessException;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.rest.context.CedarRequestContextFactory;
import org.metadatacenter.server.FolderServiceSession;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.neo4j.CypherQueryBuilder;
import org.metadatacenter.server.search.SearchPermissionQueueEvent;
import org.metadatacenter.server.security.model.auth.CedarNodePermissions;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.service.UserService;

public class SearchPermissionExecutorService {

  private final UserService userService;
  private FolderServiceSession folderSession;
  private PermissionServiceSession permissionSession;

  public SearchPermissionExecutorService(CedarConfig cedarConfig) {
    userService = CedarDataServices.getUserService();
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
        updateRecursiveFolder(event.getId());
        break;
      case FOLDER_DELETED:
        deleteRecursiveFolder(event.getId());
        break;
      case FOLDER_PERMISSION_CHANGED:
        updateRecursiveFolder(event.getId());
        break;
      case USER_CREATED:
        updateRecursiveByUser(event.getId());
        break;
      case GROUP_MEMBERS_UPDATED:
        updateRecursiveByUpdatedGroup(event.getId());
        break;
      case GROUP_DELETED:
        updateRecursiveByDeletedGroup(event.getId());
        break;
    }
  }


  private void createOneResource(String id) {
  }

  private void updateOneResource(String id) {
  }

  private void deleteOneResource(String id) {

  }

  private void createOneFolder(String id) {
    FolderServerFolder folder = folderSession.findFolderById(id);
    if (folder != null) {
      System.out.println("Create one folder:" + folder.getDisplayName());
      System.out.println("Get permissions");
      try {
        CedarNodePermissions flatPermissions = permissionSession.getNodeMaterializedPermission(id, CypherQueryBuilder
            .FolderOrResource.FOLDER);
        System.out.println("Group:" + flatPermissions.getGroupPermissions());
        System.out.println("User :" + flatPermissions.getUserPermissions());
      } catch (Exception e) {
        System.out.println("Got exception:" + e);
      }
      System.out.println("Got permissions");
    } else {
      System.out.println("Folder was not found:" + id);
    }
  }

  private void updateRecursiveFolder(String id) {
  }

  private void deleteRecursiveFolder(String id) {
  }

  private void updateRecursiveByUser(String id) {

  }

  private void updateRecursiveByUpdatedGroup(String id) {

  }

  private void updateRecursiveByDeletedGroup(String id) {

  }

}
