package org.metadatacenter.server.neo4j;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.*;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.auth.*;
import org.metadatacenter.server.security.model.user.CedarGroupExtract;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.security.model.user.CedarUserExtract;
import org.metadatacenter.server.service.UserService;
import org.metadatacenter.util.CedarUserNameUtil;
import org.metadatacenter.util.json.JsonMapper;

import java.util.*;

public class Neo4JUserSession {
  private CedarUser cu;
  private Neo4JProxy neo4JProxy;
  private String userIdPrefix;
  private String groupIdPrefix;

  public Neo4JUserSession(Neo4JProxy neo4JProxy, CedarUser cu) {
    this.neo4JProxy = neo4JProxy;
    this.cu = cu;
    this.userIdPrefix = neo4JProxy.getUserIdPrefix();
    this.groupIdPrefix = neo4JProxy.getGroupIdPrefix();
  }

  public static Neo4JUserSession get(Neo4JProxy neo4JProxy, UserService userService, CedarUser cu, boolean createHome) {
    Neo4JUserSession neo4JUserSession = new Neo4JUserSession(neo4JProxy, cu);
    if (createHome) {
      CedarFSUser createdUser = neo4JUserSession.ensureUserExists();
      CedarFSFolder createdFolder = neo4JUserSession.ensureUserHomeExists();
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

  public String getUserId() {
    // let the NPE, something is really wrong if that happens
    return userIdPrefix + cu.getId();
  }

  private String buildGroupId(String groupUUID) {
    return groupIdPrefix + groupUUID;
  }

  // Expose methods of PathUtil
  public String sanitizeName(String name) {
    return neo4JProxy.getPathUtil().sanitizeName(name);
  }

  public String normalizePath(String path) {
    return neo4JProxy.getPathUtil().normalizePath(path);
  }

  public String getChildPath(String path, String name) {
    return neo4JProxy.getPathUtil().getChildPath(path, name);
  }

  public String getRootPath() {
    return neo4JProxy.getPathUtil().getRootPath();
  }

  public String getResourceUUID(String resourceId, CedarNodeType nodeType) {
    return neo4JProxy.getResourceUUID(resourceId, nodeType);
  }

  public CedarFSFolder findFolderById(String folderURL) {
    return neo4JProxy.findFolderById(folderURL);
  }

  private CedarFSUser getNodeOwner(String nodeURL) {
    return neo4JProxy.getNodeOwner(nodeURL);
  }

  private List<CedarFSUser> getUsersWithPermission(String nodeURL, NodePermission permission) {
    return neo4JProxy.getUsersWithPermissionOnNode(nodeURL, permission);
  }

  private List<CedarFSGroup> getGroupsWithPermission(String nodeURL, NodePermission permission) {
    return neo4JProxy.getGroupsWithPermissionOnNode(nodeURL, permission);
  }

  public List<CedarFSNode> findAllNodes(int limit, int offset, List<String> sortList) {
    return neo4JProxy.findAllNodes(limit, offset, sortList);
  }

  public long findAllNodesCount() {
    return neo4JProxy.findAllNodesCount();
  }

  public CedarFSResource findResourceById(String resourceURL) {
    return neo4JProxy.findResourceById(resourceURL);
  }

  public CedarFSFolder createFolderAsChildOfId(String parentFolderURL, String name, String displayName, String
      description, NodeLabel label) {
    return createFolderAsChildOfId(parentFolderURL, name, displayName, description, label, null);
  }

  public CedarFSFolder createFolderAsChildOfId(String parentFolderURL, String name, String displayName, String
      description, NodeLabel label, Map<String, Object> extraProperties) {
    return neo4JProxy.createFolderAsChildOfId(parentFolderURL, name, displayName, description,
        getUserId(), label, extraProperties);
  }

  public CedarFSResource createResourceAsChildOfId(String parentFolderURL, String childURL, CedarNodeType
      nodeType, String name, String description, NodeLabel label) {
    return createResourceAsChildOfId(parentFolderURL, childURL, nodeType, name, description, label, null);
  }

  public CedarFSResource createResourceAsChildOfId(String parentFolderURL, String childURL, CedarNodeType
      nodeType, String name, String description, NodeLabel label, Map<String, Object> extraProperties) {
    return neo4JProxy.createResourceAsChildOfId(parentFolderURL, childURL, nodeType, name,
        description, getUserId(), label, extraProperties);
  }

  public CedarFSFolder updateFolderById(String folderURL, Map<String, String> updateFields) {
    return neo4JProxy.updateFolderById(folderURL, updateFields, getUserId());
  }

  public CedarFSResource updateResourceById(String resourceURL, CedarNodeType nodeType, Map<String,
      String> updateFields) {
    return neo4JProxy.updateResourceById(resourceURL, updateFields, getUserId());
  }

  public boolean deleteFolderById(String folderURL) {
    return neo4JProxy.deleteFolderById(folderURL);
  }

  public boolean deleteResourceById(String resourceURL, CedarNodeType nodeType) {
    return neo4JProxy.deleteResourceById(resourceURL);
  }

  public CedarFSFolder findFolderByPath(String path) {
    return neo4JProxy.findFolderByPath(path);
  }

  public CedarFSFolder findFolderByParentIdAndName(CedarFSFolder parentFolder, String name) {
    return neo4JProxy.findFolderByParentIdAndName(parentFolder.getId(), name);
  }

  public CedarFSNode findNodeByParentIdAndName(CedarFSFolder parentFolder, String name) {
    return neo4JProxy.findNodeByParentIdAndName(parentFolder.getId(), name);
  }

  public List<CedarFSFolder> findFolderPathByPath(String path) {
    return neo4JProxy.findFolderPathByPath(path);
  }


  public List<CedarFSFolder> findFolderPath(CedarFSFolder folder) {
    if (folder.isRoot()) {
      List<CedarFSFolder> pathInfo = new ArrayList<>();
      pathInfo.add(folder);
      return pathInfo;
    } else {
      return neo4JProxy.findFolderPathById(folder.getId());
    }
  }

  public List<CedarFSNode> findFolderContents(String folderURL, List<CedarNodeType> nodeTypeList, int
      limit, int offset, List<String> sortList) {
    return neo4JProxy.findFolderContents(folderURL, nodeTypeList, limit, offset, sortList, cu);
  }

  public long findFolderContentsCount(String folderURL) {
    return neo4JProxy.findFolderContentsCount(folderURL);
  }

  public long findFolderContentsCount(String folderURL, List<CedarNodeType> nodeTypeList) {
    return neo4JProxy.findFolderContentsFilteredCount(folderURL, nodeTypeList);
  }

  public void ensureGlobalObjectsExists() {
    Neo4jConfig config = neo4JProxy.getConfig();
    IPathUtil pathUtil = neo4JProxy.getPathUtil();

    boolean addAdminToEverybody = false;

    String userId = getUserId();

    CedarFSUser cedarAdmin = neo4JProxy.findUserById(userId);
    if (cedarAdmin == null) {
      String displayName = CedarUserNameUtil.getDisplayName(cu);
      cedarAdmin = neo4JProxy.createUser(userId, displayName, displayName, cu.getFirstName(), cu
          .getLastName(), cu.getEmail());
      addAdminToEverybody = true;
    }

    CedarFSGroup everybody = neo4JProxy.findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
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

    CedarFSFolder rootFolder = findFolderByPath(config.getRootFolderPath());
    String rootFolderURL = null;
    if (rootFolder == null) {
      rootFolder = neo4JProxy.createRootFolder(userId);
      neo4JProxy.addPermission(rootFolder, everybody, NodePermission.READTHIS);
    }
    if (rootFolder != null) {
      rootFolderURL = rootFolder.getId();
    }

    CedarFSFolder usersFolder = findFolderByPath(config.getUsersFolderPath());
    if (usersFolder == null) {
      Map<String, Object> extraParams = new HashMap<>();
      extraParams.put(Neo4JFields.IS_SYSTEM, true);
      String name = pathUtil.extractName(config.getUsersFolderPath());
      usersFolder = createFolderAsChildOfId(rootFolderURL, name, name, config.getUsersFolderDescription(), NodeLabel
          .SYSTEM_FOLDER, extraParams);
      neo4JProxy.addPermission(usersFolder, everybody, NodePermission.READTHIS);
    }
  }

  public CedarFSFolder ensureUserHomeExists() {
    Neo4jConfig config = neo4JProxy.getConfig();
    IPathUtil pathUtil = neo4JProxy.getPathUtil();
    String userHomePath = config.getUsersFolderPath() + pathUtil.getSeparator() + cu.getId();
    CedarFSFolder currentUserHomeFolder = findFolderByPath(userHomePath);
    if (currentUserHomeFolder == null) {
      CedarFSFolder usersFolder = findFolderByPath(config.getUsersFolderPath());
      // usersFolder should not be null at this point. If it is, we let the NPE to be thrown
      Map<String, Object> extraParams = new HashMap<>();
      extraParams.put(Neo4JFields.IS_USER_HOME, true);
      String userId = cu.getId();
      String displayName = CedarUserNameUtil.getDisplayName(cu);
      String description = CedarUserNameUtil.getHomeFolderDescription(cu);
      currentUserHomeFolder = createFolderAsChildOfId(usersFolder.getId(), userId, displayName, description, NodeLabel
          .USER_HOME_FOLDER, extraParams);
      if (currentUserHomeFolder != null) {
        CedarFSGroup everybody = neo4JProxy.findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
        if (everybody != null) {
          neo4JProxy.addPermission(currentUserHomeFolder, everybody, NodePermission.READTHIS);
        }
        return currentUserHomeFolder;
      }
    }
    return null;
  }

  public CedarFSUser ensureUserExists() {
    CedarFSUser currentUser = neo4JProxy.findUserById(getUserId());
    if (currentUser == null) {
      String displayName = CedarUserNameUtil.getDisplayName(cu);
      currentUser = neo4JProxy.createUser(getUserId(), displayName, displayName, cu.getFirstName(), cu.getLastName(),
          cu.getEmail());
      CedarFSGroup everybody = neo4JProxy.findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
      neo4JProxy.addGroupToUser(currentUser, everybody);
    }
    return currentUser;
  }

  public void addPathAndParentId(CedarFSFolder folder) {
    if (folder != null) {
      List<CedarFSFolder> path = findFolderPath(folder);
      if (path != null) {
        folder.setPath(getPathString(path));
        folder.setParentPath(getParentPathString(path));
        folder.setDisplayPath(getDisplayPathString(path));
        folder.setDisplayParentPath(getDisplayParentPathString(path));
      }
    }
  }

  public void addPathAndParentId(CedarFSResource resource) {
    if (resource != null) {
      List<CedarFSNode> path = neo4JProxy.findNodePathById(resource.getId());
      if (path != null) {
        resource.setPath(getPathString(path));
        resource.setParentPath(getParentPathString(path));
        resource.setDisplayPath(getDisplayPathString(path));
        resource.setDisplayParentPath(getDisplayParentPathString(path));
      }
    }
  }

  private String getParentId(List<? extends CedarFSNode> path) {
    if (path != null) {
      if (path.size() > 1) {
        return path.get(path.size() - 2).getId();
      }
    }
    return null;
  }

  private String getParentPathString(List<? extends CedarFSNode> path) {
    List<CedarFSNode> p = new ArrayList<>();
    p.addAll(path);
    if (path.size() > 0) {
      p.remove(p.size() - 1);
    } else {
      return null;
    }
    return getPathString(p);
  }

  private String getPathString(List<? extends CedarFSNode> path) {
    StringBuilder sb = new StringBuilder();
    boolean addSeparator = false;
    for (CedarFSNode node : path) {
      if (addSeparator) {
        sb.append(neo4JProxy.getPathUtil().getSeparator());
      }
      if (node instanceof CedarFSFolder) {
        if (!((CedarFSFolder) node).isRoot()) {
          addSeparator = true;
        }
      }
      sb.append(node.getName());
    }
    return sb.length() == 0 ? null : sb.toString();
  }

  private String getDisplayParentPathString(List<? extends CedarFSNode> path) {
    List<CedarFSNode> p = new ArrayList<>();
    p.addAll(path);
    if (path.size() > 0) {
      p.remove(p.size() - 1);
    } else {
      return null;
    }
    return getDisplayPathString(p);
  }

  private String getDisplayPathString(List<? extends CedarFSNode> path) {
    StringBuilder sb = new StringBuilder();
    boolean addSeparator = false;
    for (CedarFSNode node : path) {
      if (addSeparator) {
        sb.append(neo4JProxy.getPathUtil().getSeparator());
      }
      if (node instanceof CedarFSFolder) {
        if (!((CedarFSFolder) node).isRoot()) {
          addSeparator = true;
        }
      }
      sb.append(node.getDisplayName());
    }
    return sb.length() == 0 ? null : sb.toString();
  }

  public boolean wipeAllData() {
    return neo4JProxy.wipeAllData();
  }

  public String getHomeFolderPath() {
    Neo4jConfig config = this.neo4JProxy.getConfig();
    IPathUtil pathUtil = this.neo4JProxy.getPathUtil();
    return config.getUsersFolderPath() + pathUtil.getSeparator() + this.cu.getId();
  }

  public CedarNodePermissions getNodePermissions(String nodeURL, boolean nodeIsFolder) {
    CedarFSNode node;
    if (nodeIsFolder) {
      node = findFolderById(nodeURL);
    } else {
      node = findResourceById(nodeURL);
    }
    if (node != null) {
      CedarFSUser owner = getNodeOwner(nodeURL);
      List<CedarFSUser> readUsers = getUsersWithPermission(nodeURL, NodePermission.READ);
      List<CedarFSUser> writeUsers = getUsersWithPermission(nodeURL, NodePermission.WRITE);
      List<CedarFSGroup> readGroups = getGroupsWithPermission(nodeURL, NodePermission.READ);
      List<CedarFSGroup> writeGroups = getGroupsWithPermission(nodeURL, NodePermission.WRITE);
      return buildPermissions(owner, readUsers, writeUsers, readGroups, writeGroups);
    } else {
      return null;
    }
  }

  private CedarNodePermissions buildPermissions(CedarFSUser owner, List<CedarFSUser> readUsers, List<CedarFSUser>
      writeUsers, List<CedarFSGroup> readGroups, List<CedarFSGroup> writeGroups) {
    CedarNodePermissions permissions = new CedarNodePermissions();
    CedarUserExtract o = owner.buildExtract();
    permissions.setOwner(o);
    if (readUsers != null) {
      for (CedarFSUser user : readUsers) {
        CedarUserExtract u = user.buildExtract();
        CedarNodeUserPermission up = new CedarNodeUserPermission(u, NodePermission.READ);
        permissions.addUserPermissions(up);
      }
    }
    if (writeUsers != null) {
      for (CedarFSUser user : writeUsers) {
        CedarUserExtract u = user.buildExtract();
        CedarNodeUserPermission up = new CedarNodeUserPermission(u, NodePermission.WRITE);
        permissions.addUserPermissions(up);
      }
    }
    if (readGroups != null) {
      for (CedarFSGroup group : readGroups) {
        CedarGroupExtract g = group.buildExtract();
        CedarNodeGroupPermission gp = new CedarNodeGroupPermission(g, NodePermission.READ);
        permissions.addGroupPermissions(gp);
      }
    }
    if (writeGroups != null) {
      for (CedarFSGroup group : writeGroups) {
        CedarGroupExtract g = group.buildExtract();
        CedarNodeGroupPermission gp = new CedarNodeGroupPermission(g, NodePermission.WRITE);
        permissions.addGroupPermissions(gp);
      }
    }
    return permissions;
  }

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
        Neo4JUserSessionGroupOperations.updateNodeOwner(neo4JProxy,nodeURL, newOwnerId, nodeIsFolder);
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
        Neo4JUserSessionGroupOperations.removeUserPermissions(neo4JProxy,nodeURL, toRemoveUserPermissions, nodeIsFolder);
      }

      Set<NodePermissionUserPermissionPair> toAddUserPermissions = new HashSet<>();
      toAddUserPermissions.addAll(newUserPermissions);
      toAddUserPermissions.removeAll(oldUserPermissions);
      if (!toAddUserPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.addUserPermissions(neo4JProxy,nodeURL, toAddUserPermissions, nodeIsFolder);
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
        Neo4JUserSessionGroupOperations.removeGroupPermissions(neo4JProxy,nodeURL, toRemoveGroupPermissions, nodeIsFolder);
      }

      Set<NodePermissionGroupPermissionPair> toAddGroupPermissions = new HashSet<>();
      toAddGroupPermissions.addAll(newGroupPermissions);
      toAddGroupPermissions.removeAll(oldGroupPermissions);
      if (!toAddGroupPermissions.isEmpty()) {
        Neo4JUserSessionGroupOperations.addGroupPermissions(neo4JProxy,nodeURL, toAddGroupPermissions, nodeIsFolder);
      }
      return new BackendCallResult();
    }
  }

  public boolean userIsOwnerOfFolder(String folderURL) {
    CedarFSUser owner = getNodeOwner(folderURL);
    return owner != null && owner.getId().equals(getUserId());
  }

  public boolean userHasReadAccessToFolder(String folderURL) {
    return neo4JProxy.userHasReadAccessToFolder(getUserId(), folderURL) || neo4JProxy.userHasWriteAccessToFolder(cu
        .getId(), folderURL);
  }

  public boolean userHasWriteAccessToFolder(String folderURL) {
    return neo4JProxy.userHasWriteAccessToFolder(getUserId(), folderURL);
  }

  public boolean userIsOwnerOfResource(String resourceURL) {
    CedarFSUser owner = getNodeOwner(resourceURL);
    return owner != null && owner.getId().equals(getUserId());
  }

  public boolean userHasReadAccessToResource(String resourceURL) {
    return neo4JProxy.userHasReadAccessToResource(getUserId(), resourceURL) || neo4JProxy.userHasWriteAccessToFolder(cu
        .getId(), resourceURL);
  }

  public boolean userHasWriteAccessToResource(String resourceURL) {
    return neo4JProxy.userHasWriteAccessToResource(getUserId(), resourceURL);
  }

  public List<CedarFSUser> findUsers() {
    return neo4JProxy.findUsers();
  }

  public List<CedarFSGroup> findGroups() {
    return neo4JProxy.findGroups();
  }

  public boolean userIsOwnerOfNode(CedarFSNode node) {
    CedarFSUser nodeOwner = getNodeOwner(node.getId());
    return nodeOwner != null && nodeOwner.getId().equals(getUserId());
  }

  public CedarFSUser findUserById(String userURL) {
    return neo4JProxy.findUserById(userURL);
  }

  public CedarFSGroup findGroupById(String groupURL) {
    return neo4JProxy.findGroupById(groupURL);
  }

  public boolean moveResource(CedarFSResource sourceResource, CedarFSFolder targetFolder) {
    return neo4JProxy.moveResource(sourceResource, targetFolder);
  }

  public boolean moveFolder(CedarFSFolder sourceFolder, CedarFSFolder targetFolder) {
    return neo4JProxy.moveFolder(sourceFolder, targetFolder);
  }

  public Map<String, String> findAccessibleNodeIds() {
    return neo4JProxy.findAccessibleNodeIds(getUserId());
  }

  public CedarFSGroup findGroupByName(String groupName) {
    return neo4JProxy.findGroupByName(groupName);
  }

  public CedarFSGroup createGroup(String groupName, String groupDisplayName, String groupDescription) {
    String groupURL = buildGroupId(UUID.randomUUID().toString());
    return neo4JProxy.createGroup(groupURL, groupName, groupDisplayName, groupDescription, getUserId(), null);
  }

  public CedarFSGroup updateGroupById(String groupURL, Map<String, String> updateFields) {
    return neo4JProxy.updateGroupById(groupURL, updateFields, getUserId());
  }

  public boolean deleteGroupById(String groupURL) {
    return neo4JProxy.deleteGroupById(groupURL);
  }

  public CedarGroupUsers findGroupUsers(String groupURL) {
    Set<String> memberIds = new HashSet<>();
    Set<String> administratorsIds = new HashSet<>();
    Map<String, CedarFSUser> users = new HashMap<>();
    List<CedarFSUser> groupMembers = neo4JProxy.findGroupMembers(groupURL);
    for (CedarFSUser member : groupMembers) {
      memberIds.add(member.getId());
      users.put(member.getId(), member);
    }
    List<CedarFSUser> groupAdministrators = neo4JProxy.findGroupAdministrators(groupURL);
    for (CedarFSUser administrator : groupAdministrators) {
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
