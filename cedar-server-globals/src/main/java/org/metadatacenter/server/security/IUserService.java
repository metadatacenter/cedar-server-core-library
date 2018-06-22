package org.metadatacenter.server.security;

import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;

public interface IUserService {
  CedarUser findUserByApiKey(String apiKey) throws IOException;

  CedarUser findUser(String id) throws IOException;

}
