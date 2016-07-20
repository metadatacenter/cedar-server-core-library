package org.metadatacenter.server.dao;

import com.fasterxml.jackson.databind.JsonNode;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.server.security.model.user.CedarUser;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;

public interface GenericUserDao {

  @NonNull CedarUser create(@NonNull CedarUser element) throws IOException;

  CedarUser find(@NonNull String id) throws IOException;

  @NonNull List<CedarUser> findAll() throws IOException;

  @NonNull CedarUser update(@NonNull String userId, JsonNode modification) throws IOException,
      InstanceNotFoundException;
}
