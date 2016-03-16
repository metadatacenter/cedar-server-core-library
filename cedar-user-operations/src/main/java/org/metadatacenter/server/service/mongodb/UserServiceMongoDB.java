package org.metadatacenter.server.service.mongodb;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.server.dao.mongodb.UserDaoMongoDB;
import org.metadatacenter.server.security.model.user.CedarUser;
import org.metadatacenter.server.service.UserService;

import java.io.IOException;

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

}
