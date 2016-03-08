package org.metadatacenter.server.service.mongodb;

import org.checkerframework.checker.nullness.qual.NonNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.dao.mongodb.UserDaoMongoDB;
import org.metadatacenter.server.service.UserService;

import java.io.IOException;

public class UserServiceMongoDB extends GenericUserServiceMongoDB<String, String, JsonNode> implements
    UserService<String, String, JsonNode> {

  private final
  @NonNull
  UserDaoMongoDB userDao;

  public UserServiceMongoDB(@NonNull String db, @NonNull String usersCollection) {
    this.userDao = new UserDaoMongoDB(db, usersCollection);
  }

  @Override
  @NonNull
  public JsonNode createUser(@NonNull JsonNode user) throws IOException {
    return userDao.create(user);
  }

  @Override
  public JsonNode findUser(@NonNull String userId) throws IOException, ProcessingException {
    return userDao.find(userId);
  }

  @Override
  public JsonNode findUserByApiKey(@NonNull String apiKey) throws IOException, ProcessingException {
    return userDao.findByApiKey(apiKey);
  }

}
