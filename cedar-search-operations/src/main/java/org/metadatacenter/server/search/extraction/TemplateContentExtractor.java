package org.metadatacenter.server.search.extraction;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.exception.CedarProcessingException;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.search.extraction.model.TemplateNode;

import java.util.*;

public class TemplateContentExtractor {

  // TODO: Move to constants file
  private final String ID_FIELD_NAME = "@id";
  private final String TYPE_FIELD_NAME = "@type";
  private final String ITEMS_FIELD_NAME = "items";
  private final String PROPERTIES_FIELD_NAME = "properties";
  private final String ONEOF_FIELD_NAME = "oneOf";
  private final String ENUM_FIELD_NAME = "enum";
  private final String NAME_FIELD_NAME = "schema:name";
  private final String PREF_LABEL_FIELD_NAME = "skos:prefLabel";

  public List<TemplateNode> getTemplateNodes(JsonNode template) throws CedarProcessingException {
    return getTemplateNodes(template, null, null);
  }

  /**
   * Returns summary information of all template nodes in the template, considering that a template node may be
   * either a template element or a template field.
   *
   * @param template
   * @param currentPath Used internally to store the current node path
   * @param results     Used internally to store the results
   * @return A list all template elements and fields in the template, represented using the TemplateNode class
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
        if (!jsonField.getValue().has(ITEMS_FIELD_NAME)) {
          jsonFieldNode = jsonField.getValue();
          isArray = false;
        }
        // Multi-instance node
        else {
          jsonFieldNode = jsonField.getValue().get(ITEMS_FIELD_NAME);
          isArray = true;
        }
        // Field or Element
        if (isTemplateFieldNode(jsonFieldNode) || isTemplateElementNode(jsonFieldNode)) {

          // Get field/element identifier
          String id = null;
          if ((jsonFieldNode.get(ID_FIELD_NAME) != null) && (jsonFieldNode.get(ID_FIELD_NAME).asText().length() > 0)) {
            id = jsonFieldNode.get(ID_FIELD_NAME).asText();
          } else {
            throw (new CedarProcessingException(ID_FIELD_NAME + " not found for template field"));
          }

          // Get name
          String name = null;
          if ((jsonFieldNode.get(NAME_FIELD_NAME) != null) && (jsonFieldNode.get(NAME_FIELD_NAME).asText().length() > 0)) {
            name = jsonFieldNode.get(NAME_FIELD_NAME).asText();
          }
          else {
            // Do nothing. This field is not required.
          }

          // Get preferred label
          String prefLabel = null;
          if ((jsonFieldNode.get(PREF_LABEL_FIELD_NAME) != null) && (jsonFieldNode.get(PREF_LABEL_FIELD_NAME).asText().length() > 0)) {
            prefLabel = jsonFieldNode.get(PREF_LABEL_FIELD_NAME).asText();
          }
          else {
            // Do nothing. This field is not required.
          }

          // Add json field path to the results. I create a new list to not modify currentPath
          List<String> jsonFieldPath = new ArrayList<>(currentPath);
          jsonFieldPath.add(jsonFieldKey);

          // Field
          if (isTemplateFieldNode(jsonFieldNode)) {
            // Get instance type (@type) if it exists)
            Optional<String> instanceType = getInstanceType(jsonFieldNode);

            results.add(new TemplateNode(id, name, prefLabel, jsonFieldPath, CedarNodeType.FIELD, instanceType, isArray));
          }
          // Element
          else if (isTemplateElementNode(jsonFieldNode)) {
            results.add(new TemplateNode(id, name, prefLabel, jsonFieldPath, CedarNodeType.ELEMENT, Optional.empty(),
                isArray));
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
  public boolean isTemplateFieldNode(JsonNode node) {
    if (node.get(TYPE_FIELD_NAME) != null && node.get(TYPE_FIELD_NAME).asText().equals(CedarNodeType.FIELD.getAtType
        ())) {
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
  public boolean isTemplateElementNode(JsonNode node) {
    if (node.get(TYPE_FIELD_NAME) != null && node.get(TYPE_FIELD_NAME).asText().equals(CedarNodeType.ELEMENT
        .getAtType())) {
      return true;
    } else {
      return false;
    }
  }

  /**
   * Checks if a map contains a valid String value
   *
   * @param map
   * @param key
   * @return
   */
  public static boolean containsValidValue(Map map, String key) {
    if (map.containsKey(key) && map.get(key) != null && map.get(key).toString().trim().length() > 0) {
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
  public Optional<String> getInstanceType(JsonNode fieldNode) {
    if (isTemplateFieldNode(fieldNode)) {
      if (fieldNode.get(PROPERTIES_FIELD_NAME) != null &&
          fieldNode.get(PROPERTIES_FIELD_NAME).get(TYPE_FIELD_NAME) != null &&
          fieldNode.get(PROPERTIES_FIELD_NAME).get(TYPE_FIELD_NAME).get(ONEOF_FIELD_NAME) != null &&
          fieldNode.get(PROPERTIES_FIELD_NAME).get(TYPE_FIELD_NAME).get(ONEOF_FIELD_NAME).size() > 0 &&
          fieldNode.get(PROPERTIES_FIELD_NAME).get(TYPE_FIELD_NAME).get(ONEOF_FIELD_NAME).get(0).get(ENUM_FIELD_NAME) != null &&
          fieldNode.get(PROPERTIES_FIELD_NAME).get(TYPE_FIELD_NAME).get(ONEOF_FIELD_NAME).get(0).get(ENUM_FIELD_NAME).size() > 0) {

        return Optional.of(fieldNode.get(PROPERTIES_FIELD_NAME).
            get(TYPE_FIELD_NAME).get(ONEOF_FIELD_NAME).get(0).get(ENUM_FIELD_NAME).get(0).asText());
      }
    }
    return Optional.empty();
  }

  /**
   * Basic test to check if a string corresponds to a URI (just for http and https)
   *
   * @return
   */
  public static boolean isUri(String value) {
    value = value.toLowerCase();
    if (value.startsWith("http://") || value.startsWith("https://")) {
      return true;
    } else {
      return false;
    }
  }

}
