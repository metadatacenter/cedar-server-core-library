package org.metadatacenter.server.service.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.server.dao.mongodb.UserDaoMongoDB;
import org.metadatacenter.server.result.BackendCallError;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.service.UserService;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;

public class UserServiceMongoDB implements UserService {

  @NonNull
  private final UserDaoMongoDB userDao;


  public UserServiceMongoDB(@NonNull String db, @NonNull String usersCollection) {
    this.userDao = new UserDaoMongoDB(db, usersCollection);
  }

  @Override
  public @NonNull CedarUser createUser(@NonNull CedarUser user) throws IOException {
    return userDao.create(user);
  }

  @Override
  public CedarUser findUser(@NonNull String userId) throws IOException, ProcessingException {
    return userDao.find(userId);
  }

  @Override
  public CedarUser findUserByApiKey(@NonNull String apiKey) throws IOException, ProcessingException {
    return userDao.findByApiKey(apiKey);
  }

  @Override
  public BackendCallResult<CedarUser> updateUser(@NonNull String userId, CedarUser user) {
    return userDao.update(userId, user);
  }

  @Override
  public BackendCallResult<CedarUser> patchUser(@NonNull String userId, JsonNode modifications) {
    return userDao.patch(userId, modifications);
  }

  @Override
  public List<CedarUser> findAll() throws IOException, ProcessingException {
    return userDao.findAll();
  }

}
