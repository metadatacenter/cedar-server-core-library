package org.metadatacenter.server.service;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.exception.TemplateServerResourceNotFoundException;

import java.io.IOException;
import java.util.List;

public interface TemplateElementService<K, T> {

  @NonNull T createTemplateElement(@NonNull T templateElement) throws IOException;

  @NonNull List<T> findAllTemplateElements() throws IOException;

  @NonNull List<T> findAllTemplateElements(List<String> fieldName, FieldNameInEx includeExclude) throws IOException;

  @NonNull List<T> findAllTemplateElements(Integer limit, Integer offset, List<String> fieldName, FieldNameInEx
      includeExclude) throws IOException;

  T findTemplateElement(@NonNull K templateElementId) throws IOException, ProcessingException;

  T updateTemplateElement(@NonNull K templateElementId, @NonNull T content) throws
      TemplateServerResourceNotFoundException, IOException;

  void deleteTemplateElement(@NonNull K templateElementId) throws TemplateServerResourceNotFoundException, IOException;

  @NonNull boolean existsTemplateElement(@NonNull K templateElementId) throws IOException;

  void deleteAllTemplateElements();

  long count();
}