package org.metadatacenter.server.neo4j.proxy;

import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.model.folderserver.FolderServerGroup;
import org.metadatacenter.model.folderserver.FolderServerUser;
import org.metadatacenter.server.UserServiceSession;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.neo4j.AbstractNeo4JUserSession;
import org.metadatacenter.server.neo4j.Neo4JFieldValues;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.util.CedarUserNameUtil;

import java.util.List;

public class Neo4JUserSessionUserService extends AbstractNeo4JUserSession implements UserServiceSession {

  public Neo4JUserSessionUserService(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cu) {
    super(cedarConfig, proxies, cu);
  }

  public static UserServiceSession get(CedarConfig cedarConfig, Neo4JProxies proxies, CedarUser cedarUser) {
    return new Neo4JUserSessionUserService(cedarConfig, proxies, cedarUser);
  }

  @Override
  public List<FolderServerUser> findUsers() {
    return proxies.user().findUsers();
  }

  @Override
  public FolderServerUser findUserById(String userURL) {
    return proxies.user().findUserById(userURL);
  }

  @Override
  public FolderServerUser ensureUserExists() {
    FolderServerUser currentUser = proxies.user().findUserById(getUserId());
    if (currentUser == null) {
      String displayName = CedarUserNameUtil.getDisplayName(cedarConfig, cu);
      currentUser = proxies.user().createUser(getUserId(), displayName, displayName, cu.getFirstName(), cu
              .getLastName(),
          cu.getEmail());
      FolderServerGroup everybody = proxies.group().findGroupBySpecialValue(Neo4JFieldValues.SPECIAL_GROUP_EVERYBODY);
      proxies.user().addGroupToUser(currentUser, everybody);
    }
    return currentUser;
  }

}
