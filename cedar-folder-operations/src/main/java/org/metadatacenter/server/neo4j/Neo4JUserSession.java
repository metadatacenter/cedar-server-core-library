package org.metadatacenter.server.neo4j;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.*;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.*;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.CedarUserExtract;
import org.metadatacenter.server.service.UserService;
import org.metadatacenter.util.CedarUserNameUtil;
import org.metadatacenter.util.json.JsonMapper;

import java.util.*;

public class Neo4JUserSession implements GroupServiceSession, ServiceSession, FolderServiceSession,
    PermissionServiceSession, UserServiceSession, AdminServiceSession {
  private final CedarUser cu;
  private final Neo4JProxy neo4JProxy;
  private final String userIdPrefix;
  private final String groupIdPrefix;

  public Neo4JUserSession(Neo4JProxy neo4JProxy, CedarUser cu) {
    this.neo4JProxy = neo4JProxy;
    this.cu = cu;
    this.userIdPrefix = neo4JProxy.getUserIdPrefix();
    this.groupIdPrefix = neo4JProxy.getGroupIdPrefix();
  }

  public static Neo4JUserSession get(Neo4JProxy neo4JProxy, UserService userService, CedarUser cu, boolean createHome) {
    Neo4JUserSession neo4JUserSession = new Neo4JUserSession(neo4JProxy, cu);
    if (createHome) {
      neo4JUserSession.ensureUserExists();
      FolderServerFolder createdFolder = neo4JUserSession.ensureUserHomeExists();
      if (createdFolder != null) {
        ObjectNode homeModification = JsonMapper.MAPPER.createObjectNode();
        homeModification.put("homeFolderId", createdFolder.getId());
        System.out.println("homeModification: " + homeModification);
        try {
          userService.updateUser(cu.getId(), homeModification);
          System.out.println("User updated");
        } catch (Exception e) {
          System.out.println("Error while updating the user:");
          e.printStackTrace();
        }
      }
    }
    return neo4JUserSession;
  }

  @Override
  public String getUserId() {
    // let the NPE, something is really wrong if that happens
    return userIdPrefix + cu.getId();
  }

  private String buildGroupId(String groupUUID) {
    return groupIdPrefix + groupUUID;
  }

  // Expose methods of PathUtil
  @Override
  public String sanitizeName(String name) {
    return neo4JProxy.getPathUtil().sanitizeName(name);
  }

  @Override
  public String normalizePath(String path) {
    return neo4JProxy.getPathUtil().normalizePath(path);
  }

  @Override
  public String getChildPath(String path, String name) {
    return neo4JProxy.getPathUtil().getChildPath(path, name);
  }

  @Override
  public String getRootPath() {
    return neo4JProxy.getPathUtil().getRootPath();
  }

  @Override
  public String getResourceUUID(String resourceId, CedarNodeType nodeType) {
    return neo4JProxy.getResourceUUID(resourceId, nodeType);
  }

  @Override
  public FolderServerFolder findFolderById(String folderURL) {
    return neo4JProxy.findFolderById(folderURL);
  }

  private FolderServerUser getNodeOwner(String nodeURL) {
    return neo4JProxy.getNodeOwner(nodeURL);
  }

  private List<FolderServerUser> getUsersWithPermission(String nodeURL, NodePermission permission) {
    return neo4JProxy.getUsersWithPermissionOnNode(nodeURL, permission);
  }

  private List<FolderServerGroup> getGroupsWithPermission(String nodeURL, NodePermission permission) {
    return neo4JProxy.getGroupsWithPermissionOnNode(nodeURL, permission);
  }

  @Override
  public List<FolderServerNode> findAllNodes(int limit, int offset, List<String> sortList) {
    return neo4JProxy.findAllNodes(limit, offset, sortList);
  }

  @Override
  public long findAllNodesCount() {
    return neo4JProxy.findAllNodesCount();
  }

  @Override
  public FolderServerResource findResourceById(String resourceURL) {
    return neo4JProxy.findResourceById(resourceURL);
  }

  @Override
  public FolderServerFolder createFolderAsChildOfId(String parentFolderURL, String name, String displayName, String
      description, NodeLabel label) {
    return createFolderAsChildOfId(parentFolderURL, name, displayName, description, label, null);
  }

  @Override
  public FolderServerFolder createFolderAsChildOfId(String parentFolderURL, String name, String displayName, String
      description, NodeLabel label, Map<String, Object> extraProperties) {
    return neo4JProxy.createFolderAsChildOfId(parentFolderURL, name, displayName, description,
        getUserId(), label, extraProperties);
  }

  @Override
  public FolderServerResource createResourceAsChildOfId(String parentFolderURL, String childURL, CedarNodeType
      nodeType, String name, String description, NodeLabel label) {
    return createResourceAsChildOfId(parentFolderURL, childURL, nodeType, name, description, label, null);
  }

  @Override
  public FolderServerResource createResourceAsChildOfId(String parentFolderURL, String childURL, CedarNodeType
      nodeType, String name, String description, NodeLabel label, Map<String, Object> extraProperties) {
    return neo4JProxy.createResourceAsChildOfId(parentFolderURL, childURL, nodeType, name,
        description, getUserId(), label, extraProperties);
  }

  @Override
  public FolderServerFolder updateFolderById(String folderURL, Map<String, String> updateFields) {
    return neo4JProxy.updateFolderById(folderURL, updateFields, getUserId());
  }

  @Override
  public FolderServerResource updateResourceById(String resourceURL, CedarNodeType nodeType, Map<String,
      String> updateFields) {
    return neo4JProxy.updateResourceById(resourceURL, updateFields, getUserId());
  }

  @Override
  public boolean deleteFolderById(String folderURL) {
    return neo4JProxy.deleteFolderById(folderURL);
  }

  @Override
  public boolean deleteResourceById(String resourceURL, CedarNodeType nodeType) {
    return neo4JProxy.deleteResourceById(resourceURL);
  }

  @Override
  public FolderServerFolder findFolderByPath(String path) {
    return neo4JProxy.findFolderByPath(path);
  }

  @Override
  public FolderServerFolder findFolderByParentIdAndName(FolderServerFolder parentFolder, String name) {
    return neo4JProxy.findFolderByParentIdAndName(parentFolder.getId(), name);
  }

  @Override
  public FolderServerNode findNodeByParentIdAndName(FolderServerFolder parentFolder, String name) {
    return neo4JProxy.findNodeByParentIdAndName(parentFolder.getId(), name);
  }

  @Override
  public List<FolderServerFolder> findFolderPathByPath(String path) {
    return neo4JProxy.findFolderPathByPath(path);
  }


  @Override
  public List<FolderServerFolder> findFolderPath(FolderServerFolder folder) {
    if (folder.isRoot()) {
      List<FolderServerFolder> pathInfo = new ArrayList<>();
      pathInfo.add(folder);
      return pathInfo;
    } else {
      return neo4JProxy.findFolderPathById(folder.getId());
    }
  }

  @Override
  public List<FolderServerNode> findFolderContents(String folderURL, List<CedarNodeType> nodeTypeList, int
      limit, int offset, List<String> sortList) {
    return neo4JProxy.findFolderContents(folderURL, nodeTypeList, limit, offset, sortList, cu);
  }

  @Override
  public long findFolderContentsCount(String folderURL) {
    return neo4JProxy.findFolderContentsCount(folderURL);
  }

  @Override
  public long findFolderContentsCount(String folderURL, List<CedarNodeType> nodeTypeList) {
    return neo4JProxy.findFolderContentsFilteredCount(folderURL, nodeTypeList);
  }

  @Override
  public void ensureGlobalObjectsExists() {
    Neo4jConfig config = neo4JProxy.getConfig();
    PathUtil pathUtil = neo4JProxy.getPathUtil();

    boolean addAdminToEverybody = false;

    String userId = getUserId();

    FolderServerUser cedarAdmin = neo4JProxy.findUserById(userId);
    if (cedarAdmin == null) {
      String displayName = CedarUserNameUtil.getDisplayName(cu);
      cedarAdmin = neo4JProxy.createUser(userId, displayName, displayName, cu.getFirstName(), cu
          .getLastName(), cu.getEmail());
      addAdminToEverybody = true;
    }

    FolderServerGroup everybody = neo4JProxy.findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
    if (everybody == null) {
      String everybodyURL = buildGroupId(UUID.randomUUID().toString());
      Map<String, Object> extraParams = new HashMap<>();
      extraParams.put(Neo4JFields.SPECIAL_GROUP, Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
      everybody = neo4JProxy.createGroup(everybodyURL, config.getEverybodyGroupName(),
          config.getEverybodyGroupDisplayName(), config.getEverybodyGroupDescription(), userId, extraParams);
      addAdminToEverybody = true;
    }

    if (addAdminToEverybody) {
      neo4JProxy.addGroupToUser(cedarAdmin, everybody);
    }

    FolderServerFolder rootFolder = findFolderByPath(config.getRootFolderPath());
    String rootFolderURL = null;
    if (rootFolder == null) {
      rootFolder = neo4JProxy.createRootFolder(userId);
      neo4JProxy.addPermission(rootFolder, everybody, NodePermission.READTHIS);
    }
    if (rootFolder != null) {
      rootFolderURL = rootFolder.getId();
    }

    FolderServerFolder usersFolder = findFolderByPath(config.getUsersFolderPath());
    if (usersFolder == null) {
      Map<String, Object> extraParams = new HashMap<>();
      extraParams.put(Neo4JFields.IS_SYSTEM, true);
      String name = pathUtil.extractName(config.getUsersFolderPath());
      usersFolder = createFolderAsChildOfId(rootFolderURL, name, name, config.getUsersFolderDescription(), NodeLabel
          .SYSTEM_FOLDER, extraParams);
      neo4JProxy.addPermission(usersFolder, everybody, NodePermission.READTHIS);
    }
  }

  @Override
  public FolderServerFolder ensureUserHomeExists() {
    Neo4jConfig config = neo4JProxy.getConfig();
    PathUtil pathUtil = neo4JProxy.getPathUtil();
    String userHomePath = config.getUsersFolderPath() + pathUtil.getSeparator() + cu.getId();
    FolderServerFolder currentUserHomeFolder = findFolderByPath(userHomePath);
    if (currentUserHomeFolder == null) {
      FolderServerFolder usersFolder = findFolderByPath(config.getUsersFolderPath());
      // usersFolder should not be null at this point. If it is, we let the NPE to be thrown
      Map<String, Object> extraParams = new HashMap<>();
      extraParams.put(Neo4JFields.IS_USER_HOME, true);
      String userId = cu.getId();
      String displayName = CedarUserNameUtil.getDisplayName(cu);
      String description = CedarUserNameUtil.getHomeFolderDescription(cu);
      currentUserHomeFolder = createFolderAsChildOfId(usersFolder.getId(), userId, displayName, description, NodeLabel
          .USER_HOME_FOLDER, extraParams);
      if (currentUserHomeFolder != null) {
        FolderServerGroup everybody = neo4JProxy.findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
        if (everybody != null) {
          neo4JProxy.addPermission(currentUserHomeFolder, everybody, NodePermission.READTHIS);
        }
        return currentUserHomeFolder;
      }
    }
    return null;
  }

  @Override
  public FolderServerUser ensureUserExists() {
    FolderServerUser currentUser = neo4JProxy.findUserById(getUserId());
    if (currentUser == null) {
      String displayName = CedarUserNameUtil.getDisplayName(cu);
      currentUser = neo4JProxy.createUser(getUserId(), displayName, displayName, cu.getFirstName(), cu.getLastName(),
          cu.getEmail());
      FolderServerGroup everybody = neo4JProxy.findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
      neo4JProxy.addGroupToUser(currentUser, everybody);
    }
    return currentUser;
  }

  @Override
  public void addPathAndParentId(FolderServerFolder folder) {
    if (folder != null) {
      List<FolderServerFolder> path = findFolderPath(folder);
      if (path != null) {
        folder.setPath(getPathString(path));
        folder.setParentPath(getParentPathString(path));
        folder.setDisplayPath(getDisplayPathString(path));
        folder.setDisplayParentPath(getDisplayParentPathString(path));
      }
    }
  }

  @Override
  public void addPathAndParentId(FolderServerResource resource) {
    if (resource != null) {
      List<FolderServerNode> path = neo4JProxy.findNodePathById(resource.getId());
      if (path != null) {
        resource.setPath(getPathString(path));
        resource.setParentPath(getParentPathString(path));
        resource.setDisplayPath(getDisplayPathString(path));
        resource.setDisplayParentPath(getDisplayParentPathString(path));
      }
    }
  }

  private String getParentId(List<? extends FolderServerNode> path) {
    if (path != null) {
      if (path.size() > 1) {
        return path.get(path.size() - 2).getId();
      }
    }
    return null;
  }

  private String getParentPathString(List<? extends FolderServerNode> path) {
    List<FolderServerNode> p = new ArrayList<>();
    p.addAll(path);
    if (path.size() > 0) {
      p.remove(p.size() - 1);
    } else {
      return null;
    }
    return getPathString(p);
  }

  private String getPathString(List<? extends FolderServerNode> path) {
    StringBuilder sb = new StringBuilder();
    boolean addSeparator = false;
    for (FolderServerNode node : path) {
      if (addSeparator) {
        sb.append(neo4JProxy.getPathUtil().getSeparator());
      }
      if (node instanceof FolderServerFolder) {
        if (!((FolderServerFolder) node).isRoot()) {
          addSeparator = true;
        }
      }
      sb.append(node.getName());
    }
    return sb.length() == 0 ? null : sb.toString();
  }

  private String getDisplayParentPathString(List<? extends FolderServerNode> path) {
    List<FolderServerNode> p = new ArrayList<>();
    p.addAll(path);
    if (path.size() > 0) {
      p.remove(p.size() - 1);
    } else {
      return null;
    }
    return getDisplayPathString(p);
  }

  private String getDisplayPathString(List<? extends FolderServerNode> path) {
    StringBuilder sb = new StringBuilder();
    boolean addSeparator = false;
    for (FolderServerNode node : path) {
      if (addSeparator) {
        sb.append(neo4JProxy.getPathUtil().getSeparator());
      }
      if (node instanceof FolderServerFolder) {
        if (!((FolderServerFolder) node).isRoot()) {
          addSeparator = true;
        }
      }
      sb.append(node.getDisplayName());
    }
    return sb.length() == 0 ? null : sb.toString();
  }

  @Override
  public boolean wipeAllData() {
    return neo4JProxy.wipeAllData();
  }

  @Override
  public String getHomeFolderPath() {
    Neo4jConfig config = this.neo4JProxy.getConfig();
    PathUtil pathUtil = this.neo4JProxy.getPathUtil();
    return config.getUsersFolderPath() + pathUtil.getSeparator() + this.cu.getId();
  }

  @Override
  public CedarNodePermissions getNodePermissions(String nodeURL, boolean nodeIsFolder) {
    FolderServerNode node;
    if (nodeIsFolder) {
      node = findFolderById(nodeURL);
    } else {
      node = findResourceById(nodeURL);
    }
    if (node != null) {
      FolderServerUser owner = getNodeOwner(nodeURL);
      List<FolderServerUser> readUsers = getUsersWithPermission(nodeURL, NodePermission.READ);
      List<FolderServerUser> writeUsers = getUsersWithPermission(nodeURL, NodePermission.WRITE);
      List<FolderServerGroup> readGroups = getGroupsWithPermission(nodeURL, NodePermission.READ);
      List<FolderServerGroup> writeGroups = getGroupsWithPermission(nodeURL, NodePermission.WRITE);
      return buildPermissions(owner, readUsers, writeUsers, readGroups, writeGroups);
    } else {
      return null;
    }
  }

  private CedarNodePermissions buildPermissions(FolderServerUser owner, List<FolderServerUser> readUsers,
                                                List<FolderServerUser>
                                                    writeUsers, List<FolderServerGroup> readGroups,
                                                List<FolderServerGroup> writeGroups) {
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
  public BackendCallResult updateNodePermissions(String nodeURL, CedarNodePermissionsRequest request,
                                                 boolean nodeIsFolder) {

    PermissionRequestValidator prv = new PermissionRequestValidator(this, nodeURL, request, nodeIsFolder);
    BackendCallResult bcr = prv.getCallResult();
    if (bcr.isError()) {
      return bcr;
    } else {
      CedarNodePermissions currentPermissions = getNodePermissions(nodeURL, nodeIsFolder);
      CedarNodePermissions newPermissions = prv.getPermissions();

      String oldOwnerId = currentPermissions.getOwner().getId();
      String newOwnerId = newPermissions.getOwner().getId();
      if (oldOwnerId != null && !oldOwnerId.equals(newOwnerId)) {
        Neo4JUserSessionGroupOperations.updateNodeOwner(neo4JProxy, nodeURL, newOwnerId, nodeIsFolder);
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
        Neo4JUserSessionGroupOperations.removeUserPermissions(neo4JProxy, nodeURL, toRemoveUserPermissions,
            nodeIsFolder);
      }

      Set<NodePermissionUserPermissionPair> toAddUserPermissions = new HashSet<>();
      toAddUserPermissions.addAll(newUserPermissions);
      toAddUserPermissions.removeAll(oldUserPermissions);
      if (!toAddUserPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.addUserPermissions(neo4JProxy, nodeURL, toAddUserPermissions, nodeIsFolder);
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
        Neo4JUserSessionGroupOperations.removeGroupPermissions(neo4JProxy, nodeURL, toRemoveGroupPermissions,
            nodeIsFolder);
      }

      Set<NodePermissionGroupPermissionPair> toAddGroupPermissions = new HashSet<>();
      toAddGroupPermissions.addAll(newGroupPermissions);
      toAddGroupPermissions.removeAll(oldGroupPermissions);
      if (!toAddGroupPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.addGroupPermissions(neo4JProxy, nodeURL, toAddGroupPermissions, nodeIsFolder);
      }
      return new BackendCallResult();
    }
  }

  @Override
  public boolean userIsOwnerOfFolder(String folderURL) {
    FolderServerUser owner = getNodeOwner(folderURL);
    return owner != null && owner.getId().equals(getUserId());
  }

  @Override
  public boolean userHasReadAccessToFolder(String folderURL) {
    return neo4JProxy.userHasReadAccessToFolder(getUserId(), folderURL) || neo4JProxy.userHasWriteAccessToFolder(cu
        .getId(), folderURL);
  }

  @Override
  public boolean userHasWriteAccessToFolder(String folderURL) {
    return neo4JProxy.userHasWriteAccessToFolder(getUserId(), folderURL);
  }

  @Override
  public boolean userIsOwnerOfResource(String resourceURL) {
    FolderServerUser owner = getNodeOwner(resourceURL);
    return owner != null && owner.getId().equals(getUserId());
  }

  @Override
  public boolean userHasReadAccessToResource(String resourceURL) {
    return neo4JProxy.userHasReadAccessToResource(getUserId(), resourceURL) || neo4JProxy.userHasWriteAccessToFolder(cu
        .getId(), resourceURL);
  }

  @Override
  public boolean userHasWriteAccessToResource(String resourceURL) {
    return neo4JProxy.userHasWriteAccessToResource(getUserId(), resourceURL);
  }

  @Override
  public List<FolderServerUser> findUsers() {
    return neo4JProxy.findUsers();
  }

  @Override
  public List<FolderServerGroup> findGroups() {
    return neo4JProxy.findGroups();
  }

  @Override
  public boolean userIsOwnerOfNode(FolderServerNode node) {
    FolderServerUser nodeOwner = getNodeOwner(node.getId());
    return nodeOwner != null && nodeOwner.getId().equals(getUserId());
  }

  @Override
  public FolderServerUser findUserById(String userURL) {
    return neo4JProxy.findUserById(userURL);
  }

  @Override
  public FolderServerGroup findGroupById(String groupURL) {
    return neo4JProxy.findGroupById(groupURL);
  }

  @Override
  public boolean moveResource(FolderServerResource sourceResource, FolderServerFolder targetFolder) {
    return neo4JProxy.moveResource(sourceResource, targetFolder);
  }

  @Override
  public boolean moveFolder(FolderServerFolder sourceFolder, FolderServerFolder targetFolder) {
    return neo4JProxy.moveFolder(sourceFolder, targetFolder);
  }

  @Override
  public Map<String, String> findAccessibleNodeIds() {
    return neo4JProxy.findAccessibleNodeIds(getUserId());
  }

  @Override
  public FolderServerGroup findGroupByName(String groupName) {
    return neo4JProxy.findGroupByName(groupName);
  }

  @Override
  public FolderServerGroup createGroup(String groupName, String groupDisplayName, String groupDescription) {
    String groupURL = buildGroupId(UUID.randomUUID().toString());
    return neo4JProxy.createGroup(groupURL, groupName, groupDisplayName, groupDescription, getUserId(), null);
  }

  @Override
  public FolderServerGroup updateGroupById(String groupURL, Map<String, String> updateFields) {
    return neo4JProxy.updateGroupById(groupURL, updateFields, getUserId());
  }

  @Override
  public boolean deleteGroupById(String groupURL) {
    return neo4JProxy.deleteGroupById(groupURL);
  }

  @Override
  public CedarGroupUsers findGroupUsers(String groupURL) {
    Set<String> memberIds = new HashSet<>();
    Set<String> administratorsIds = new HashSet<>();
    Map<String, FolderServerUser> users = new HashMap<>();
    List<FolderServerUser> groupMembers = neo4JProxy.findGroupMembers(groupURL);
    for (FolderServerUser member : groupMembers) {
      memberIds.add(member.getId());
      users.put(member.getId(), member);
    }
    List<FolderServerUser> groupAdministrators = neo4JProxy.findGroupAdministrators(groupURL);
    for (FolderServerUser administrator : groupAdministrators) {
      administratorsIds.add(administrator.getId());
      users.put(administrator.getId(), administrator);
    }
    CedarGroupUsers ret = new CedarGroupUsers();
    for (String userURL : users.keySet()) {
      ret.addUser(new CedarGroupUser(users.get(userURL).buildExtract(),
              administratorsIds.contains(userURL),
              memberIds.contains(userURL))
      );
    }
    return ret;
  }

  @Override
  public BackendCallResult updateGroupUsers(String groupURL, CedarGroupUsersRequest request) {

    GroupUsersRequestValidator gurv = new GroupUsersRequestValidator(this, groupURL, request);
    BackendCallResult bcr = gurv.getCallResult();
    if (bcr.isError()) {
      return bcr;
    } else {
      CedarGroupUsers currentGroupUsers = findGroupUsers(groupURL);
      CedarGroupUsers newGroupUsers = gurv.getUsers();

      Neo4JUserSessionGroupOperations.updateGroupUsers(neo4JProxy, groupURL, currentGroupUsers, newGroupUsers,
          RelationLabel.ADMINISTERS, Neo4JUserSessionGroupOperations.Filter.ADMINISTRATOR);
      Neo4JUserSessionGroupOperations.updateGroupUsers(neo4JProxy, groupURL, currentGroupUsers, newGroupUsers,
          RelationLabel.MEMBEROF, Neo4JUserSessionGroupOperations.Filter.MEMBER);

      return new BackendCallResult();
    }
  }


}
