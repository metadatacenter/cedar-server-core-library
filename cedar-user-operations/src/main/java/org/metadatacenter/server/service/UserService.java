package org.metadatacenter.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.security.IUserService;
import org.metadatacenter.server.security.model.user.CedarUser;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;

public interface UserService extends IUserService {

  @NonNull
  public CedarUser createUser(@NonNull CedarUser user) throws IOException;

  public CedarUser findUser(@NonNull String userId) throws IOException, ProcessingException;

  public CedarUser findUserByApiKey(@NonNull String apiKey) throws IOException, ProcessingException;

  public CedarUser updateUser(@NonNull String userId, JsonNode modifications) throws IOException, ProcessingException,
      InstanceNotFoundException;

  public List<CedarUser> findAll() throws IOException, ProcessingException;

}
