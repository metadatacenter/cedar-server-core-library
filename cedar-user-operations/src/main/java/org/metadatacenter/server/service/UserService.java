package org.metadatacenter.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.IUserService;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;
import java.util.List;

public interface UserService extends IUserService {

  CedarUser createUser(CedarUser user) throws IOException;

  CedarUser findUser(String userId) throws IOException, ProcessingException;

  CedarUser findUserByApiKey(String apiKey) throws IOException, ProcessingException;

  BackendCallResult<CedarUser> updateUser(String userId, CedarUser user);

  BackendCallResult<CedarUser> patchUser(String userId, JsonNode modifications);

  List<CedarUser> findAll() throws IOException, ProcessingException;

}
