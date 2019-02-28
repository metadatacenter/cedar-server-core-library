package org.metadatacenter.bridge;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.exception.CedarObjectNotFoundException;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.basic.FolderServerResource;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerFolderCurrentUserReport;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerNodeCurrentUserReport;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerResourceCurrentUserReport;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;
import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.FolderServiceSession;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.VersionServiceSession;
import org.metadatacenter.server.permissions.CurrentUserPermissionUpdaterForGraphDbFolder;
import org.metadatacenter.server.permissions.CurrentUserPermissionUpdaterForGraphDbResource;
import org.metadatacenter.server.security.model.auth.FolderWithCurrentUserPermissions;
import org.metadatacenter.server.security.model.auth.ResourceWithCurrentUserPermissions;

import java.util.List;

public class GraphDbPermissionReader {

  private GraphDbPermissionReader() {
  }

  public static FolderServerResourceCurrentUserReport getResourceCurrentUserReport(CedarRequestContext context,
                                                                                   FolderServiceSession folderSession,
                                                                                   PermissionServiceSession permissionSession,
                                                                                   CedarConfig cedarConfig,
                                                                                   String resourceId)
      throws CedarException {
    if (resourceId != null) {
      FolderServerResource resource = folderSession.findResourceById(resourceId);
      if (resource == null) {
        throw new CedarObjectNotFoundException("The resource can not be found by id")
            .errorKey(CedarErrorKey.RESOURCE_NOT_FOUND)
            .parameter("id", resourceId);
      }

      folderSession.addPathAndParentId(resource);

      List<FolderServerNodeExtract> pathInfo = folderSession.findNodePathExtract(resource);
      resource.setPathInfo(pathInfo);

      FolderServerResourceCurrentUserReport resourceReport =
          (FolderServerResourceCurrentUserReport) FolderServerNodeCurrentUserReport.fromNode(resource);

      decorateResourceWithCurrentUserPermissions(context, permissionSession, cedarConfig, resourceReport);

      return resourceReport;
    }
    return null;
  }

  public static void decorateResourceWithCurrentUserPermissions(CedarRequestContext c,
                                                                PermissionServiceSession permissionSession,
                                                                CedarConfig cedarConfig,
                                                                ResourceWithCurrentUserPermissions resource) {
    VersionServiceSession versionSession = CedarDataServices.getVersionServiceSession(c);
    CurrentUserPermissionUpdater cupu = CurrentUserPermissionUpdaterForGraphDbResource.get(permissionSession,
        versionSession, cedarConfig, resource);
    cupu.update(resource.getCurrentUserPermissions());
  }

  public static FolderServerFolderCurrentUserReport getFolderCurrentUserReport(CedarRequestContext context,
                                                                               FolderServiceSession folderSession,
                                                                               PermissionServiceSession permissionSession,
                                                                               String folderId) throws CedarException {
    if (folderId != null) {

      FolderServerFolder folder = folderSession.findFolderById(folderId);
      if (folder == null) {
        throw new CedarObjectNotFoundException("The folder can not be found by id")
            .errorKey(CedarErrorKey.FOLDER_NOT_FOUND)
            .parameter("id", folderId);
      }

      folderSession.addPathAndParentId(folder);

      List<FolderServerNodeExtract> pathInfo = folderSession.findNodePathExtract(folder);
      folder.setPathInfo(pathInfo);

      FolderServerFolderCurrentUserReport folderReport =
          (FolderServerFolderCurrentUserReport) FolderServerNodeCurrentUserReport.fromNode(folder);

      decorateFolderWithCurrentUserPermissions(context, permissionSession, folderReport);

      return folderReport;
    }
    return null;
  }

  private static void decorateFolderWithCurrentUserPermissions(CedarRequestContext c,
                                                               PermissionServiceSession permissionSession,
                                                               FolderWithCurrentUserPermissions folder) {
    CurrentUserPermissionUpdater cupu = CurrentUserPermissionUpdaterForGraphDbFolder.get(permissionSession, folder);
    cupu.update(folder.getCurrentUserPermissions());
  }

}
