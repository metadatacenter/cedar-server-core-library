package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourceStatusFilter {

  DRAFT("bibo:draft"),
  PUBLISHED("bibo:published"),
  ALL("all");

  private final String value;

  ResourceStatusFilter(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourceStatusFilter forValue(String value) {
    for (ResourceStatusFilter t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }
}