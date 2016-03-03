package org.metadatacenter.server.dao;

import checkers.nullness.quals.NonNull;

import java.io.IOException;

public interface GenericUserDao<K, T> {

  @NonNull T create(@NonNull T element) throws IOException;

  T find(@NonNull K id) throws IOException;
}
