package org.metadatacenter.util.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.util.mongo.FixMongoDirection;

import java.util.Iterator;
import java.util.Map;

import static org.metadatacenter.model.ModelNodeNames.SCHEMA_IS_BASED_ON;

public class JsonUtils {

  /* Fix for the keywords not allowed by MongoDB (e.g. $schema) */
  // Rename JSON field to be stored into MongoDB
  // direction: WRITE_TO_MONGO  -> update field names for MongoDB storage (e.g. $schema -> _$schema)
  // direction: READ_FROM_MONGO -> update field names after reading them from MongoDB (e.g. _$schema -> $schema)
  public JsonNode fixMongoDB(JsonNode node, FixMongoDirection direction) {
    boolean reverse = false;
    if (direction == FixMongoDirection.READ_FROM_MONGO) {
      reverse = true;
    }
    updateFieldName(node, "$schema", "_$schema", reverse);
    updateFieldName(node, "$oid", "_$oid", reverse);
    updateFieldName(node, "$numberLong", "_$numberLong", reverse);
    // Now, recursively invoke this method on all properties
    for (JsonNode child : node) {
      fixMongoDB(child, direction);
    }
    return node;
  }

  private JsonNode updateFieldName(JsonNode node, String fieldName, String newFieldName,
                                   boolean reverse) {
    if (reverse) {
      String swap = fieldName;
      fieldName = newFieldName;
      newFieldName = swap;
    }
    if (node.has(fieldName)) {
      ((ObjectNode) node).set(newFieldName, new TextNode(node.get(fieldName).asText()));
      ((ObjectNode) node).remove(fieldName);
    }
    return node;
  }

  // Remove a particular field from a JsonNode object
  public static JsonNode removeField(JsonNode node, String fieldName) {
    ObjectNode object = (ObjectNode) node;
    object.remove(fieldName);
    return object;
  }

  // TODO: move the below 5 functions into a separate class, all of them are dealing with id replacement
  public static JsonNode localizeAtIdsAndTemplateId(JsonNode node, LinkedDataUtil
      linkedDataUtil) {
    ObjectNode object = (ObjectNode) node;
    localizeFieldValueAsId(object, SCHEMA_IS_BASED_ON, linkedDataUtil, CedarNodeType.TEMPLATE);
    localizeAtIdRecursively(object, linkedDataUtil);
    return object;
  }

  private static void localizeAtIdRecursively(ObjectNode object, LinkedDataUtil linkedDataUtil) {
    String atType = getKeyValueIfString(object, "@type");
    CedarNodeType nodeType = CedarNodeType.forAtType(atType);
    if (nodeType != null) {
      localizeFieldValueAsId(object, "@id", linkedDataUtil, nodeType);
      Iterator<Map.Entry<String, JsonNode>> fields = object.fields();
      while (fields.hasNext()) {
        Map.Entry<String, JsonNode> entry = fields.next();
        JsonNode child = entry.getValue();
        if (child != null) {
          if (child.isObject()) {
            localizeAtIdRecursively((ObjectNode) child, linkedDataUtil);
          } else if (child.isArray()) {
            localizeAtIdRecursivelyInArray((ArrayNode) child, linkedDataUtil);
          }
        }
      }
    }
  }

  private static void localizeAtIdRecursivelyInArray(ArrayNode array, LinkedDataUtil linkedDataUtil) {
    for (int i = 0; i < array.size(); i++) {
      JsonNode element = array.get(i);
      if (element.isObject()) {
        localizeAtIdRecursively((ObjectNode) element, linkedDataUtil);
      } else if (element.isArray()) {
        localizeAtIdRecursivelyInArray((ArrayNode) element, linkedDataUtil);
      }
    }
  }

  public static String getKeyValueIfString(ObjectNode object, String key) {
    JsonNode jsonNode = object.get(key);
    if (jsonNode != null) {
      if (jsonNode.isTextual()) {
        return jsonNode.asText();
      }
    }
    return null;
  }

  private static void localizeFieldValueAsId(ObjectNode object, String fieldName, LinkedDataUtil
      linkedDataUtil, CedarNodeType nodeType) {
    String v = getKeyValueIfString(object, fieldName);
    if (v != null) {
      int lastPos = v.lastIndexOf('/');
      if (lastPos != -1) {
        String uuid = v.substring(lastPos + 1);
        object.put(fieldName, linkedDataUtil.getLinkedDataId(nodeType, uuid));
      }
    }
  }
}
