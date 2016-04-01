package org.metadatacenter.server.service;

import org.checkerframework.checker.nullness.qual.NonNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.provenance.ProvenanceInfo;

import java.io.IOException;
import java.util.List;

public interface TemplateFieldService<K, T> {

  @NonNull
  public List<T> findAllTemplateFields(Integer limit, Integer offset, List<String> fieldName, FieldNameInEx
      includeExclude) throws IOException;

  public T findTemplateField(@NonNull String templateFieldId) throws IOException, ProcessingException;

  public long count();

  public void saveNewFieldsAndReplaceIds(T genericInstance, ProvenanceInfo pi) throws IOException;
}