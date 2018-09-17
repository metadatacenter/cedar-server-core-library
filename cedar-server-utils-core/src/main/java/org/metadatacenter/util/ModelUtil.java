package org.metadatacenter.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.metadatacenter.constant.CedarConstants;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.server.jsonld.LinkedDataUtil;
import org.metadatacenter.server.model.provenance.ProvenanceInfo;
import org.metadatacenter.util.provenance.ProvenanceUtil;

import java.util.Iterator;
import java.util.Map;
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

  public static JsonPointerValuePair extractAtIdFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, AT_ID);
  }

  public static JsonPointerValuePair extractNameFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, SCHEMA_NAME);
  }

  public static JsonPointerValuePair extractDescriptionFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, SCHEMA_DESCRIPTION);
  }

  public static JsonPointerValuePair extractIdentifierFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, SCHEMA_IDENTIFIER);
  }

  public static JsonPointerValuePair extractVersionFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, PAV_VERSION);
  }

  public static JsonPointerValuePair extractPublicationStatusFromResource(CedarNodeType nodeType, JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, BIBO_STATUS);
  }

  public static JsonPointerValuePair extractIsBasedOnFromInstance(JsonNode jsonNode) {
    return extractStringFromPointer(jsonNode, SCHEMA_IS_BASED_ON);
  }

  public static void ensureFieldIdsRecursively(JsonNode genericInstance, ProvenanceInfo pi, ProvenanceUtil provenanceUtil,
                                        LinkedDataUtil linkedDataUtil) {
    JsonNode properties = genericInstance.get("properties");
    if (properties != null) {
      Iterator<Map.Entry<String, JsonNode>> it = properties.fields();
      while (it.hasNext()) {
        Map.Entry<String, JsonNode> entry = it.next();
        JsonNode fieldCandidate = entry.getValue();
        // If the entry is an object
        if (fieldCandidate.isObject()
            && fieldCandidate.get("type") != null
            && !ModelUtil.isSpecialField(entry.getKey())) {
          String type = fieldCandidate.get("type").asText();
          if ("object".equals(type)) {
            generateFieldIdIfTemporaryOrMissing(fieldCandidate, pi, provenanceUtil, linkedDataUtil);
            // multiple instance
          } else if ("array".equals(type)) {
            generateFieldIdIfTemporaryOrMissing(fieldCandidate.get("items"), pi, provenanceUtil, linkedDataUtil);
          }
        }
      }
    }
  }

  private static void generateFieldIdIfTemporaryOrMissing(JsonNode fieldCandidate, ProvenanceInfo pi, ProvenanceUtil
      provenanceUtil, LinkedDataUtil linkedDataUtil) {
    provenanceUtil.addProvenanceInfo(fieldCandidate, pi);
    if (fieldCandidate.get("@id") != null) {
      String id = fieldCandidate.get("@id").asText();
      if (id == null || id.indexOf(CedarConstants.TEMP_ID_PREFIX) == 0) {
        ((ObjectNode) fieldCandidate).remove("@id");
        ((ObjectNode) fieldCandidate).put("@id", generateNewFieldId(linkedDataUtil));
      }
    } else {
      ((ObjectNode) fieldCandidate).put("@id", generateNewFieldId(linkedDataUtil));
    }
  }

  private static String generateNewFieldId(LinkedDataUtil linkedDataUtil) {
    return linkedDataUtil.buildNewLinkedDataId(CedarNodeType.FIELD);
  }

}



