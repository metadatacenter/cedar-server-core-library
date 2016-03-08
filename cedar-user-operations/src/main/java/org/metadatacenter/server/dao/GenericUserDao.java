package org.metadatacenter.server.dao;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;

public interface GenericUserDao<K, T> {

  @NonNull T create(@NonNull T element) throws IOException;

  T find(@NonNull K id) throws IOException;
}
