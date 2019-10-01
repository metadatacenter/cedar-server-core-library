package org.metadatacenter.bridge;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.exception.CedarObjectNotFoundException;
import org.metadatacenter.id.CedarCategoryId;
import org.metadatacenter.model.folderserver.basic.FolderServerArtifact;
import org.metadatacenter.model.folderserver.basic.FolderServerCategory;
import org.metadatacenter.model.folderserver.basic.FolderServerFolder;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerArtifactCurrentUserReport;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerCategoryCurrentUserReport;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerFolderCurrentUserReport;
import org.metadatacenter.model.folderserver.currentuserpermissions.FolderServerResourceCurrentUserReport;
import org.metadatacenter.permission.currentuserpermission.CurrentUserCategoryPermissionUpdater;
import org.metadatacenter.permission.currentuserpermission.CurrentUserPermissionUpdater;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.*;
import org.metadatacenter.server.permissions.CurrentUserPermissionUpdaterForGraphDbCategory;
import org.metadatacenter.server.permissions.CurrentUserPermissionUpdaterForGraphDbFolder;
import org.metadatacenter.server.permissions.CurrentUserPermissionUpdaterForGraphDbResource;
import org.metadatacenter.server.security.model.auth.FolderWithCurrentUserPermissions;
import org.metadatacenter.server.security.model.permission.resource.ResourceWithCurrentUserPermissions;

public class GraphDbPermissionReader {

  private GraphDbPermissionReader() {
  }

  public static FolderServerArtifactCurrentUserReport getArtifactCurrentUserReport(CedarRequestContext context,
                                                                                   FolderServiceSession folderSession,
                                                                                   ResourcePermissionServiceSession permissionSession,
                                                                                   CedarConfig cedarConfig,
                                                                                   String artifactId)
      throws CedarException {
    if (artifactId != null) {
      FolderServerArtifact artifact = folderSession.findArtifactById(artifactId);
      if (artifact == null) {
        throw new CedarObjectNotFoundException("The artifact can not be found by id")
            .errorKey(CedarErrorKey.ARTIFACT_NOT_FOUND)
            .parameter("id", artifactId);
      }

      folderSession.addPathAndParentId(artifact);

      artifact.setPathInfo(PathInfoBuilder.getResourcePathExtract(context, folderSession, permissionSession, artifact));

      FolderServerArtifactCurrentUserReport artifactReport =
          (FolderServerArtifactCurrentUserReport) FolderServerResourceCurrentUserReport.fromResource(artifact);

      decorateResourceWithCurrentUserPermissions(context, permissionSession, cedarConfig, artifactReport);

      return artifactReport;
    }
    return null;
  }

  public static void decorateResourceWithCurrentUserPermissions(CedarRequestContext c,
                                                                ResourcePermissionServiceSession permissionSession,
                                                                CedarConfig cedarConfig,
                                                                ResourceWithCurrentUserPermissions resource) {
    VersionServiceSession versionSession = CedarDataServices.getVersionServiceSession(c);
    CurrentUserPermissionUpdater cupu = CurrentUserPermissionUpdaterForGraphDbResource.get(permissionSession,
        versionSession, cedarConfig, resource);
    cupu.update(resource.getCurrentUserPermissions());
  }

  public static FolderServerFolderCurrentUserReport getFolderCurrentUserReport(CedarRequestContext context,
                                                                               FolderServiceSession folderSession,
                                                                               ResourcePermissionServiceSession permissionSession,
                                                                               String folderId) throws CedarException {
    if (folderId != null) {

      FolderServerFolder folder = folderSession.findFolderById(folderId);
      if (folder == null) {
        throw new CedarObjectNotFoundException("The folder can not be found by id")
            .errorKey(CedarErrorKey.FOLDER_NOT_FOUND)
            .parameter("id", folderId);
      }

      folderSession.addPathAndParentId(folder);

      folder.setPathInfo(PathInfoBuilder.getResourcePathExtract(context, folderSession, permissionSession, folder));

      FolderServerFolderCurrentUserReport folderReport =
          (FolderServerFolderCurrentUserReport) FolderServerResourceCurrentUserReport.fromResource(folder);

      decorateFolderWithCurrentUserPermissions(context, permissionSession, folderReport);

      return folderReport;
    }
    return null;
  }

  private static void decorateFolderWithCurrentUserPermissions(CedarRequestContext c,
                                                               ResourcePermissionServiceSession permissionSession,
                                                               FolderWithCurrentUserPermissions folder) {
    CurrentUserPermissionUpdater cupu = CurrentUserPermissionUpdaterForGraphDbFolder.get(permissionSession, folder);
    cupu.update(folder.getCurrentUserPermissions());
  }

  public static FolderServerCategoryCurrentUserReport getCategoryCurrentUserReport(CedarRequestContext context,
                                                                                   CategoryServiceSession categorySession,
                                                                                   CategoryPermissionServiceSession categoryPermissionSession,
                                                                                   CedarCategoryId categoryId) throws CedarException {
    if (categoryId != null) {

      FolderServerCategory category = categorySession.getCategoryById(categoryId);
      if (category == null) {
        throw new CedarObjectNotFoundException("The category can not be found by id")
            .errorKey(CedarErrorKey.CATEGORY_NOT_FOUND)
            .parameter("id", categoryId);
      }

      FolderServerCategoryCurrentUserReport categoryReport =
          FolderServerCategoryCurrentUserReport.fromCategory(category);

      decorateCategoryWithCurrentUserPermissions(context, categoryPermissionSession, categoryReport);

      return categoryReport;
    }
    return null;
  }

  private static void decorateCategoryWithCurrentUserPermissions(CedarRequestContext context,
                                                                 CategoryPermissionServiceSession categoryPermissionSession,
                                                                 FolderServerCategoryCurrentUserReport categoryReport) {
    CurrentUserCategoryPermissionUpdater cupu =
        CurrentUserPermissionUpdaterForGraphDbCategory.get(categoryPermissionSession,
        categoryReport);
    cupu.update(categoryReport.getCurrentUserPermissions());
  }

}
