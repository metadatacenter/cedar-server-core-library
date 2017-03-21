package org.metadatacenter.server;

import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.security.model.auth.CedarNodePermissions;
import org.metadatacenter.server.security.model.auth.CedarNodePermissionsRequest;

import java.util.Map;

public interface PermissionServiceSession {

  CedarNodePermissions getNodePermissions(String nodeURL, FolderOrResource folderOrResource);

  CedarNodeMaterializedPermissions getNodeMaterializedPermission(String nodeURL, FolderOrResource folderOrResource);

  BackendCallResult updateNodePermissions(String nodeURL, CedarNodePermissionsRequest request, FolderOrResource
      folderOrResource);

  boolean userIsOwnerOfFolder(String folderURL);

  boolean userHasReadAccessToFolder(String folderURL);

  boolean userHasWriteAccessToFolder(String folderURL);

  boolean userIsOwnerOfResource(String resourceURL);

  boolean userHasReadAccessToResource(String resourceURL);

  boolean userHasWriteAccessToResource(String resourceURL);

  boolean userIsOwnerOfNode(FolderServerNode node);

  Map<String, String> findAccessibleNodeIds();
}
