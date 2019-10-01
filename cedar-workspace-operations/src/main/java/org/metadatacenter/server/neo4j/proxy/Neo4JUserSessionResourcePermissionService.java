package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.id.CedarCategoryId;
import org.metadatacenter.model.folderserver.basic.FileSystemResource;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.model.folderserver.datagroup.ResourceWithEverybodyPermission;
import org.metadatacenter.server.ResourcePermissionServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.neo4j.Neo4JFieldValues;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.*;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionsRequest;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermission;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionGroupPermissionPair;
import org.metadatacenter.server.security.model.permission.resource.ResourcePermissionUserPermissionPair;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Neo4JUserSessionResourcePermissionService extends AbstractNeo4JUserSession implements ResourcePermissionServiceSession {

  private Neo4JUserSessionResourcePermissionService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu,
                                                    String globalRequestId, String localRequestId) {
    super(cedarConfig, proxies, cu, globalRequestId, localRequestId);
  }

  public static ResourcePermissionServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser,
                                                     String globalRequestId, String localRequestId) {
    return new Neo4JUserSessionResourcePermissionService(cedarConfig, proxies, cedarUser, globalRequestId, localRequestId);
  }

  @Override
  public CedarNodePermissions getNodePermissions(String nodeURL) {
    FileSystemResource node = proxies.resource().findNodeById(nodeURL);
    if (node != null) {
      FolderServerUser owner = getNodeOwner(nodeURL);
      List<FolderServerUser> readUsers = getUsersWithDirectPermission(nodeURL, ResourcePermission.READ);
      List<FolderServerUser> writeUsers = getUsersWithDirectPermission(nodeURL, ResourcePermission.WRITE);
      List<FolderServerGroup> readGroups = getGroupsWithDirectPermission(nodeURL, ResourcePermission.READ);
      List<FolderServerGroup> writeGroups = getGroupsWithDirectPermission(nodeURL, ResourcePermission.WRITE);
      return buildPermissions(owner, readUsers, writeUsers, readGroups, writeGroups);
    } else {
      return null;
    }
  }

  private List<FolderServerUser> getUsersWithDirectPermission(String nodeURL, ResourcePermission permission) {
    return proxies.permission().getUsersWithDirectPermissionOnNode(nodeURL, permission);
  }

  private List<FolderServerGroup> getGroupsWithDirectPermission(String nodeURL, ResourcePermission permission) {
    return proxies.permission().getGroupsWithDirectPermissionOnNode(nodeURL, permission);
  }

  @Override
  public BackendCallResult updateNodePermissions(String nodeURL, ResourcePermissionsRequest request) {

    ResourcePermissionRequestValidator prv = new ResourcePermissionRequestValidator(this, proxies, nodeURL, request);
    BackendCallResult bcr = prv.getCallResult();
    if (bcr.isError()) {
      return bcr;
    } else {
      CedarNodePermissions currentPermissions = getNodePermissions(nodeURL);
      CedarNodePermissions newPermissions = prv.getPermissions();

      String oldOwnerId = currentPermissions.getOwner().getId();
      String newOwnerId = newPermissions.getOwner().getId();
      if (oldOwnerId != null && !oldOwnerId.equals(newOwnerId)) {
        Neo4JUserSessionGroupOperations.updateNodeOwner(proxies.resource(), nodeURL, newOwnerId);
      }

      Set<ResourcePermissionUserPermissionPair> oldUserPermissions = new HashSet<>();
      for (CedarNodeUserPermission up : currentPermissions.getUserPermissions()) {
        oldUserPermissions.add(up.getAsUserIdPermissionPair());
      }
      Set<ResourcePermissionUserPermissionPair> newUserPermissions = new HashSet<>();
      for (CedarNodeUserPermission up : newPermissions.getUserPermissions()) {
        newUserPermissions.add(up.getAsUserIdPermissionPair());
      }

      Set<ResourcePermissionUserPermissionPair> toRemoveUserPermissions = new HashSet<>(oldUserPermissions);
      toRemoveUserPermissions.removeAll(newUserPermissions);
      if (!toRemoveUserPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.removeUserPermissions(proxies.permission(), nodeURL, toRemoveUserPermissions);
      }

      Set<ResourcePermissionUserPermissionPair> toAddUserPermissions = new HashSet<>(newUserPermissions);
      toAddUserPermissions.removeAll(oldUserPermissions);
      if (!toAddUserPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.addUserPermissions(proxies.permission(), nodeURL, toAddUserPermissions);
      }

      Set<ResourcePermissionGroupPermissionPair> oldGroupPermissions = new HashSet<>();
      for (CedarNodeGroupPermission gp : currentPermissions.getGroupPermissions()) {
        oldGroupPermissions.add(gp.getAsGroupIdPermissionPair());
      }
      Set<ResourcePermissionGroupPermissionPair> newGroupPermissions = new HashSet<>();
      for (CedarNodeGroupPermission gp : newPermissions.getGroupPermissions()) {
        newGroupPermissions.add(gp.getAsGroupIdPermissionPair());
      }

      Set<ResourcePermissionGroupPermissionPair> toRemoveGroupPermissions = new HashSet<>(oldGroupPermissions);
      toRemoveGroupPermissions.removeAll(newGroupPermissions);
      if (!toRemoveGroupPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.removeGroupPermissions(proxies.permission(), nodeURL, toRemoveGroupPermissions);
      }

      Set<ResourcePermissionGroupPermissionPair> toAddGroupPermissions = new HashSet<>(newGroupPermissions);
      toAddGroupPermissions.removeAll(oldGroupPermissions);
      if (!toAddGroupPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.addGroupPermissions(proxies.permission(), nodeURL, toAddGroupPermissions);
      }

      ResourceWithEverybodyPermission node = proxies.resource().findNodeById(nodeURL);
      if (node != null) {
        FolderServerGroup everybody = proxies.group().findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
        NodeSharePermission setEverybodyPermission = null;
        for (ResourcePermissionGroupPermissionPair groupPermission : newGroupPermissions) {
          if (groupPermission.getGroup().getId().equals(everybody.getId())) {
            NodeSharePermission everybodyPermissionCandidate = NodeSharePermission.fromGroupPermission(groupPermission);
            if (everybodyPermissionCandidate != node.getEverybodyPermission()) {
              setEverybodyPermission = everybodyPermissionCandidate;
            }
          }
        }
        if (setEverybodyPermission == null && node.getEverybodyPermission() != NodeSharePermission.NONE) {
          setEverybodyPermission = NodeSharePermission.NONE;
        }

        if (setEverybodyPermission != null) {
          proxies.resource().setEverybodyPermission(nodeURL, setEverybodyPermission);
        }
      }

      return new BackendCallResult();
    }
  }

  @Override
  public boolean userCanChangeOwnerOfNode(String nodeURL) {
    if (cu.has(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE)) {
      return true;
    } else {
      FolderServerUser owner = getNodeOwner(nodeURL);
      return owner != null && owner.getId().equals(cu.getId());
    }
  }

  @Override
  public boolean userHasReadAccessToNode(String nodeURL) {
    if (cu.has(CedarPermission.READ_NOT_READABLE_NODE)) {
      return true;
    } else {
      return proxies.permission().userHasReadAccessToNode(cu.getId(), nodeURL)
          || proxies.permission().userHasWriteAccessToNode(cu.getId(), nodeURL);
    }
  }

  @Override
  public boolean userHasWriteAccessToNode(String nodeURL) {
    if (cu.has(CedarPermission.WRITE_NOT_WRITABLE_NODE)) {
      return true;
    } else {
      return proxies.permission().userHasWriteAccessToNode(cu.getId(), nodeURL);
    }
  }

  @Override
  public boolean userIsOwnerOfNode(FileSystemResource node) {
    FolderServerUser owner = getNodeOwner(node.getId());
    return owner != null && owner.getId().equals(cu.getId());
  }

  private CedarNodePermissions buildPermissions(FolderServerUser owner, List<FolderServerUser> readUsers,
                                                List<FolderServerUser> writeUsers, List<FolderServerGroup>
                                                    readGroups, List<FolderServerGroup> writeGroups) {
    CedarNodePermissions permissions = new CedarNodePermissions();
    CedarUserExtract o = owner.buildExtract();
    permissions.setOwner(o);
    if (readUsers != null) {
      for (FolderServerUser user : readUsers) {
        CedarUserExtract u = user.buildExtract();
        CedarNodeUserPermission up = new CedarNodeUserPermission(u, ResourcePermission.READ);
        permissions.addUserPermissions(up);
      }
    }
    if (writeUsers != null) {
      for (FolderServerUser user : writeUsers) {
        CedarUserExtract u = user.buildExtract();
        CedarNodeUserPermission up = new CedarNodeUserPermission(u, ResourcePermission.WRITE);
        permissions.addUserPermissions(up);
      }
    }
    if (readGroups != null) {
      for (FolderServerGroup group : readGroups) {
        CedarGroupExtract g = group.buildExtract();
        CedarNodeGroupPermission gp = new CedarNodeGroupPermission(g, ResourcePermission.READ);
        permissions.addGroupPermissions(gp);
      }
    }
    if (writeGroups != null) {
      for (FolderServerGroup group : writeGroups) {
        CedarGroupExtract g = group.buildExtract();
        CedarNodeGroupPermission gp = new CedarNodeGroupPermission(g, ResourcePermission.WRITE);
        permissions.addGroupPermissions(gp);
      }
    }
    return permissions;
  }

  @Override
  public CedarNodeMaterializedPermissions getNodeMaterializedPermission(String nodeURL) {
    FileSystemResource node = proxies.resource().findNodeById(nodeURL);
    if (node != null) {
      NodeSharePermission everybodyPermission = node.getEverybodyPermission();
      if (everybodyPermission == null) {
        everybodyPermission = NodeSharePermission.NONE;
      }

      List<FolderServerUser> readUsers = new ArrayList<>();
      List<FolderServerUser> writeUsers = new ArrayList<>();
      List<FolderServerGroup> readGroups = new ArrayList<>();
      List<FolderServerGroup> writeGroups = new ArrayList<>();

      if (everybodyPermission == NodeSharePermission.WRITE) {
        // do not read permissions, since everybody will have full access
      } else if (everybodyPermission == NodeSharePermission.READ) {
        // read just write permissions, since everybody can read
        writeUsers = getUsersWithTransitivePermission(nodeURL, ResourcePermission.WRITE);
        writeGroups = getGroupsWithTransitivePermission(nodeURL, ResourcePermission.WRITE);
      } else {
        // read all permissions, since there is no everybody permission
        writeUsers = getUsersWithTransitivePermission(nodeURL, ResourcePermission.WRITE);
        writeGroups = getGroupsWithTransitivePermission(nodeURL, ResourcePermission.WRITE);
        readUsers = getUsersWithTransitivePermission(nodeURL, ResourcePermission.READ);
        readGroups = getGroupsWithTransitivePermission(nodeURL, ResourcePermission.READ);
      }

      return buildMaterializedPermissions(nodeURL, readUsers, writeUsers, readGroups, writeGroups);
    } else {
      return null;
    }
  }

  private CedarNodeMaterializedPermissions buildMaterializedPermissions(String id, List<FolderServerUser> readUsers,
                                                                        List<FolderServerUser> writeUsers,
                                                                        List<FolderServerGroup> readGroups,
                                                                        List<FolderServerGroup> writeGroups) {
    CedarNodeMaterializedPermissions permissions = new CedarNodeMaterializedPermissions(id);
    if (readUsers != null) {
      for (FolderServerUser user : readUsers) {
        permissions.setUserPermission(user.getId(), ResourcePermission.READ);
      }
    }
    if (writeUsers != null) {
      for (FolderServerUser user : writeUsers) {
        permissions.setUserPermission(user.getId(), ResourcePermission.WRITE);
      }
    }
    if (readGroups != null) {
      for (FolderServerGroup group : readGroups) {
        permissions.setGroupPermission(group.getId(), ResourcePermission.READ);
      }
    }
    if (writeGroups != null) {
      for (FolderServerGroup group : writeGroups) {
        permissions.setGroupPermission(group.getId(), ResourcePermission.WRITE);
      }
    }
    return permissions;
  }

  private List<FolderServerUser> getUsersWithTransitivePermission(String nodeURL, ResourcePermission permission) {
    return proxies.permission().getUsersWithTransitivePermissionOnNode(nodeURL, permission);
  }

  private List<FolderServerGroup> getGroupsWithTransitivePermission(String nodeURL, ResourcePermission permission) {
    return proxies.permission().getGroupsWithTransitivePermissionOnNode(nodeURL, permission);
  }
}
