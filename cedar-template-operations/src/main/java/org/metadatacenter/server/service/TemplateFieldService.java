package org.metadatacenter.server.service;

import org.checkerframework.checker.nullness.qual.NonNull;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.server.model.provenance.ProvenanceInfo;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.List;

public interface TemplateFieldService<K, T> {

  @NonNull
  public T createTemplateField(@NonNull T templateField) throws IOException;

  @NonNull
  public List<T> findAllTemplateFields(Integer limit, Integer offset, List<String> fieldName, FieldNameInEx
      includeExclude) throws IOException;

  public T findTemplateField(@NonNull String templateFieldId) throws IOException, ProcessingException;

  @NonNull
  public T updateTemplateField(@NonNull K templateFieldId, @NonNull T modifications) throws
      InstanceNotFoundException, IOException;

  public void deleteTemplateField(@NonNull K templateFieldId) throws InstanceNotFoundException, IOException;

  public void deleteAllTemplateFields();

  public long count();

  public void saveNewFieldsAndReplaceIds(T genericInstance, ProvenanceInfo pi, String linkedDataIdBasePath) throws
      IOException;
}