package org.metadatacenter.bridge;

import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.model.folderserver.extract.FolderServerFolderExtract;
import org.metadatacenter.model.folderserver.extract.FolderServerNodeExtract;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.server.FolderServiceSession;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.security.model.auth.CedarPermission;

import java.util.List;

public final class PathInfoBuilder {

  private PathInfoBuilder() {
  }

  public static List<FolderServerNodeExtract> getNodePathExtract(CedarRequestContext context,
                                                                 FolderServiceSession folderSession,
                                                                 PermissionServiceSession permissionSession,
                                                                 FolderServerNode node) {
    List<FolderServerNodeExtract> pathInfo = folderSession.findNodePathExtract(node);
    for (FolderServerNodeExtract extract : pathInfo) {
      extract.setActiveUserCanRead(activeUserCanRead(context, permissionSession, extract));
    }
    return pathInfo;
  }

  private static boolean activeUserCanRead(CedarRequestContext context, PermissionServiceSession permissionSession,
                                           FolderServerNodeExtract nodeExtract) {
    if (context.getCedarUser().has(CedarPermission.READ_NOT_READABLE_NODE)) {
      return true;
    }
    if (nodeExtract instanceof FolderServerFolderExtract) {
      FolderServerFolderExtract folderExtract = (FolderServerFolderExtract) nodeExtract;
      if (folderExtract.isRoot() || folderExtract.isSystem()) {
        return false;
      }
    }
    return permissionSession.userHasReadAccessToNode(nodeExtract.getId());
  }
}
