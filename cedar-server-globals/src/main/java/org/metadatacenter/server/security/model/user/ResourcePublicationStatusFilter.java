package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourcePublicationStatusFilter {

  DRAFT("bibo:draft"),
  PUBLISHED("bibo:published"),
  ALL("all");

  private final String value;

  ResourcePublicationStatusFilter(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourcePublicationStatusFilter forValue(String value) {
    for (ResourcePublicationStatusFilter t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }
}