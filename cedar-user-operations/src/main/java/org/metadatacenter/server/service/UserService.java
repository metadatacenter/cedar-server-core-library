package org.metadatacenter.server.service;

import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.security.IUserService;

import java.io.IOException;

public interface UserService<K, A, T> extends IUserService<K, A, T> {

  @NonNull
  public T createUser(@NonNull T user) throws IOException;

  public T findUser(@NonNull K userId) throws IOException, ProcessingException;

  public T findUserByApiKey(@NonNull A apiKey) throws IOException, ProcessingException;

}
