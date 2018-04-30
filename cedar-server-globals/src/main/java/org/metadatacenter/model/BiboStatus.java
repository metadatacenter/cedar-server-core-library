package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum BiboStatus {

  DRAFT("bibo:draft"),
  PUBLISHED("bibo:published");

  private final String value;

  BiboStatus(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static BiboStatus forValue(String value) {
    for (BiboStatus t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }
}