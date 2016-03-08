package org.metadatacenter.server.service.mongodb;

import org.checkerframework.checker.nullness.qual.NonNull;
import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import org.metadatacenter.util.json.JsonUtils;

public class GenericTemplateServiceMongoDB<K, T> {

  // Validation against JSON schema
  public void validate(@NonNull JsonNode schema, @NonNull JsonNode instance) throws ProcessingException {
    JsonUtils jsonUtils = new JsonUtils();
    jsonUtils.validate(schema, instance);
  }
}
