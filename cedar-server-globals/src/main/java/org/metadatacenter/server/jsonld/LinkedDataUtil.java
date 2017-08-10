package org.metadatacenter.server.jsonld;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterators;
import org.metadatacenter.config.LinkedDataConfig;
import org.metadatacenter.model.CedarNodeType;
import org.metadatacenter.util.json.JsonMapper;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public class LinkedDataUtil {

  private final LinkedDataConfig ldConfig;
  final String ID_FIELD = "@id";
  final String VALUE_FIELD = "@value";
  final String CONTEXT_FIELD = "@context";
  final String TYPE_FIELD = "@type";

  public LinkedDataUtil(LinkedDataConfig ldConfig) {
    this.ldConfig = ldConfig;
  }

  private String getLinkedDataPrefix(CedarNodeType nodeType) {
    if (nodeType == CedarNodeType.USER) {
      return ldConfig.getUsersBase();
    } else {
      return ldConfig.getBase() + nodeType.getPrefix() + "/";
    }
  }

  public String getUserId(String uuid) {
    return getLinkedDataId(CedarNodeType.USER, uuid);
  }

  public String getLinkedDataId(CedarNodeType nodeType, String uuid) {
    return getLinkedDataPrefix(nodeType) + uuid;
  }

  public String buildNewLinkedDataId(CedarNodeType nodeType) {
    return getLinkedDataId(nodeType, UUID.randomUUID().toString());
  }

  public String getUUID(String resourceId, CedarNodeType nodeType) {
    String prefix = getLinkedDataPrefix(nodeType);
    if (resourceId != null && resourceId.startsWith(prefix)) {
      return resourceId.substring(prefix.length());
    } else {
      return null;
    }
  }

  /**
   * Adds template element instance @id's to the instance if necessary
   */
  public void addElementInstanceIds(JsonNode nodeContent, CedarNodeType nodeType) {
    if (nodeType.equals(CedarNodeType.INSTANCE)) { // this is just a check to avoid running it for other node types
      Iterator<Map.Entry<String, JsonNode>> fieldsIterator = nodeContent.fields();
      while (fieldsIterator.hasNext()) {
        Map.Entry<String, JsonNode> field = fieldsIterator.next();
        if (field.getValue().isContainerNode()) {
          if (!field.getKey().equals(CONTEXT_FIELD)) {
            addElementInstanceIdsToPotentialElementInstance(field.getValue());
          }
        }
      }
    }
  }

  private void addElementInstanceIdsToPotentialElementInstance(JsonNode fieldContent) {
    // Single value
    if (fieldContent.isObject()) {
      // Check that it is an element instance
      if (!fieldContent.has(VALUE_FIELD) && !fieldContent.has(ID_FIELD)) {
        if ((!fieldContent.has(TYPE_FIELD) && Iterators.size(fieldContent.elements()) > 0) || (fieldContent.has(TYPE_FIELD) && Iterators.size(fieldContent.elements()) > 1)) {
          String id = buildNewLinkedDataId(CedarNodeType.ELEMENT_INSTANCE);
          ((ObjectNode) fieldContent).put(ID_FIELD, id);
          addElementInstanceIds(fieldContent, CedarNodeType.INSTANCE);
        }
      }
    }
    // it is an Array (Multi-instance value)
    else if (fieldContent.isArray()) {
      for (int i = 0; i < fieldContent.size(); i++) {
        addElementInstanceIdsToPotentialElementInstance(fieldContent.get(i));
      }
    }
  }
}