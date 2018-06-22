package org.metadatacenter.server.service.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.mongodb.MongoClient;
import org.metadatacenter.server.dao.mongodb.UserDaoMongoDB;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.service.UserService;

import java.io.IOException;
import java.util.List;

public class UserServiceMongoDB implements UserService {

  private final UserDaoMongoDB userDao;


  public UserServiceMongoDB(MongoClient mongoClient, String db, String usersCollection) {
    this.userDao = new UserDaoMongoDB(mongoClient, db, usersCollection);
  }

  @Override
  public CedarUser createUser(CedarUser user) throws IOException {
    return userDao.create(user);
  }

  @Override
  public CedarUser findUser(String userId) throws IOException {
    return userDao.find(userId);
  }

  @Override
  public CedarUser findUserByApiKey(String apiKey) throws IOException {
    return userDao.findByApiKey(apiKey);
  }

  @Override
  public BackendCallResult<CedarUser> updateUser(String userId, CedarUser user) {
    return userDao.update(userId, user);
  }

  @Override
  public BackendCallResult<CedarUser> patchUser(String userId, JsonNode modifications) {
    return userDao.patch(userId, modifications);
  }

  @Override
  public List<CedarUser> findAll() throws IOException {
    return userDao.findAll();
  }

}
