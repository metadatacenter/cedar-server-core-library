package org.metadatacenter.server.service;

import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.security.IUserService;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;

public interface UserService extends IUserService {

  @NonNull
  public CedarUser createUser(@NonNull CedarUser user) throws IOException;

  public CedarUser findUser(@NonNull String userId) throws IOException, ProcessingException;

  public CedarUser findUserByApiKey(@NonNull String apiKey) throws IOException, ProcessingException;

}
