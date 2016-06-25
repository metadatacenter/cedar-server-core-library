package org.metadatacenter.server.security;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;

public interface IUserService {
  public CedarUser findUserByApiKey(@NonNull String apiKey) throws IOException, ProcessingException;

  public CedarUser findUser(@NonNull String id) throws IOException, ProcessingException;

}
