package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum IsUserHome {
  TRUE(true), FALSE(false);

  private final boolean value;

  IsUserHome(boolean value) {
    this.value = value;
  }

  @JsonValue
  public boolean getValue() {
    return value;
  }

  @JsonCreator
  public static IsUserHome forValue(boolean value) {
    for (IsUserHome t : values()) {
      if (t.getValue() == value) {
        return t;
      }
    }
    return null;
  }
}
