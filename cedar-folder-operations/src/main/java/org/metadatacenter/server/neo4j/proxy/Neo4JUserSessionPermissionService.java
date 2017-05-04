package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerNode;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.PermissionServiceSession;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.model.FolderOrResource;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.*;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.CedarUserExtract;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Neo4JUserSessionPermissionService extends AbstractNeo4JUserSession implements PermissionServiceSession {

  public Neo4JUserSessionPermissionService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu) {
    super(cedarConfig, proxies, cu);
  }

  public static PermissionServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser) {
    return new Neo4JUserSessionPermissionService(cedarConfig, proxies, cedarUser);
  }

  @Override
  public CedarNodePermissions getNodePermissions(String nodeURL, FolderOrResource folderOrResource) {
    FolderServerNode node;
    if (folderOrResource == FolderOrResource.FOLDER) {
      node = proxies.folder().findFolderById(nodeURL);
    } else {
      node = proxies.resource().findResourceById(nodeURL);
    }
    if (node != null) {
      FolderServerUser owner = getNodeOwner(nodeURL);
      List<FolderServerUser> readUsers = getUsersWithDirectPermission(nodeURL, NodePermission.READ);
      List<FolderServerUser> writeUsers = getUsersWithDirectPermission(nodeURL, NodePermission.WRITE);
      List<FolderServerGroup> readGroups = getGroupsWithDirectPermission(nodeURL, NodePermission.READ);
      List<FolderServerGroup> writeGroups = getGroupsWithDirectPermission(nodeURL, NodePermission.WRITE);
      return buildPermissions(owner, readUsers, writeUsers, readGroups, writeGroups);
    } else {
      return null;
    }
  }

  private FolderServerUser getNodeOwner(String nodeURL) {
    return proxies.node().getNodeOwner(nodeURL);
  }

  private List<FolderServerUser> getUsersWithDirectPermission(String nodeURL, NodePermission permission) {
    return proxies.permission().getUsersWithDirectPermissionOnNode(nodeURL, permission);
  }

  private List<FolderServerGroup> getGroupsWithDirectPermission(String nodeURL, NodePermission permission) {
    return proxies.permission().getGroupsWithDirectPermissionOnNode(nodeURL, permission);
  }

  @Override
  public BackendCallResult updateNodePermissions(String nodeURL, CedarNodePermissionsRequest request,
                                                 FolderOrResource folderOrResource) {

    PermissionRequestValidator prv = new PermissionRequestValidator(this, proxies, nodeURL, request, folderOrResource);
    BackendCallResult bcr = prv.getCallResult();
    if (bcr.isError()) {
      return bcr;
    } else {
      CedarNodePermissions currentPermissions = getNodePermissions(nodeURL, folderOrResource);
      CedarNodePermissions newPermissions = prv.getPermissions();

      String oldOwnerId = currentPermissions.getOwner().getId();
      String newOwnerId = newPermissions.getOwner().getId();
      if (oldOwnerId != null && !oldOwnerId.equals(newOwnerId)) {
        Neo4JUserSessionGroupOperations.updateNodeOwner(proxies.node(), nodeURL, newOwnerId, folderOrResource);
      }

      Set<NodePermissionUserPermissionPair> oldUserPermissions = new HashSet<>();
      for (CedarNodeUserPermission up : currentPermissions.getUserPermissions()) {
        oldUserPermissions.add(up.getAsUserIdPermissionPair());
      }
      Set<NodePermissionUserPermissionPair> newUserPermissions = new HashSet<>();
      for (CedarNodeUserPermission up : newPermissions.getUserPermissions()) {
        newUserPermissions.add(up.getAsUserIdPermissionPair());
      }

      Set<NodePermissionUserPermissionPair> toRemoveUserPermissions = new HashSet<>();
      toRemoveUserPermissions.addAll(oldUserPermissions);
      toRemoveUserPermissions.removeAll(newUserPermissions);
      if (!toRemoveUserPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.removeUserPermissions(proxies.permission(), nodeURL, toRemoveUserPermissions,
            folderOrResource);
      }

      Set<NodePermissionUserPermissionPair> toAddUserPermissions = new HashSet<>();
      toAddUserPermissions.addAll(newUserPermissions);
      toAddUserPermissions.removeAll(oldUserPermissions);
      if (!toAddUserPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.addUserPermissions(proxies.permission(), nodeURL, toAddUserPermissions,
            folderOrResource);
      }

      Set<NodePermissionGroupPermissionPair> oldGroupPermissions = new HashSet<>();
      for (CedarNodeGroupPermission gp : currentPermissions.getGroupPermissions()) {
        oldGroupPermissions.add(gp.getAsGroupIdPermissionPair());
      }
      Set<NodePermissionGroupPermissionPair> newGroupPermissions = new HashSet<>();
      for (CedarNodeGroupPermission gp : newPermissions.getGroupPermissions()) {
        newGroupPermissions.add(gp.getAsGroupIdPermissionPair());
      }

      Set<NodePermissionGroupPermissionPair> toRemoveGroupPermissions = new HashSet<>();
      toRemoveGroupPermissions.addAll(oldGroupPermissions);
      toRemoveGroupPermissions.removeAll(newGroupPermissions);
      if (!toRemoveGroupPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.removeGroupPermissions(proxies.permission(), nodeURL, toRemoveGroupPermissions,
            folderOrResource);
      }

      Set<NodePermissionGroupPermissionPair> toAddGroupPermissions = new HashSet<>();
      toAddGroupPermissions.addAll(newGroupPermissions);
      toAddGroupPermissions.removeAll(oldGroupPermissions);
      if (!toAddGroupPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.addGroupPermissions(proxies.permission(), nodeURL, toAddGroupPermissions,
            folderOrResource);
      }
      return new BackendCallResult();
    }
  }

  @Override
  public boolean userCanChangeOwnerOfFolder(String folderURL) {
    if (cu.has(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE)) {
      return true;
    } else {
      FolderServerUser owner = getNodeOwner(folderURL);
      return owner != null && owner.getId().equals(cu.getId());
    }
  }

  @Override
  public boolean userHasReadAccessToFolder(String folderURL) {
    if (cu.has(CedarPermission.READ_NOT_READABLE_NODE)) {
      return true;
    } else {
      return proxies.permission().userHasReadAccessToFolder(cu.getId(), folderURL) || proxies.permission()
          .userHasWriteAccessToFolder(cu.getId(), folderURL);
    }
  }

  @Override
  public boolean userHasWriteAccessToFolder(String folderURL) {
    if (cu.has(CedarPermission.WRITE_NOT_WRITABLE_NODE)) {
      return true;
    } else {
      return proxies.permission().userHasWriteAccessToFolder(cu.getId(), folderURL);
    }
  }

  @Override
  public boolean userCanChangeOwnerOfResource(String resourceURL) {
    if (cu.has(CedarPermission.UPDATE_PERMISSION_NOT_WRITABLE_NODE)) {
      return true;
    } else {
      FolderServerUser owner = getNodeOwner(resourceURL);
      return owner != null && owner.getId().equals(cu.getId());
    }
  }

  @Override
  public boolean userHasReadAccessToResource(String resourceURL) {
    if (cu.has(CedarPermission.READ_NOT_READABLE_NODE)) {
      return true;
    } else {
      return proxies.permission().userHasReadAccessToResource(cu.getId(), resourceURL) || proxies.permission()
          .userHasWriteAccessToFolder(cu.getId(), resourceURL);
    }
  }

  @Override
  public boolean userHasWriteAccessToResource(String resourceURL) {
    if (cu.has(CedarPermission.WRITE_NOT_WRITABLE_NODE)) {
      return true;
    } else {
      return proxies.permission().userHasWriteAccessToResource(cu.getId(), resourceURL);
    }
  }

  @Override
  public boolean userIsOwnerOfNode(FolderServerNode node) {
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
        CedarNodeUserPermission up = new CedarNodeUserPermission(u, NodePermission.READ);
        permissions.addUserPermissions(up);
      }
    }
    if (writeUsers != null) {
      for (FolderServerUser user : writeUsers) {
        CedarUserExtract u = user.buildExtract();
        CedarNodeUserPermission up = new CedarNodeUserPermission(u, NodePermission.WRITE);
        permissions.addUserPermissions(up);
      }
    }
    if (readGroups != null) {
      for (FolderServerGroup group : readGroups) {
        CedarGroupExtract g = group.buildExtract();
        CedarNodeGroupPermission gp = new CedarNodeGroupPermission(g, NodePermission.READ);
        permissions.addGroupPermissions(gp);
      }
    }
    if (writeGroups != null) {
      for (FolderServerGroup group : writeGroups) {
        CedarGroupExtract g = group.buildExtract();
        CedarNodeGroupPermission gp = new CedarNodeGroupPermission(g, NodePermission.WRITE);
        permissions.addGroupPermissions(gp);
      }
    }
    return permissions;
  }

  @Override
  public CedarNodeMaterializedPermissions getNodeMaterializedPermission(String nodeURL, FolderOrResource
      folderOrResource) {
    FolderServerNode node;
    if (folderOrResource == FolderOrResource.FOLDER) {
      node = proxies.folder().findFolderById(nodeURL);
    } else {
      node = proxies.resource().findResourceById(nodeURL);
    }
    if (node != null) {
      List<FolderServerUser> readUsers = getUsersWithTransitivePermission(nodeURL, NodePermission.READ,
          folderOrResource);
      List<FolderServerUser> writeUsers = getUsersWithTransitivePermission(nodeURL, NodePermission.WRITE,
          folderOrResource);
      List<FolderServerGroup> readGroups = getGroupsWithTransitivePermission(nodeURL, NodePermission.READ,
          folderOrResource);
      List<FolderServerGroup> writeGroups = getGroupsWithTransitivePermission(nodeURL, NodePermission.WRITE,
          folderOrResource);
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
        permissions.setUserPermission(user.getId(), NodePermission.READ);
      }
    }
    if (writeUsers != null) {
      for (FolderServerUser user : writeUsers) {
        permissions.setUserPermission(user.getId(), NodePermission.WRITE);
      }
    }
    if (readGroups != null) {
      for (FolderServerGroup group : readGroups) {
        permissions.setGroupPermission(group.getId(), NodePermission.READ);
      }
    }
    if (writeGroups != null) {
      for (FolderServerGroup group : writeGroups) {
        permissions.setGroupPermission(group.getId(), NodePermission.WRITE);
      }
    }
    return permissions;
  }

  private List<FolderServerUser> getUsersWithTransitivePermission(String nodeURL, NodePermission permission,
                                                                  FolderOrResource
                                                                      folderOrResource) {
    return proxies.permission().getUsersWithTransitivePermissionOnNode(nodeURL, permission, folderOrResource);
  }

  private List<FolderServerGroup> getGroupsWithTransitivePermission(String nodeURL, NodePermission permission,
                                                                    FolderOrResource
                                                                        folderOrResource) {
    return proxies.permission().getGroupsWithTransitivePermissionOnNode(nodeURL, permission, folderOrResource);
  }

}
