package org.metadatacenter.server.service.mongodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.mongodb.MongoClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.exception.TemplateServerResourceNotFoundException;
import org.metadatacenter.server.dao.mongodb.TemplateDaoMongoDB;
import org.metadatacenter.server.service.FieldNameInEx;
import org.metadatacenter.server.service.TemplateService;

import java.io.IOException;
import java.util.List;

public class TemplateServiceMongoDB extends GenericTemplateServiceMongoDB<String, JsonNode> implements
    TemplateService<String, JsonNode> {

  @NonNull
  private final TemplateDaoMongoDB templateDao;


  public TemplateServiceMongoDB(@NonNull MongoClient mongoClient, @NonNull String db, @NonNull String
      templatesCollection) {
    this.templateDao = new TemplateDaoMongoDB(mongoClient, db, templatesCollection);
  }

  @Override
  @NonNull
  public JsonNode createTemplate(@NonNull JsonNode template) throws IOException {
    return templateDao.create(template);
  }

  @Override
  @NonNull
  public List<JsonNode> findAllTemplates() throws IOException {
    return templateDao.findAll();
  }

  @Override
  @NonNull
  public List<JsonNode> findAllTemplates(List<String> fieldNames, FieldNameInEx includeExclude) throws IOException {
    return templateDao.findAll(fieldNames, includeExclude);
  }

  @Override
  @NonNull
  public List<JsonNode> findAllTemplates(Integer limit, Integer offset, List<String> fieldNames, FieldNameInEx
      includeExclude) throws IOException {
    return templateDao.findAll(limit, offset, fieldNames, includeExclude);
  }

  @Override
  public JsonNode findTemplate(@NonNull String templateId)
      throws IOException, ProcessingException {
    return templateDao.find(templateId);
  }

  @Override
  public JsonNode updateTemplate(@NonNull String templateId, @NonNull JsonNode content)
      throws TemplateServerResourceNotFoundException, IOException {
    return templateDao.update(templateId, content);
  }

  @Override
  public void deleteTemplate(@NonNull String templateId) throws TemplateServerResourceNotFoundException, IOException {
    templateDao.delete(templateId);
  }

  @Override
  public boolean existsTemplate(@NonNull String templateId) throws IOException {
    return templateDao.exists(templateId);
  }

  @Override
  public void deleteAllTemplates() {
    templateDao.deleteAll();
  }

  @Override
  public long count() {
    return templateDao.count();
  }


}
