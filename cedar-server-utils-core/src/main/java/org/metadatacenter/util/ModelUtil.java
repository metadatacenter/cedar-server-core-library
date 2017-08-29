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

  public static JsonPointerValuePair extractNameFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    JsonPointerValuePair r = new JsonPointerValuePair();
    if (nodeType.isFielOrElementOrTemplate()) {
      r.setPointer(UI_TITLE);
    } else if (nodeType == CedarNodeType.INSTANCE) {
      r.setPointer(SCHEMA_NAME);
    }
    JsonNode titleNode = jsonNode.at(r.getPointer());
    if (titleNode != null && !titleNode.isMissingNode()) {
      r.setValue(titleNode.textValue());
    }
    return r;
  }

  public static JsonPointerValuePair extractDescriptionFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    JsonPointerValuePair r = new JsonPointerValuePair();
    if (nodeType.isFielOrElementOrTemplate()) {
      r.setPointer(UI_DESCRIPTION);
    } else if (nodeType == CedarNodeType.INSTANCE) {
      r.setPointer(SCHEMA_DESCRIPTION);
    }
    JsonNode descriptionNode = jsonNode.at(r.getPointer());
    if (descriptionNode != null && !descriptionNode.isMissingNode()) {
      r.setValue(descriptionNode.textValue());
    }
    return r;
  }


}



