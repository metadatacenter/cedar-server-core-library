package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.FolderServerFolder;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.AdminServiceSession;
import org.metadatacenter.server.neo4j.*;
import org.metadatacenter.server.neo4j.cypher.NodeProperty;
import org.metadatacenter.server.security.model.auth.NodePermission;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.util.CedarUserNameUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Neo4JUserSessionAdminService extends AbstractNeo4JUserSession implements AdminServiceSession {

  protected static final Logger log = LoggerFactory.getLogger(Neo4JUserSessionAdminService.class);

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

    log.info("Checking/Creating Global Objects");

    String userId = cu.getId();
    log.info("Current User Id: " + userId);

    log.info("Looking for Admin User in Workspace");
    FolderServerUser cedarAdmin = proxies.user().findUserById(userId);
    if (cedarAdmin == null) {
      log.info("Admin user not found, trying to create it");
      String displayName = CedarUserNameUtil.getDisplayName(cedarConfig, cu);
      cedarAdmin = proxies.user().createUser(userId, displayName, cu.getFirstName(), cu.getLastName(), cu.getEmail());
      log.info("Admin user created, returned:" + cedarAdmin);
      addAdminToEverybody = true;
    } else {
      log.info("Admin user found");
    }

    log.info("Looking for Everybody Group in Workspace");
    FolderServerGroup everybody = proxies.group().findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
    if (everybody == null) {
      log.info("Everybody Group not found, trying to create it");
      String everybodyURL = linkedDataUtil.buildNewLinkedDataId(CedarNodeType.GROUP);
      log.info("Everybody Group URL just generated:" + everybodyURL);
      everybody = proxies.group().createGroup(everybodyURL, config.getEverybodyGroupName(),
          config.getEverybodyGroupDisplayName(), config.getEverybodyGroupDescription(), userId, Neo4JFieldValues
              .SPECIAL_GROUP_EVERYBODY);
      log.info("Everybody Group created, returned:" + everybody);
      addAdminToEverybody = true;
    } else {
      log.info("Everybody Group found");
    }

    if (addAdminToEverybody) {
      log.info("Adding Admin user to Everybody Group");
      boolean added = proxies.user().addUserToGroup(cedarAdmin, everybody);
      log.info("Admin user added to Everybody Group, returned:" + added);
    } else {
      log.info("Adding Admin user to Everybody Group is not needed");
    }

    FolderServerFolder rootFolder = proxies.folder().findFolderByPath(config.getRootFolderPath());
    String rootFolderURL = null;
    if (rootFolder == null) {
      rootFolder = proxies.folder().createRootFolder(userId);
      proxies.permission().addPermission(rootFolder, everybody, NodePermission.READ_THIS);
    }
    if (rootFolder != null) {
      rootFolderURL = rootFolder.getId();
    }

    FolderServerFolder usersFolder = proxies.folder().findFolderByPath(config.getUsersFolderPath());
    if (usersFolder == null) {
      String name = pathUtil.extractName(config.getUsersFolderPath());

      FolderServerFolder newUsersFolder = new FolderServerFolder();
      newUsersFolder.setName(name);
      newUsersFolder.setDescription(config.getUsersFolderDescription());
      newUsersFolder.setCreatedByTotal(cedarAdmin.getId());
      newUsersFolder.setRoot(false);
      newUsersFolder.setSystem(true);
      newUsersFolder.setUserHome(false);

      usersFolder = proxies.folder().createFolderAsChildOfId(newUsersFolder, rootFolderURL);
      proxies.permission().addPermission(usersFolder, everybody, NodePermission.READ_THIS);
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
