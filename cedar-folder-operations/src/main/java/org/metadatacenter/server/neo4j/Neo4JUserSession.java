package org.metadatacenter.server.neo4j;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.PathComponent;
import org.metadatacenter.model.folderserver.CedarFSFolder;
import org.metadatacenter.model.folderserver.CedarFSNode;
import org.metadatacenter.model.folderserver.CedarFSResource;
import org.metadatacenter.model.folderserver.CedarFSUser;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.service.UserService;
import org.metadatacenter.util.json.JsonMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Neo4JUserSession {
  private CedarUser cu;
  private Neo4JProxy neo4JProxy;
  private String userIdPrefix;

  public Neo4JUserSession(Neo4JProxy neo4JProxy, CedarUser cu) {
    this.neo4JProxy = neo4JProxy;
    this.cu = cu;
    this.userIdPrefix = neo4JProxy.getUserIdPrefix();
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
          userService.updateUser(cu.getUserId(), homeModification);
          System.out.println("User updated");
        } catch (Exception e) {
          System.out.println("Error while updating the user:");
          e.printStackTrace();
        }
      }
    }
    return neo4JUserSession;
  }

  private String getUserId() {
    // let the NPE, something is really wrong if that happens
    return userIdPrefix + cu.getUserId();
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

  // Expose methods of Neo4JProxy
  // Convert folderId from URL style into internal representation
  // Other resource ids should not be converted
  // Add user info to calls
  public String getFolderUUID(String folderId) {
    return neo4JProxy.getFolderUUID(folderId);
  }

  public String getResourceUUID(String resourceId, CedarNodeType nodeType) {
    return neo4JProxy.getResourceUUID(resourceId, nodeType);
  }

  public CedarFSFolder findFolderById(String folderURL) {
    return neo4JProxy.findFolderById(getFolderUUID(folderURL));
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

  public CedarFSFolder createFolderAsChildOfId(String parentFolderURL, String name, String description, NodeLabel
      label) {
    return createFolderAsChildOfId(parentFolderURL, name, description, label, null);
  }

  public CedarFSFolder createFolderAsChildOfId(String parentFolderURL, String name, String description, NodeLabel
      label, Map<NodeExtraParameter, Object> extraProperties) {
    return neo4JProxy.createFolderAsChildOfId(getFolderUUID(parentFolderURL), name, description,
        getUserId(), label, extraProperties);
  }

  public CedarFSResource createResourceAsChildOfId(String parentFolderURL, String childURL, CedarNodeType
      nodeType, String name, String description, NodeLabel label) {
    return createResourceAsChildOfId(parentFolderURL, childURL, nodeType, name, description, label, null);
  }

  public CedarFSResource createResourceAsChildOfId(String parentFolderURL, String childURL, CedarNodeType
      nodeType, String name, String description, NodeLabel label, Map<NodeExtraParameter, Object> extraProperties) {
    return neo4JProxy.createResourceAsChildOfId(getFolderUUID(parentFolderURL), childURL, nodeType, name,
        description, getUserId(), label, extraProperties);
  }

  public CedarFSFolder updateFolderById(String folderURL, Map<String, String> updateFields) {
    return neo4JProxy.updateFolderById(getFolderUUID(folderURL), updateFields, getUserId());
  }

  public CedarFSResource updateResourceById(String resourceURL, CedarNodeType nodeType, Map<String,
      String> updateFields) {
    return neo4JProxy.updateResourceById(resourceURL, updateFields, getUserId());
  }

  public boolean deleteFolderById(String folderURL) {
    return neo4JProxy.deleteFolderById(getFolderUUID(folderURL));
  }

  public boolean deleteResourceById(String resourceURL, CedarNodeType nodeType) {
    return neo4JProxy.deleteResourceById(resourceURL);
  }

  public CedarFSFolder findFolderByPath(String path) {
    return neo4JProxy.findFolderByPath(path);
  }

  public CedarFSFolder findFolderByParentIdAndName(CedarFSFolder parentFolder, String name) {
    return neo4JProxy.findFolderByParentIdAndName(getFolderUUID(parentFolder.getId()), name);
  }

  public CedarFSNode findNodeByParentIdAndName(CedarFSFolder parentFolder, String name) {
    return neo4JProxy.findNodeByParentIdAndName(getFolderUUID(parentFolder.getId()), name);
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
      return neo4JProxy.findFolderPathById(getFolderUUID(folder.getId()));
    }
  }

  public List<CedarFSNode> findFolderContents(String folderURL, List<CedarNodeType> nodeTypeList, int
      limit, int offset, List<String> sortList) {
    return neo4JProxy.findFolderContents(getFolderUUID(folderURL), nodeTypeList, limit, offset, sortList, cu);
  }

  public long findFolderContentsCount(String folderURL) {
    return neo4JProxy.findFolderContentsCount(getFolderUUID(folderURL));
  }

  public long findFolderContentsCount(String folderURL, List<CedarNodeType> nodeTypeList) {
    return neo4JProxy.findFolderContentsFilteredCount(getFolderUUID(folderURL), nodeTypeList);
  }

  public void ensureGlobalObjectsExists() {
    Neo4jConfig config = neo4JProxy.getConfig();
    IPathUtil pathUtil = neo4JProxy.getPathUtil();

    String userId = getUserId();

    CedarFSUser cedarAdmin = neo4JProxy.findUserById(userId);
    if (cedarAdmin == null) {
      cedarAdmin = neo4JProxy.createUser(userId);
    }

    CedarFSFolder rootFolder = findFolderByPath(config.getRootFolderPath());
    String rootFolderURL = null;
    if (rootFolder == null) {
      rootFolder = neo4JProxy.createRootFolder(userId);
    }
    if (rootFolder != null) {
      rootFolderURL = rootFolder.getId();
    }
    CedarFSFolder usersFolder = findFolderByPath(config.getUsersFolderPath());
    if (usersFolder == null) {
      Map<NodeExtraParameter, Object> extraParams = new HashMap<>();
      extraParams.put(NodeExtraParameter.IS_SYSTEM, true);
      extraParams.put(NodeExtraParameter.IS_PUBLICLY_READABLE, true);
      usersFolder = createFolderAsChildOfId(rootFolderURL, pathUtil.extractName(config.getUsersFolderPath()), config
          .getUsersFolderDescription(), NodeLabel.SYSTEM_FOLDER, extraParams);
    }
    CedarFSFolder lostAndFoundFolder = findFolderByPath(config.getLostAndFoundFolderPath());
    if (lostAndFoundFolder == null) {
      Map<NodeExtraParameter, Object> extraParams = new HashMap<>();
      extraParams.put(NodeExtraParameter.IS_SYSTEM, true);
      extraParams.put(NodeExtraParameter.IS_PUBLICLY_READABLE, true);
      lostAndFoundFolder = createFolderAsChildOfId(rootFolderURL, pathUtil.extractName(config
              .getLostAndFoundFolderPath()), config.getLostAndFoundFolderDescription(), NodeLabel.SYSTEM_FOLDER,
          extraParams);
    }
  }

  public CedarFSFolder ensureUserHomeExists() {
    Neo4jConfig config = neo4JProxy.getConfig();
    IPathUtil pathUtil = neo4JProxy.getPathUtil();
    String userHomePath = config.getUsersFolderPath() + pathUtil.getSeparator() + cu.getUserId();
    CedarFSFolder currentUserHomeFolder = findFolderByPath(userHomePath);
    if (currentUserHomeFolder == null) {
      CedarFSFolder usersFolder = findFolderByPath(config.getUsersFolderPath());
      // usersFolder should not be null at this point. If it is, we let the NPE to be thrown
      Map<NodeExtraParameter, Object> extraParams = new HashMap<>();
      extraParams.put(NodeExtraParameter.IS_USER_HOME, true);
      extraParams.put(NodeExtraParameter.IS_SYSTEM, true);
      currentUserHomeFolder = createFolderAsChildOfId(usersFolder.getId(), cu.getUserId(), "", NodeLabel
          .USER_HOME_FOLDER, extraParams);
      if (currentUserHomeFolder != null) {
        return currentUserHomeFolder;
      }
    }
    return null;
  }

  public CedarFSUser ensureUserExists() {
    CedarFSUser currentUser = neo4JProxy.findUserById(getUserId());
    if (currentUser == null) {
      currentUser = neo4JProxy.createUser(getUserId());
    }
    return currentUser;
  }

  public void addPathAndParentId(CedarFSFolder folder) {
    if (folder != null) {
      List<CedarFSFolder> path = findFolderPath(folder);
      if (path != null) {
        folder.setPath(getPathString(path));
        folder.setParentPath(getParentPathString(path));
        folder.setParentFolderId(getParentId(path));
        folder.setPathComponents(getPathComponentsForFolderPath(path));
      }
    }
  }

  public void addPathAndParentId(CedarFSResource resource) {
    if (resource != null) {
      List<CedarFSNode> path = neo4JProxy.findNodePathById(resource.getId());
      if (path != null) {
        resource.setPath(getPathString(path));
        resource.setParentPath(getParentPathString(path));
        resource.setParentFolderId(getParentId(path));
        resource.setPathComponents(getPathComponentsForNodePath(path));
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

  private List<PathComponent> getPathComponentsForNodePath(List<CedarFSNode> path) {
    List<PathComponent> ret = new ArrayList<>();
    for (CedarFSNode node : path) {
      boolean isUserHome = false;
      if (node instanceof CedarFSFolder) {
        isUserHome = ((CedarFSFolder) node).isUserHome();
      }
      ret.add(new PathComponent(node.getName(), isUserHome));
    }
    return ret;
  }

  private List<PathComponent> getPathComponentsForFolderPath(List<CedarFSFolder> path) {
    List<PathComponent> ret = new ArrayList<>();
    for (CedarFSFolder folder : path) {
      ret.add(new PathComponent(folder.getName(), folder.isUserHome()));
    }
    return ret;
  }

  public boolean wipeAllData() {
    return neo4JProxy.wipeAllData();
  }
}
