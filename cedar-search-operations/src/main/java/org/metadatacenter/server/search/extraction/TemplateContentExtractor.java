package org.metadatacenter.server.search.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarResourceType;
import org.metadatacenter.server.search.extraction.model.TemplateNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.metadatacenter.model.ModelNodeNames.*;

/**
 * Utilities to extract information from CEDAR Templates
 */
public class TemplateContentExtractor {

  private static final Logger log = LoggerFactory.getLogger(TemplateContentExtractor.class);

  public List<TemplateNode> getTemplateNodes(JsonNode template) throws CedarProcessingException {
    return getTemplateNodes(template, null, null);
  }

  /**
   * Returns summary information of all template nodes in the template.
   *
   * @param template Template in JSON
   * @param currentPath Used internally to store the current node path
   * @param results     Used internally to store the results
   * @return A list of the template elements and fields in the template, represented using the TemplateNode class
   */
  private List<TemplateNode> getTemplateNodes(JsonNode template, List<String> currentPath, List results) throws
      CedarProcessingException {
    if (currentPath == null) {
      currentPath = new ArrayList<>();
    }
    if (results == null) {
      results = new ArrayList();
    }
    Iterator<Map.Entry<String, JsonNode>> jsonFieldsIterator = template.fields();
    while (jsonFieldsIterator.hasNext()) {
      Map.Entry<String, JsonNode> jsonField = jsonFieldsIterator.next();
      final String jsonFieldKey = jsonField.getKey();
      if (jsonField.getValue().isContainerNode()) {
        JsonNode jsonFieldNode;
        boolean isArray;
        // Single-instance node
        if (!jsonField.getValue().has(JSON_SCHEMA_ITEMS)) {
          jsonFieldNode = jsonField.getValue();
          isArray = false;
        }
        // Multi-instance node
        else {
          jsonFieldNode = jsonField.getValue().get(JSON_SCHEMA_ITEMS);
          isArray = true;
        }
        // Field or Element
        if (isTemplateFieldNode(jsonFieldNode) || isTemplateElementNode(jsonFieldNode)) {

          // Get field/element identifier
          String id = null;
          if ((jsonFieldNode.get(LD_ID) != null) && (jsonFieldNode.get(LD_ID).asText().length() > 0)) {
            id = jsonFieldNode.get(LD_ID).asText();
          } else {
            throw (new CedarProcessingException(LD_ID + " not found for template field"));
          }

          // Get name
          String name = null;
          if ((jsonFieldNode.get(SCHEMA_NAME) != null) && (jsonFieldNode.get(SCHEMA_NAME).asText().length() > 0)) {
            name = jsonFieldNode.get(SCHEMA_NAME).asText();
          } else {
            // Do nothing. This field is not required.
          }

          // Get preferred label
          String prefLabel = null;
          if ((jsonFieldNode.get(SKOS_PREFLABEL) != null) && (jsonFieldNode.get(SKOS_PREFLABEL).asText().length() > 0)) {
            prefLabel = jsonFieldNode.get(SKOS_PREFLABEL).asText();
          } else {
            // Do nothing. This field is not required.
          }

          // Add json field path to the results. I create a new list to not modify currentPath
          List<String> jsonFieldPath = new ArrayList<>(currentPath);
          jsonFieldPath.add(jsonFieldKey);

          // Field
          if (isTemplateFieldNode(jsonFieldNode)) {
            // Get instance type (@type) if it exists)
            Optional<String> instanceType = getInstanceType(jsonFieldNode);

            results.add(new TemplateNode(id, name, prefLabel, jsonFieldPath, CedarResourceType.FIELD, isArray));
          }
          // Element
          else if (isTemplateElementNode(jsonFieldNode)) {
            results.add(new TemplateNode(id, name, prefLabel, jsonFieldPath, CedarResourceType.ELEMENT, isArray));
            getTemplateNodes(jsonFieldNode, jsonFieldPath, results);
          }
        }
        // All other nodes
        else {
          getTemplateNodes(jsonFieldNode, currentPath, results);
        }
      }
    }
    return results;
  }

  /**
   * Checks if a Json node corresponds to a CEDAR template field
   *
   * @param node
   * @return
   */
  private boolean isTemplateFieldNode(JsonNode node) {
    if (node.get(LD_TYPE) != null && node.get(LD_TYPE).asText().equals(CedarResourceType.FIELD.getAtType())) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Checks if a Json node corresponds to a CEDAR template element
   *
   * @param node
   * @return
   */
  private boolean isTemplateElementNode(JsonNode node) {
    if (node.get(LD_TYPE) != null && node.get(LD_TYPE).asText().equals(CedarResourceType.ELEMENT.getAtType())) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Returns the instance type of a field node
   *
   * @param fieldNode
   * @return
   */
  private Optional<String> getInstanceType(JsonNode fieldNode) {
    if (isTemplateFieldNode(fieldNode)) {
      if (fieldNode.get(JSON_SCHEMA_PROPERTIES) != null &&
          fieldNode.get(JSON_SCHEMA_PROPERTIES).get(LD_TYPE) != null &&
          fieldNode.get(JSON_SCHEMA_PROPERTIES).get(LD_TYPE).get(JSON_SCHEMA_ONE_OF) != null &&
          fieldNode.get(JSON_SCHEMA_PROPERTIES).get(LD_TYPE).get(JSON_SCHEMA_ONE_OF).size() > 0 &&
          fieldNode.get(JSON_SCHEMA_PROPERTIES).get(LD_TYPE).get(JSON_SCHEMA_ONE_OF).get(0).get(JSON_SCHEMA_ENUM) != null &&
          fieldNode.get(JSON_SCHEMA_PROPERTIES).get(LD_TYPE).get(JSON_SCHEMA_ONE_OF).get(0).get(JSON_SCHEMA_ENUM).size() > 0) {

        return Optional.of(fieldNode.get(JSON_SCHEMA_PROPERTIES).
            get(LD_TYPE).get(JSON_SCHEMA_ONE_OF).get(0).get(JSON_SCHEMA_ENUM).get(0).asText());
      }
    }
    return Optional.empty();
  }

}
