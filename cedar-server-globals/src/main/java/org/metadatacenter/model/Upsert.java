package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Upsert {

  UPDATE("update"),
  INSERT("insert");

  private final String value;

  Upsert(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static Upsert forValue(String value) {
    for (Upsert t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }
}