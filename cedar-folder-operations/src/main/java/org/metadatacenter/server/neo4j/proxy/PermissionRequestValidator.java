package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.result.BackendCallErrorType;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PermissionRequestValidator {

  private final CedarNodePermissionsRequest request;
  private final PermissionServiceSession permissionService;
  private final Neo4JProxies proxies;
  private final BackendCallResult callResult;
  private final CedarNodePermissions permissions;
  private final String nodeURL;
  private final boolean nodeIsFolder;

  private FolderServerNode node;

  public PermissionRequestValidator(PermissionServiceSession permissionService, Neo4JProxies proxies, String nodeURL,
                                    CedarNodePermissionsRequest request, boolean nodeIsFolder) {
    this.permissionService = permissionService;
    this.proxies = proxies;
    this.callResult = new BackendCallResult();
    this.request = request;
    this.nodeURL = nodeURL;
    this.nodeIsFolder = nodeIsFolder;
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
    if (nodeIsFolder) {
      FolderServerFolder folder = proxies.folder().findFolderById(nodeURL);
      node = folder;
      if (folder == null) {
        callResult.addError(BackendCallErrorType.NOT_FOUND)
            .subType("folderNotFound")
            .message("Folder not found by id")
            .param("folderId", nodeURL);
      }
    } else {
      FolderServerResource resource = proxies.resource().findResourceById(nodeURL);
      node = resource;
      if (resource == null) {
        callResult.addError(BackendCallErrorType.NOT_FOUND)
            .subType("resourceNotFound")
            .message("Resource not found by id")
            .param("resourceId", nodeURL);
      }
    }
  }

  private void validateWritePermission() {
    if (nodeIsFolder) {
      if (!permissionService.userHasWriteAccessToFolder(nodeURL)) {
        callResult.addError(BackendCallErrorType.AUTHORIZATION)
            .subType("userHasNoWriteAccess")
            .message("The current user has no write access to the folder")
            .param("folderId", nodeURL);
      }
    } else {
      if (!permissionService.userHasWriteAccessToResource(nodeURL)) {
        callResult.addError(BackendCallErrorType.AUTHORIZATION)
            .subType("userHasNoWriteAccess")
            .message("The current user has no write access to the resource")
            .param("resourceId", nodeURL);
      }
    }
  }

  private void validateAndSetOwner() {
    NodePermissionUser owner = request.getOwner();
    if (owner == null) {
      callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
          .subType("ownerMissing")
          .message("The owner should be present in the request");
    } else {
      String newOwnerId = owner.getId();
      FolderServerUser newOwner = proxies.user().findUserById(newOwnerId);
      if (newOwner == null) {
        callResult.addError(BackendCallErrorType.NOT_FOUND)
            .subType("userNotFound")
            .message("The new owner can not be found")
            .param("userId", newOwnerId);
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
        callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
            .subType("userNodeMissing")
            .message("The user node is missing from the request");
      } else {
        NodePermission permission = pair.getPermission();
        if (permission == null) {
          callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
              .subType("permissionMissing")
              .message("The permission is missing from the request");
        } else {
          String userURL = permissionUser.getId();
          FolderServerUser user = proxies.user().findUserById(userURL);
          if (user == null) {
            callResult.addError(BackendCallErrorType.NOT_FOUND)
                .subType("userNotFound")
                .message("The user from request can not be found")
                .param("userId", userURL);

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
        callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
            .subType("groupNodeMissing")
            .message("The group node is missing from the request");
      } else {
        NodePermission permission = pair.getPermission();
        if (permission == null) {
          callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
              .subType("permissionMissing")
              .message("The permission is missing from the request");
        } else {
          String groupURL = permissionGroup.getId();
          FolderServerGroup group = proxies.group().findGroupById(groupURL);
          if (group == null) {
            callResult.addError(BackendCallErrorType.NOT_FOUND)
                .subType("groupNotFound")
                .message("The group from request can not be found")
                .param("groupId", groupURL);
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
        callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
            .subType("userIdNotUnique")
            .message("Each user should be listed only once in the request")
            .param("userId", uid);
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
        callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
            .subType("groupIdNotUnique")
            .message("Each group should be listed only once in the request")
            .param("groupId", gid);
      } else {
        groupIds.add(gid);
      }
    }
  }

  private void validateOwnerAndUserCollision() {
    String ownerId = permissions.getOwner().getId();
    for (CedarNodeUserPermission up : permissions.getUserPermissions()) {
      if (ownerId.equals(up.getUser().getId())) {
        callResult.addError(BackendCallErrorType.INVALID_ARGUMENT)
            .subType("ownerAsUser")
            .message("The owner should not be listed among the user permissions")
            .param("userId", ownerId);
      }
    }
  }

  private void validateOwnerSetPermission() {
    String newOwnerId = permissions.getOwner().getId();
    CedarNodePermissions currentPermissions = permissionService.getNodePermissions(nodeURL, nodeIsFolder);
    String currentOwnerId = currentPermissions.getOwner().getId();
    if (!newOwnerId.equals(currentOwnerId)) {
      if (!permissionService.userIsOwnerOfNode(node)) {
        callResult.addError(BackendCallErrorType.AUTHORIZATION)
            .subType("userNotOwner")
            .message("Only the owner of a node can change the ownership")
            .param("nodeId", nodeURL);
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
