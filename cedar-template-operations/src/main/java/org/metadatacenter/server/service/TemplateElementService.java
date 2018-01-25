package org.metadatacenter.server.service;

import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.exception.TemplateServerResourceNotFoundException;

import java.io.IOException;
import java.util.List;

public interface TemplateElementService<K, T> {

  T createTemplateElement(T templateElement) throws IOException;

  List<T> findAllTemplateElements() throws IOException;

  List<T> findAllTemplateElements(List<String> fieldName, FieldNameInEx includeExclude) throws IOException;

  List<T> findAllTemplateElements(Integer limit, Integer offset, List<String> fieldName, FieldNameInEx
      includeExclude) throws IOException;

  T findTemplateElement(K templateElementId) throws IOException, ProcessingException;

  T updateTemplateElement(K templateElementId, T content) throws TemplateServerResourceNotFoundException, IOException;

  void deleteTemplateElement(K templateElementId) throws TemplateServerResourceNotFoundException, IOException;

  boolean existsTemplateElement(K templateElementId) throws IOException;

  void deleteAllTemplateElements();

  long count();
}