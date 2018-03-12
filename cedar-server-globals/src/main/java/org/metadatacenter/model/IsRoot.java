package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IsRoot {
  TRUE(true), FALSE(false);

  private final boolean value;

  IsRoot(boolean value) {
    this.value = value;
  }

  @JsonValue
  public boolean getValue() {
    return value;
  }

  @JsonCreator
  public static IsRoot forValue(boolean value) {
    for (IsRoot t : values()) {
      if (t.getValue() == value) {
        return t;
      }
    }
    return null;
  }
}
