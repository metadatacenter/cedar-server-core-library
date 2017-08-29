package org.metadatacenter.server.dao;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.exception.TemplateServerResourceNotFoundException;
import org.metadatacenter.server.service.FieldNameInEx;

import java.io.IOException;
import java.util.List;

public interface GenericDao<K, T> {

  @NonNull T create(@NonNull T element) throws IOException;

  @NonNull List<T> findAll() throws IOException;

  @NonNull List<T> findAll(Integer count, Integer page, List<String> fieldNames, FieldNameInEx includeExclude) throws
      IOException;

  @NonNull List<T> findAll(List<String> fieldNames, FieldNameInEx includeExclude) throws IOException;

  T find(@NonNull K id) throws IOException;

  T update(@NonNull K id, @NonNull T content) throws TemplateServerResourceNotFoundException, IOException;

  void delete(@NonNull K id) throws TemplateServerResourceNotFoundException, IOException;

  boolean exists(@NonNull K id) throws IOException;

  void deleteAll();

  long count();
}
