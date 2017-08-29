package org.metadatacenter.server.service;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.exception.TemplateServerResourceNotFoundException;

import java.io.IOException;
import java.util.List;

public interface TemplateInstanceService<K, T> {

  @NonNull T createTemplateInstance(@NonNull T templateInstance) throws IOException;

  @NonNull List<T> findAllTemplateInstances() throws IOException;

  @NonNull List<T> findAllTemplateInstances(List<String> fieldNames, FieldNameInEx includeExclude) throws IOException;

  @NonNull List<T> findAllTemplateInstances(Integer limit, Integer offset, List<String> fieldNames, FieldNameInEx
      includeExclude) throws IOException;

  T findTemplateInstance(@NonNull K templateInstanceId) throws IOException;

  @NonNull T updateTemplateInstance(@NonNull K templateInstanceId, @NonNull T content) throws
      TemplateServerResourceNotFoundException, IOException;

  void deleteTemplateInstance(@NonNull K templateInstanceId) throws TemplateServerResourceNotFoundException,
      IOException;

  void deleteAllTemplateInstances();

  long count();

  long countReferencingTemplate(@NonNull K templateId);
}
