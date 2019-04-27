package org.metadatacenter.server.search.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.config.CedarConfig;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.model.folderserver.basic.FolderServerNode;
import org.metadatacenter.rest.context.CedarRequestContext;
import org.metadatacenter.search.InfoField;
import org.metadatacenter.server.search.extraction.model.FieldValue;
import org.metadatacenter.server.search.extraction.model.TemplateNode;
import org.neo4j.driver.internal.async.HandshakeHandler;

import java.util.*;

public class TemplateInstanceContentExtractor {

  // TODO: Move to constants file
  private final String VALUE_FIELD_NAME = "@value";
  private final String ID_FIELD_NAME = "@id";
  private final String LABEL_FIELD_NAME = "rdfs:label";
  private final String SCHEMA_IS_BASED_ON_FIELD_NAME = "schema:isBasedOn";

  private ExtractionUtils extractionUtils;
  private TemplateContentExtractor templateContentExtractor;

  public TemplateInstanceContentExtractor(CedarConfig cedarConfig) {
    this.extractionUtils = new ExtractionUtils(cedarConfig);
    this.templateContentExtractor = new TemplateContentExtractor();
  }

  public List<InfoField> generateInfoFields(FolderServerNode folderServerNode, CedarRequestContext requestContext)
      throws CedarProcessingException {

    if (!folderServerNode.getType().equals(CedarNodeType.INSTANCE)) {
      throw new CedarProcessingException("The artifact must be an Instance but it is a "
          + folderServerNode.getType().name());
    }
    else {

      List<InfoField> infoFields = new ArrayList<>();
      JsonNode templateInstance = extractionUtils.getArtifactById(folderServerNode.getId(), folderServerNode.getType(), requestContext);
      String templateId = templateInstance.get(SCHEMA_IS_BASED_ON_FIELD_NAME).asText();
      JsonNode template = extractionUtils.getArtifactById(templateId, CedarNodeType.TEMPLATE, requestContext);
      List<TemplateNode> templateNodes = templateContentExtractor.getTemplateNodes(template);

      HashMap<String, TemplateNode> nodesMap = new HashMap<>();
      for (TemplateNode node : templateNodes) {
        nodesMap.put(node.generatePathDotNotation(), node);
      }
      List<FieldValue> fieldValues = getFieldValues(templateInstance, nodesMap, null, null);

      for (FieldValue fieldValue : fieldValues) {
        // TODO: set field name
        infoFields.add(new InfoField("", fieldValue.getFieldKey(), fieldValue.generatePathDotNotation(),
            fieldValue.getFieldValue(), fieldValue.getFieldValueUri()));
      }
      return infoFields;
    }
  }

  private List<FieldValue> getFieldValues(JsonNode currentNode, HashMap<String, TemplateNode> templateNodesMap,
                                          List<String> currentPath, List<FieldValue> results) throws CedarProcessingException {

    if (currentPath == null) { currentPath = new ArrayList<>(); }
    if (results == null) { results = new ArrayList(); }

    Iterator<Map.Entry<String, JsonNode>> jsonNodesIterator = currentNode.fields();
    while (jsonNodesIterator.hasNext()) {
      Map.Entry<String, JsonNode> currentNodeMap = jsonNodesIterator.next();
      List<String> tmpPath = new ArrayList<>();
      tmpPath.addAll(currentPath);
      tmpPath.add(currentNodeMap.getKey());
      String tmpPathDotNotation = getPathDotNotation(tmpPath);
      TemplateNode templateNode = null;
      if (templateNodesMap.containsKey(tmpPathDotNotation)) {
        templateNode = templateNodesMap.get(tmpPathDotNotation);
        // Not an array
        if (!templateNode.isArray()) {
          // Template Element
          if (templateNode.isTemplateElementNode()) {
            getFieldValues(currentNodeMap.getValue(), templateNodesMap, tmpPath, results);
          }
          // Template Field
          else if (templateNode.isTemplateFieldNode()) {
            // Extract value and save it to the results
            results.add(generateFieldValue(currentNodeMap.getValue(), tmpPath));
          }
          else {
            throw new CedarProcessingException("Unrecognized node type. The template node must be either a " +
                "Template Field or a Template Element. Node type: " + templateNode.getType().name());
          }
        }
        // Array
        else {
          // Array of template elements
          if (templateNode.isTemplateElementNode()) {
            for (JsonNode node : currentNodeMap.getValue()) {
              getFieldValues(node, templateNodesMap, tmpPath, results);
            }
          }
          // Array of template fields
          else if (templateNode.isTemplateFieldNode()) {
            for (JsonNode node : currentNodeMap.getValue()) {
              // Extract value and save it to the results
              results.add(generateFieldValue(node, tmpPath));
            }
          }
          else {
            throw new CedarProcessingException("Unrecognized node type. The template node must be either a " +
                "Template Field or a Template Element. Node type: " + templateNode.getType().name());
          }
        }
      }
      else {
        // Node not found in the map of template nodes. It is not a relevant node (e.g. @context) so we ignore it.
      }
    }
    return results;
  }

  private String getPathDotNotation(List<String> path) {
    return String.join(".", path);
  }

  private FieldValue generateFieldValue(JsonNode fieldNode, List<String> fieldPath) {
    FieldValue fieldValue = new FieldValue();
    fieldValue.setFieldKey(fieldPath.get(fieldPath.size()-1));
    fieldValue.setFieldPath(fieldPath);
    // Regular value
    if (fieldNode.hasNonNull(VALUE_FIELD_NAME) && !fieldNode.get(VALUE_FIELD_NAME).asText().isEmpty()) {
      fieldValue.setFieldValue(fieldNode.get(VALUE_FIELD_NAME).asText());
    }
    // Ontology term
    else {
      if (fieldNode.hasNonNull(LABEL_FIELD_NAME) && !fieldNode.get(LABEL_FIELD_NAME).asText().isEmpty()) {
        fieldValue.setFieldValue(fieldNode.get(LABEL_FIELD_NAME).asText());
      }
      if (fieldNode.hasNonNull(ID_FIELD_NAME) && !fieldNode.get(ID_FIELD_NAME).asText().isEmpty()) {
        fieldValue.setFieldValueUri(fieldNode.get(ID_FIELD_NAME).asText());
      }
    }
    return fieldValue;
  }


}
