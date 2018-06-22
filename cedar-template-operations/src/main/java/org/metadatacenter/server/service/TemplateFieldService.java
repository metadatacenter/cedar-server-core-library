package org.metadatacenter.server.service;

import org.metadatacenter.exception.TemplateServerResourceNotFoundException;

import java.io.IOException;
import java.util.List;

public interface TemplateFieldService<K, T> {

  T createTemplateField(T templateField) throws IOException;

  List<T> findAllTemplateFields(Integer limit, Integer offset, List<String> fieldName, FieldNameInEx includeExclude)
      throws IOException;

  T findTemplateField(String templateFieldId) throws IOException;

  T updateTemplateField(K templateFieldId, T content) throws TemplateServerResourceNotFoundException, IOException;

  void deleteTemplateField(K templateFieldId) throws TemplateServerResourceNotFoundException, IOException;

  void deleteAllTemplateFields();

  long count();

}