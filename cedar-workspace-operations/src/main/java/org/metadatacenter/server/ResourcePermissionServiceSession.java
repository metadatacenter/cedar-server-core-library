package org.metadatacenter.server;

import org.metadatacenter.id.CedarFilesystemResourceId;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.CedarNodeMaterializedPermissions;
import org.metadatacenter.server.security.model.auth.CedarNodePermissions;
import org.metadatacenter.server.security.model.auth.CedarPermission;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionsRequest;

public interface ResourcePermissionServiceSession {

  CedarNodePermissions getResourcePermissions(CedarFilesystemResourceId resourceId);

  CedarNodeMaterializedPermissions getResourceMaterializedPermission(CedarFilesystemResourceId resourceId);

  BackendCallResult updateResourcePermissions(CedarFilesystemResourceId resourceId, ResourcePermissionsRequest request);

  boolean userCanChangeOwnerOfResource(CedarFilesystemResourceId resourceId);

  boolean userHasReadAccessToResource(CedarFilesystemResourceId resourceId);

  boolean userHasWriteAccessToResource(CedarFilesystemResourceId resourceId);

  boolean userIsOwnerOfResource(CedarFilesystemResourceId resource);

  boolean userHasPermission(CedarPermission permission);
}
