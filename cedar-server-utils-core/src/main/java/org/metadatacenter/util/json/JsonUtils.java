package org.metadatacenter.util.json;

import checkers.nullness.quals.NonNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import org.metadatacenter.util.FixMongoDirection;

import java.io.IOException;
import java.io.StringWriter;

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
}
