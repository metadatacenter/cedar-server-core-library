package org.metadatacenter.bridge;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.exception.CedarException;
import org.metadatacenter.exception.CedarObjectNotFoundException;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.WorkspaceObjectBuilder;
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
import org.metadatacenter.server.permissions.CurrentUserPermissionUpdaterForWorkspaceFolder;
import org.metadatacenter.server.permissions.CurrentUserPermissionUpdaterForWorkspaceResource;
import org.metadatacenter.server.security.model.auth.FolderWithCurrentUserPermissions;
import org.metadatacenter.server.security.model.auth.ResourceWithCurrentUserPermissions;
import org.metadatacenter.util.http.CedarUrlUtil;
import org.metadatacenter.util.http.ProxyUtil;
import org.metadatacenter.util.json.JsonMapper;

import java.util.List;

public class FolderServerProxy {

  private FolderServerProxy() {
  }

  public static FolderServerResource getResource(String folderBaseResource, String resourceId, CedarRequestContext
      context) throws CedarProcessingException {
    if (resourceId != null) {
      try {
        String url = folderBaseResource + "/" + CedarUrlUtil.urlEncode(resourceId);
        HttpResponse proxyResponse = ProxyUtil.proxyGet(url, context);
        int statusCode = proxyResponse.getStatusLine().getStatusCode();
        HttpEntity entity = proxyResponse.getEntity();
        if (entity != null) {
          if (HttpStatus.SC_OK == statusCode) {
            FolderServerResource node = WorkspaceObjectBuilder.artifact(proxyResponse.getEntity().getContent());
            return node;
          }
        }
      } catch (Exception e) {
        throw new CedarProcessingException(e);
      }
    }
    return null;
  }

  public static FolderServerResourceCurrentUserReport getResourceCurrentUserReport(CedarRequestContext context,
                                                                                   CedarConfig cedarConfig,
                                                                                   String resourceId)
      throws CedarException {
    if (resourceId != null) {
      FolderServiceSession folderSession = CedarDataServices.getFolderServiceSession(context);

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

      decorateResourceWithCurrentUserPermissions(context, cedarConfig, resourceReport);

      return resourceReport;
    }
    return null;
  }

  public static void decorateResourceWithCurrentUserPermissions(CedarRequestContext c,
                                                                CedarConfig cedarConfig,
                                                                ResourceWithCurrentUserPermissions resource) {
    PermissionServiceSession permissionSession = CedarDataServices.getPermissionServiceSession(c);
    VersionServiceSession versionSession = CedarDataServices.getVersionServiceSession(c);
    CurrentUserPermissionUpdater cupu = CurrentUserPermissionUpdaterForWorkspaceResource.get(permissionSession,
        versionSession, cedarConfig, resource);
    cupu.update(resource.getCurrentUserPermissions());
  }

  public static FolderServerFolder getFolder(String folderBaseFolders, String folderId, CedarRequestContext context)
      throws CedarProcessingException {
    if (folderId != null) {
      try {
        String url = folderBaseFolders + "/" + CedarUrlUtil.urlEncode(folderId);
        HttpResponse proxyResponse = ProxyUtil.proxyGet(url, context);
        int statusCode = proxyResponse.getStatusLine().getStatusCode();
        HttpEntity entity = proxyResponse.getEntity();
        if (entity != null) {
          if (HttpStatus.SC_OK == statusCode) {
            FolderServerFolder folder = null;
            String responseString = EntityUtils.toString(proxyResponse.getEntity());
            folder = JsonMapper.MAPPER.readValue(responseString, FolderServerFolder.class);
            return folder;
          }
        }
      } catch (Exception e) {
        throw new CedarProcessingException(e);
      }
    }
    return null;
  }

  public static FolderServerFolderCurrentUserReport getFolderCurrentUserReport(CedarRequestContext context,
                                                                               String folderId) throws CedarException {
    if (folderId != null) {

      FolderServiceSession folderSession = CedarDataServices.getFolderServiceSession(context);

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

      decorateFolderWithCurrentUserPermissions(context, folderReport);

      return folderReport;
    }
    return null;
  }

  public static void decorateFolderWithCurrentUserPermissions(CedarRequestContext c,
                                                              FolderWithCurrentUserPermissions folder) {
    PermissionServiceSession permissionSession = CedarDataServices.getPermissionServiceSession(c);
    CurrentUserPermissionUpdater cupu = CurrentUserPermissionUpdaterForWorkspaceFolder.get(permissionSession, folder);
    cupu.update(folder.getCurrentUserPermissions());
  }

}
