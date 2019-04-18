package org.metadatacenter.server.service;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.IUserService;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.util.List;

public interface UserService extends IUserService {

  CedarUser createUser(CedarUser user);

  CedarUser findUser(String userId);

  CedarUser findUserByApiKey(String apiKey);

  BackendCallResult<CedarUser> updateUser(CedarUser user);

  BackendCallResult<CedarUser> patchUser(String userId, JsonNode modifications);

  List<CedarUser> findAll();

}
