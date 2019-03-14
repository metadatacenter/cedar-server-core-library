package org.metadatacenter.server.jsonld;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Iterators;
import org.metadatacenter.config.LinkedDataConfig;
import org.metadatacenter.constant.LinkedData;
import org.metadatacenter.model.CedarNodeType;

import java.util.*;

public class LinkedDataUtil {

  protected static final String SEPARATOR = "/";

  private final LinkedDataConfig ldConfig;
  private List<String> knownPrefixes;

  public LinkedDataUtil(LinkedDataConfig ldConfig) {
    this.ldConfig = ldConfig;
    knownPrefixes = new ArrayList<>();
    for (CedarNodeType nt : CedarNodeType.values()) {
      knownPrefixes.add(getLinkedDataPrefix(nt));
    }
  }

  private String getLinkedDataPrefix(CedarNodeType nodeType) {
    if (nodeType == CedarNodeType.USER) {
      return ldConfig.getUsersBase();
    } else {
      return ldConfig.getBase() + nodeType.getPrefix() + SEPARATOR;
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
    if (resourceId != null) {
      int pos = resourceId.lastIndexOf(SEPARATOR);
      return resourceId.substring(pos + 1);
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
          if (!field.getKey().equals(LinkedData.CONTEXT)) {
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
      if (!fieldContent.has(LinkedData.VALUE) && !fieldContent.has(LinkedData.ID)) {
        if ((!fieldContent.has(LinkedData.TYPE) && Iterators.size(fieldContent.elements()) > 0) || (fieldContent.has
            (LinkedData.TYPE) && Iterators.size(fieldContent.elements()) > 1)) {
          String id = buildNewLinkedDataId(CedarNodeType.ELEMENT_INSTANCE);
          ((ObjectNode) fieldContent).put(LinkedData.ID, id);
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

  public boolean isValidId(String id) {
    String uuid = null;
    if (id != null) {
      for (String prefix : knownPrefixes) {
        if (uuid == null && id.startsWith(prefix)) {
          uuid = id.substring(prefix.length());
        }
      }
    }
    if (uuid != null) {
      return uuid.trim().length() > 0;
    }
    return false;
  }
}
