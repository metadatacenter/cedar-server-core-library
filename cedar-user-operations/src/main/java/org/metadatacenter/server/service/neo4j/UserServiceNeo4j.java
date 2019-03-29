package org.metadatacenter.server.service.neo4j;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.folderserver.basic.FolderServerGroup;
import org.metadatacenter.model.folderserver.basic.FolderServerUser;
import org.metadatacenter.server.neo4j.Neo4JFieldValues;
import org.metadatacenter.server.neo4j.proxy.Neo4JProxyGroup;
import org.metadatacenter.server.neo4j.proxy.Neo4JProxyUser;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UserServiceNeo4j implements UserService {

  private Neo4JProxyUser userProxy;

  public UserServiceNeo4j(Neo4JProxyUser userProxy) {
    this.userProxy = userProxy;
  }

  @Override
  public CedarUser createUser(CedarUser user) throws IOException {
    FolderServerUser u = userProxy.createUser(user);
    return u == null ? null : u.buildUser();
  }

  @Override
  public CedarUser findUser(String userId) throws IOException {
    FolderServerUser user = userProxy.findUserById(userId);
    return user == null ? null : user.buildUser();
  }

  @Override
  public CedarUser findUserByApiKey(String apiKey) throws IOException {
    FolderServerUser user = userProxy.findUserByApiKey(apiKey);
    return user == null ? null : user.buildUser();
  }

  @Override
  public BackendCallResult<CedarUser> updateUser(CedarUser user) {
    return userProxy.updateUser(user);
  }

  @Override
  public BackendCallResult<CedarUser> patchUser(String userId, JsonNode modifications) {
    return userProxy.patchUser(userId, modifications);
  }

  @Override
  public List<CedarUser> findAll() throws IOException {
    List<FolderServerUser> users = userProxy.findUsers();
    List<CedarUser> ret = new ArrayList<>();
    for (FolderServerUser fsu : users) {
      if (fsu != null) {
        ret.add(fsu.buildUser());
      }
    }
    return ret;
  }

}
