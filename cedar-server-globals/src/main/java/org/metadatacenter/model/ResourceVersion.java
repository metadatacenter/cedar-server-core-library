package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class ResourceVersion {

  private String value;

  public ResourceVersion(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourceVersion forValue(String value) {
    return new ResourceVersion(value);
  }

}
