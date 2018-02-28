package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IsSystem {
  TRUE(true), FALSE(false);

  private final boolean value;

  IsSystem(boolean value) {
    this.value = value;
  }

  @JsonValue
  public boolean getValue() {
    return value;
  }

  @JsonCreator
  public static IsSystem forValue(boolean value) {
    for (IsSystem t : values()) {
      if (t.getValue() == value) {
        return t;
      }
    }
    return null;
  }
}
