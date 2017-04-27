package org.metadatacenter.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import org.metadatacenter.model.CedarNodeType;

public enum ResourceType {

  TEMPLATE(CedarNodeType.Types.TEMPLATE),
  ELEMENT(CedarNodeType.Types.ELEMENT),
  FIELD(CedarNodeType.Types.FIELD),
  INSTANCE(CedarNodeType.Types.INSTANCE);

  private final String value;

  ResourceType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourceType forValue(String value) {
    ResourceType resourceType = null;
    for (ResourceType t : values()) {
      if (t.getValue().equals(value)) {
        resourceType = t;
      }
    }
    return resourceType;
  }
}
