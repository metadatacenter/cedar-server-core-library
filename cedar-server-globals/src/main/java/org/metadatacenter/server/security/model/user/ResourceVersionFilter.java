package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceVersionFilter {

  LATEST("latest"),
  ALL("all");

  private final String value;

  ResourceVersionFilter(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourceVersionFilter forValue(String value) {
    for (ResourceVersionFilter t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }
}