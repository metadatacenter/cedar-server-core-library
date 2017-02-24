package org.metadatacenter.util.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.util.mongo.FixMongoDirection;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;
import java.util.Map;

import static org.metadatacenter.constant.CedarConstants.SCHEMA_IS_BASED_ON;

public class JsonUtils {

  /* JSON Schema Validation */
  public void validate(@NonNull JsonNode schema, @NonNull JsonNode instance) throws ProcessingException {
    JsonValidator validator = JsonSchemaFactory.byDefault().getValidator();
    ProcessingReport report = validator.validate(schema, instance);
    if (!report.isSuccess()) {
      throw new RuntimeException("JSON Schema validation failed");
    }
  }

  /* Fix for the keywords not allowed by MongoDB (e.g. $schema) */
  // Rename JSON field to be stored into MongoDB
  // direction: WRITE_TO_MONGO  -> update field names for MongoDB storage (e.g. $schema -> _$schema)
  // direction: READ_FROM_MONGO -> update field names after reading them from MongoDB (e.g. _$schema -> $schema)
  @NonNull
  public JsonNode fixMongoDB(@NonNull JsonNode node, FixMongoDirection direction) {
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

  @NonNull
  private JsonNode updateFieldName(@NonNull JsonNode node, @NonNull String fieldName, @NonNull String newFieldName,
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
  public static @NonNull JsonNode removeField(@NonNull JsonNode node, @NonNull String fieldName) {
    ObjectNode object = (ObjectNode) node;
    object.remove(fieldName);
    return object;
  }

  public static String prettyPrint(Object o) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.INDENT_OUTPUT);
    StringWriter sw = new StringWriter();
    mapper.writeValue(sw, o);
    return sw.toString();
  }

  // TODO: move the below 5 functions into a separate class, all of them are dealing with id replacement
  public static @NonNull JsonNode localizeAtIdsAndTemplateId(@NonNull JsonNode node, @NonNull LinkedDataUtil
      linkedDataUtil) {
    ObjectNode object = (ObjectNode) node;
    localizeFieldValueAsId(object, SCHEMA_IS_BASED_ON, linkedDataUtil, CedarNodeType.TEMPLATE);
    localizeAtIdRecursively(object, linkedDataUtil);
    return object;
  }

  private static void localizeAtIdRecursively(ObjectNode object, @NonNull LinkedDataUtil linkedDataUtil) {
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

  private static void localizeAtIdRecursivelyInArray(ArrayNode array, @NonNull LinkedDataUtil linkedDataUtil) {
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

  private static void localizeFieldValueAsId(ObjectNode object, String fieldName, @NonNull LinkedDataUtil
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
