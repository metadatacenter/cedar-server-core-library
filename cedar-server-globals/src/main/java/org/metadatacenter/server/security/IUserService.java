package org.metadatacenter.server.security;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;

public interface IUserService {
  CedarUser findUserByApiKey(String apiKey) throws IOException, ProcessingException;

  CedarUser findUser(String id) throws IOException, ProcessingException;

}
