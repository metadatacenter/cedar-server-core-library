package org.metadatacenter.server.dao;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;
import java.util.List;

public interface GenericUserDao {

  CedarUser create(CedarUser user) throws IOException;

  CedarUser find(String id) throws IOException;

  List<CedarUser> findAll() throws IOException;

  BackendCallResult<CedarUser> update(String userId, CedarUser user);

  BackendCallResult<CedarUser> patch(String userId, JsonNode modification);
}
