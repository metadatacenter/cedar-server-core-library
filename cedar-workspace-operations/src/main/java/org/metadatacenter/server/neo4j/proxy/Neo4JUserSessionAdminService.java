package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.IsRoot;
import org.metadatacenter.model.IsSystem;
import org.metadatacenter.model.IsUserHome;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.AdminServiceSession;
import org.metadatacenter.server.neo4j.*;
import org.metadatacenter.server.neo4j.parameter.NodeProperty;
import org.metadatacenter.server.security.model.auth.NodePermission;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.util.CedarUserNameUtil;

public class Neo4JUserSessionAdminService extends AbstractNeo4JUserSession implements AdminServiceSession {

  public Neo4JUserSessionAdminService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu) {
    super(cedarConfig, proxies, cu);
  }

  public static AdminServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser) {
    return new Neo4JUserSessionAdminService(cedarConfig, proxies, cedarUser);
  }

  @Override
  public boolean wipeAllData() {
    return proxies.admin().wipeAllData();
  }

  @Override
  public void ensureGlobalObjectsExists() {
    Neo4jConfig config = proxies.config;
    PathUtil pathUtil = proxies.pathUtil;

    boolean addAdminToEverybody = false;

    String userId = cu.getId();

    FolderServerUser cedarAdmin = proxies.user().findUserById(userId);
    if (cedarAdmin == null) {
      String displayName = CedarUserNameUtil.getDisplayName(cedarConfig, cu);
      cedarAdmin = proxies.user().createUser(userId, displayName, displayName, cu.getFirstName(), cu.getLastName(),
          cu.getEmail());
      addAdminToEverybody = true;
    }

    FolderServerGroup everybody = proxies.group().findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
    if (everybody == null) {
      String everybodyURL = linkedDataUtil.buildNewLinkedDataId(CedarNodeType.GROUP);
      everybody = proxies.group().createGroup(everybodyURL, config.getEverybodyGroupName(),
          config.getEverybodyGroupDisplayName(), config.getEverybodyGroupDescription(), userId, Neo4JFieldValues
              .SPECIAL_GROUP_EVERYBODY);
      addAdminToEverybody = true;
    }

    if (addAdminToEverybody) {
      proxies.user().addGroupToUser(cedarAdmin, everybody);
    }

    FolderServerFolder rootFolder = proxies.folder().findFolderByPath(config.getRootFolderPath());
    String rootFolderURL = null;
    if (rootFolder == null) {
      rootFolder = proxies.folder().createRootFolder(userId);
      proxies.permission().addPermission(rootFolder, everybody, NodePermission.READTHIS);
    }
    if (rootFolder != null) {
      rootFolderURL = rootFolder.getId();
    }

    FolderServerFolder usersFolder = proxies.folder().findFolderByPath(config.getUsersFolderPath());
    if (usersFolder == null) {
      String name = pathUtil.extractName(config.getUsersFolderPath());
      usersFolder = proxies.folder().createFolderAsChildOfId(rootFolderURL, name, config.getUsersFolderDescription(),
          cedarAdmin.getId(), IsRoot.FALSE, IsSystem.TRUE, IsUserHome.FALSE, null);
      proxies.permission().addPermission(usersFolder, everybody, NodePermission.READTHIS);
    }
  }

  @Override
  public boolean createUniqueConstraint(NodeLabel nodeLabel, NodeProperty property) {
    return proxies.admin().createUniqueConstraint(nodeLabel, property);
  }

  @Override
  public boolean createIndex(NodeLabel nodeLabel, NodeProperty property) {
    return proxies.admin().createIndex(nodeLabel, property);
  }
}
