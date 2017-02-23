package org.metadatacenter.server.dao;

import com.fasterxml.jackson.databind.JsonNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.server.result.BackendCallResult;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;
import java.util.List;

public interface GenericUserDao {

  @NonNull CedarUser create(@NonNull CedarUser user) throws IOException;

  CedarUser find(@NonNull String id) throws IOException;

  @NonNull List<CedarUser> findAll() throws IOException;

  @NonNull BackendCallResult<CedarUser> update(@NonNull String userId, CedarUser user);

  @NonNull BackendCallResult<CedarUser> patch(@NonNull String userId, JsonNode modification);
}
