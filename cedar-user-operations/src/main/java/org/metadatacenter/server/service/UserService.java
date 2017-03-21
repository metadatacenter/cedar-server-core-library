package org.metadatacenter.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.IUserService;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;
import java.util.List;

public interface UserService extends IUserService {

  @NonNull CedarUser createUser(@NonNull CedarUser user) throws IOException;

  CedarUser findUser(@NonNull String userId) throws IOException, ProcessingException;

  CedarUser findUserByApiKey(@NonNull String apiKey) throws IOException, ProcessingException;

  BackendCallResult<CedarUser> updateUser(@NonNull String userId, CedarUser user);

  BackendCallResult<CedarUser> patchUser(@NonNull String userId, JsonNode modifications);

  List<CedarUser> findAll() throws IOException, ProcessingException;

}
