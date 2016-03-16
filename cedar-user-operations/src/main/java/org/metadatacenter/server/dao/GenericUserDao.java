package org.metadatacenter.server.dao;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.server.security.model.user.CedarUser;

import java.io.IOException;

public interface GenericUserDao {

  @NonNull CedarUser create(@NonNull CedarUser element) throws IOException;

  CedarUser find(@NonNull String id) throws IOException;
}
