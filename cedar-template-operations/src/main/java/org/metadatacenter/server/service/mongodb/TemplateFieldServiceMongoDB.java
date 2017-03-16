package org.metadatacenter.server.service.mongodb;

import com.mongodb.MongoClient;
import org.checkerframework.checker.nullness.qual.NonNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.dao.mongodb.TemplateFieldDaoMongoDB;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.model.provenance.ProvenanceInfo;
import org.metadatacenter.server.service.FieldNameInEx;
import org.metadatacenter.server.service.TemplateFieldService;
import org.metadatacenter.util.ModelUtil;
import org.metadatacenter.util.provenance.ProvenanceUtil;

import javax.management.InstanceNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class TemplateFieldServiceMongoDB extends GenericTemplateServiceMongoDB<String, JsonNode> implements
    TemplateFieldService<String, JsonNode> {

  @NonNull
  private final TemplateFieldDaoMongoDB templateFieldDao;

  public TemplateFieldServiceMongoDB(@NonNull MongoClient mongoClient, @NonNull String db, @NonNull String
      templateFieldsCollection) {
    this.templateFieldDao = new TemplateFieldDaoMongoDB(mongoClient, db, templateFieldsCollection);
  }

  @Override
  public JsonNode createTemplateField(@NonNull JsonNode templateField) throws IOException {
    return templateFieldDao.create(templateField);
  }

  @Override
  @NonNull
  public List<JsonNode> findAllTemplateFields(Integer limit, Integer offset, List<String> fieldNames, FieldNameInEx
      includeExclude) throws IOException {
    return templateFieldDao.findAll(limit, offset, fieldNames, includeExclude);
  }

  @Override
  public JsonNode findTemplateField(@NonNull String templateFieldId) throws IOException,
      ProcessingException {
    return templateFieldDao.find(templateFieldId);
  }

  @Override
  public JsonNode updateTemplateField(@NonNull String templateFieldId, @NonNull JsonNode modifications) throws
      InstanceNotFoundException, IOException {
    return templateFieldDao.update(templateFieldId, modifications);
  }

  @Override
  public void deleteTemplateField(@NonNull String templateFieldId) throws InstanceNotFoundException, IOException {
    templateFieldDao.delete(templateFieldId);
  }

  @Override
  public void deleteAllTemplateFields() {
    templateFieldDao.deleteAll();
  }

  @Override
  public long count() {
    return templateFieldDao.count();
  }

  @Override
  public void saveNewFieldsAndReplaceIds(JsonNode genericInstance, ProvenanceInfo pi, ProvenanceUtil provenanceUtil,
                                         LinkedDataUtil linkedDataUtil) throws IOException {
    JsonNode properties = genericInstance.get("properties");
    if (properties != null) {
      Iterator<Map.Entry<String, JsonNode>> it = properties.fields();
      while (it.hasNext()) {
        Map.Entry<String, JsonNode> entry = it.next();
        JsonNode fieldCandidate = entry.getValue();
        // If the entry is an object
        if (fieldCandidate.isObject() && fieldCandidate.get("type") != null
            && !ModelUtil.isSpecialField(entry.getKey())) {
          String type = fieldCandidate.get("type").asText();
          if ("object".equals(type)) {
            saveFieldIfValid(fieldCandidate, pi, provenanceUtil, linkedDataUtil);
            // multiple instance
          } else if ("array".equals(type)) {
            saveFieldIfValid(fieldCandidate.get("items"), pi, provenanceUtil, linkedDataUtil);
          }
        }
      }
    }
  }

  private void saveFieldIfValid(JsonNode fieldCandidate, ProvenanceInfo pi, ProvenanceUtil provenanceUtil,
                                LinkedDataUtil linkedDataUtil) throws IOException {
    provenanceUtil.addProvenanceInfo(fieldCandidate, pi);
    if (fieldCandidate.get("@id") != null) {
      String id = fieldCandidate.get("@id").asText();
      if (id == null || id.indexOf(CedarConstants.TEMP_ID_PREFIX) == 0) {
        ((ObjectNode) fieldCandidate).remove("@id");
        ((ObjectNode) fieldCandidate).put("@id", generateNewId(linkedDataUtil));
        //TODO: this is commented, because we do not handle fields yet
        //templateFieldDao.create(fieldCandidate);
      }
    }
    // There is no @id field
    else {
      ((ObjectNode) fieldCandidate).put("@id", generateNewId(linkedDataUtil));
      //TODO: this is commented, because we do not handle fields yet
      //templateFieldDao.create(fieldCandidate);
    }
  }

  private String generateNewId(LinkedDataUtil linkedDataUtil) {
    return linkedDataUtil.buildNewLinkedDataId(CedarNodeType.FIELD);
  }

}
