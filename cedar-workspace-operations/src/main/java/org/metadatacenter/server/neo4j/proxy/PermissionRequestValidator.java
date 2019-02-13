package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.error.CedarErrorKey;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.model.folderserver.basic.*;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.metadatacenter.error.CedarErrorType.*;

public class PermissionRequestValidator {

  private final CedarNodePermissionsRequest request;
  private final PermissionServiceSession permissionService;
  private final Neo4JProxies proxies;
  private final BackendCallResult callResult;
  private final CedarNodePermissions permissions;
  private final String nodeURL;
  private final FolderOrResource folderOrResource;

  private FolderServerNode node;

  public PermissionRequestValidator(PermissionServiceSession permissionService, Neo4JProxies proxies, String nodeURL,
                                    CedarNodePermissionsRequest request, FolderOrResource folderOrResource) {
    this.permissionService = permissionService;
    this.proxies = proxies;
    this.callResult = new BackendCallResult();
    this.request = request;
    this.nodeURL = nodeURL;
    this.folderOrResource = folderOrResource;
    this.permissions = new CedarNodePermissions();

    validateNodeExistence();

    if (callResult.isOk()) {
      validateWritePermission();
    }
    if (callResult.isOk()) {
      validateAndSetOwner();
    }
    if (callResult.isOk()) {
      validateAndSetUsers();
    }
    if (callResult.isOk()) {
      validateAndSetGroups();
    }
    if (callResult.isOk()) {
      validateUserUniqueness();
    }
    if (callResult.isOk()) {
      validateGroupUniqueness();
    }
    if (callResult.isOk()) {
      validateOwnerAndUserCollision();
    }
    if (callResult.isOk()) {
      validateOwnerSetPermission();
    }
  }

  private void validateNodeExistence() {
    if (folderOrResource == FolderOrResource.FOLDER) {
      FolderServerFolder folder = proxies.folder().findFolderById(nodeURL);
      node = folder;
      if (folder == null) {
        callResult.addError(NOT_FOUND)
            .errorKey(CedarErrorKey.FOLDER_NOT_FOUND)
            .message("Folder not found by id")
            .parameter("folderId", nodeURL);
      }
    } else {
      FolderServerResource resource = proxies.resource().findResourceById(nodeURL);
      node = resource;
      if (resource == null) {
        callResult.addError(NOT_FOUND)
            .errorKey(CedarErrorKey.RESOURCE_NOT_FOUND)
            .message("Resource not found by id")
            .parameter("resourceId", nodeURL);
      }
    }
  }

  private void validateWritePermission() {
    if (!permissionService.userHasWriteAccessToNode(nodeURL)) {
      callResult.addError(AUTHORIZATION)
          .errorKey(CedarErrorKey.NO_WRITE_ACCESS_TO_NODE)
          .message("The current user has no write access to the node")
          .parameter("nodeId", nodeURL);
    }
  }

  private void validateAndSetOwner() {
    NodePermissionUser owner = request.getOwner();
    if (owner == null) {
      callResult.addError(INVALID_ARGUMENT)
          .errorKey(CedarErrorKey.MISSING_PARAMETER)
          .parameter("paramName", "owner")
          .message("The owner should be present in the request");
    } else {
      String newOwnerId = owner.getId();
      FolderServerUser newOwner = proxies.user().findUserById(newOwnerId);
      if (newOwner == null) {
        callResult.addError(NOT_FOUND)
            .errorKey(CedarErrorKey.USER_NOT_FOUND)
            .message("The new owner can not be found")
            .parameter("userId", newOwnerId);
      } else {
        permissions.setOwner(newOwner.buildExtract());
      }
    }
  }

  private void validateAndSetUsers() {
    List<NodePermissionUserPermissionPair> userPermissions = request.getUserPermissions();
    for (NodePermissionUserPermissionPair pair : userPermissions) {
      NodePermissionUser permissionUser = pair.getUser();
      if (permissionUser == null) {
        callResult.addError(INVALID_ARGUMENT)
            .errorKey(CedarErrorKey.MISSING_PARAMETER)
            .parameter("paramName", "user")
            .message("The user node is missing from the request");
      } else {
        NodePermission permission = pair.getPermission();
        if (permission == null) {
          callResult.addError(INVALID_ARGUMENT)
              .errorKey(CedarErrorKey.MISSING_PARAMETER)
              .parameter("paramName", "permission")
              .message("The permission is missing from the request");
        } else {
          String userURL = permissionUser.getId();
          FolderServerUser user = proxies.user().findUserById(userURL);
          if (user == null) {
            callResult.addError(NOT_FOUND)
                .errorKey(CedarErrorKey.USER_NOT_FOUND)
                .message("The user from request can not be found")
                .parameter("userId", userURL);

          } else {
            permissions.addUserPermissions(new CedarNodeUserPermission(user.buildExtract(), permission));
          }
        }
      }
    }
  }

  private void validateAndSetGroups() {
    List<NodePermissionGroupPermissionPair> groupPermissions = request.getGroupPermissions();
    for (NodePermissionGroupPermissionPair pair : groupPermissions) {
      NodePermissionGroup permissionGroup = pair.getGroup();
      if (permissionGroup == null) {
        callResult.addError(INVALID_ARGUMENT)
            .errorKey(CedarErrorKey.MISSING_PARAMETER)
            .parameter("paramName", "group")
            .message("The group node is missing from the request");
      } else {
        NodePermission permission = pair.getPermission();
        if (permission == null) {
          callResult.addError(INVALID_ARGUMENT)
              .errorKey(CedarErrorKey.MISSING_PARAMETER)
              .parameter("paramName", "permission")
              .message("The permission is missing from the request");
        } else {
          String groupURL = permissionGroup.getId();
          FolderServerGroup group = proxies.group().findGroupById(groupURL);
          if (group == null) {
            callResult.addError(NOT_FOUND)
                .errorKey(CedarErrorKey.GROUP_NOT_FOUND)
                .message("The group from request can not be found")
                .parameter("groupId", groupURL);
          } else {
            permissions.addGroupPermissions(new CedarNodeGroupPermission(group.buildExtract(), permission));
          }
        }
      }
    }
  }

  private void validateUserUniqueness() {
    Set<String> userIds = new HashSet<>();
    for (CedarNodeUserPermission up : permissions.getUserPermissions()) {
      String uid = up.getUser().getId();
      if (userIds.contains(uid)) {
        callResult.addError(INVALID_ARGUMENT)
            .errorKey(CedarErrorKey.UNIQUE_CONSTRAINT_COLLISION)
            .message("Each user should be listed only once in the request")
            .parameter("propertyName", "userId")
            .parameter("userId", uid);
      } else {
        userIds.add(uid);
      }
    }
  }

  private void validateGroupUniqueness() {
    Set<String> groupIds = new HashSet<>();
    for (CedarNodeGroupPermission gp : permissions.getGroupPermissions()) {
      String gid = gp.getGroup().getId();
      if (groupIds.contains(gid)) {
        callResult.addError(INVALID_ARGUMENT)
            .errorKey(CedarErrorKey.UNIQUE_CONSTRAINT_COLLISION)
            .message("Each group should be listed only once in the request")
            .parameter("propertyName", "groupId")
            .parameter("groupId", gid);
      } else {
        groupIds.add(gid);
      }
    }
  }

  private void validateOwnerAndUserCollision() {
    String ownerId = permissions.getOwner().getId();
    for (CedarNodeUserPermission up : permissions.getUserPermissions()) {
      if (ownerId.equals(up.getUser().getId())) {
        callResult.addError(INVALID_ARGUMENT)
            .errorKey(CedarErrorKey.INVALID_DATA)
            .message("The owner should not be listed among the user permissions")
            .parameter("userId", ownerId);
      }
    }
  }

  private void validateOwnerSetPermission() {
    String newOwnerId = permissions.getOwner().getId();
    CedarNodePermissions currentPermissions = permissionService.getNodePermissions(nodeURL, folderOrResource);
    String currentOwnerId = currentPermissions.getOwner().getId();
    if (!newOwnerId.equals(currentOwnerId)) {
      // if it has the role, we do not check
      if (permissionService.userHas(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE)) {
        return;
      }
      if (!permissionService.userIsOwnerOfNode(node)) {
        callResult.addError(AUTHORIZATION)
            .errorKey(CedarErrorKey.NOT_AUTHORIZED)
            .message("Only the owner of a node can change the ownership")
            .parameter("nodeId", nodeURL);
      }
    }
  }

  public BackendCallResult getCallResult() {
    return callResult;
  }

  public CedarNodePermissions getPermissions() {
    return permissions;
  }
}
