package org.metadatacenter.server;

import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.security.model.auth.CedarNodePermissions;
import org.metadatacenter.server.security.model.auth.CedarNodePermissionsRequest;
import org.metadatacenter.server.security.model.auth.CedarPermission;

public interface PermissionServiceSession {

  CedarNodePermissions getNodePermissions(String nodeURL, FolderOrResource folderOrResource);

  CedarNodeMaterializedPermissions getNodeMaterializedPermission(String nodeURL, FolderOrResource folderOrResource);

  BackendCallResult updateNodePermissions(String nodeURL, CedarNodePermissionsRequest request, FolderOrResource
      folderOrResource);

  boolean userCanChangeOwnerOfNode(String nodeURL);

  boolean userHasReadAccessToNode(String nodeURL);

  boolean userHasWriteAccessToNode(String nodeURL);

  boolean userIsOwnerOfNode(FolderServerNode node);

  boolean userHas(CedarPermission permission);
}
