package org.metadatacenter.server.service;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.exception.TemplateServerResourceNotFoundException;

import java.io.IOException;
import java.util.List;

public interface TemplateService<K, T> {

  @NonNull T createTemplate(@NonNull T template) throws IOException;

  @NonNull List<T> findAllTemplates() throws IOException;

  @NonNull List<T> findAllTemplates(List<String> fieldNames, FieldNameInEx includeExclude) throws IOException;

  @NonNull List<T> findAllTemplates(Integer limit, Integer offset, List<String> fieldNames, FieldNameInEx
      includeExclude) throws IOException;

  T findTemplate(@NonNull K templateId) throws IOException, ProcessingException;

  T updateTemplate(@NonNull K templateId, @NonNull T content) throws TemplateServerResourceNotFoundException,
      IOException;

  void deleteTemplate(@NonNull K templateId) throws TemplateServerResourceNotFoundException, IOException;

  @NonNull boolean existsTemplate(@NonNull K templateId) throws IOException;

  void deleteAllTemplates();

  long count();
}
