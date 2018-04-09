package org.metadatacenter.util;

import com.fasterxml.jackson.databind.JsonNode;
import org.metadatacenter.model.CedarNodeType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.metadatacenter.model.ModelPaths.*;

public class ModelUtil {

  private static final String specialFieldPattern = "(^@)|(^_)|(^schema:)|(^pav:)|(^oslc:)";

  private ModelUtil() {
  }

  public static boolean isSpecialField(String fieldName) {
    // Create a Pattern object
    Pattern r = Pattern.compile(specialFieldPattern);
    // Now create matcher object.
    Matcher m = r.matcher(fieldName);
    return m.find();
  }

  private static JsonPointerValuePair extractStringFromPointer(JsonNode jsonNode, String
      pointer) {
    JsonPointerValuePair r = new JsonPointerValuePair();
    r.setPointer(pointer);
    JsonNode titleNode = jsonNode.at(r.getPointer());
    if (titleNode != null && !titleNode.isMissingNode()) {
      r.setValue(titleNode.textValue());
    }
    return r;
  }

  public static JsonPointerValuePair extractNameFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, SCHEMA_NAME);
  }

  public static JsonPointerValuePair extractDescriptionFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, SCHEMA_DESCRIPTION);
  }

  public static JsonPointerValuePair extractVersionFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, PAV_VERSION);
  }

  public static JsonPointerValuePair extractStatusFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, BIBO_STATUS);
  }

  public static JsonPointerValuePair extractIsBasedOnFromInstance(JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, PAV_IS_BASED_ON);
  }
}



