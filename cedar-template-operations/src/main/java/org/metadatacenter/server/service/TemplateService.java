package org.metadatacenter.server.service;

import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;

public interface TemplateService<K, T> {

  @NonNull T createTemplate(@NonNull T template) throws IOException;

  @NonNull List<T> findAllTemplates() throws IOException;

  @NonNull List<T> findAllTemplates(List<String> fieldNames, FieldNameInEx includeExclude) throws IOException;

  @NonNull List<T> findAllTemplates(Integer limit, Integer offset, List<String> fieldNames, FieldNameInEx
      includeExclude) throws IOException;

  T findTemplate(@NonNull K templateId) throws IOException, ProcessingException;

  @NonNull T updateTemplate(@NonNull K templateId, @NonNull T content) throws InstanceNotFoundException,
      IOException;

  void deleteTemplate(@NonNull K templateId) throws InstanceNotFoundException, IOException;

  @NonNull boolean existsTemplate(@NonNull K templateId) throws IOException;

  void deleteAllTemplates();

  long count();
}
