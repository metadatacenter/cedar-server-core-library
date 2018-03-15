package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class ResourceVersion {

  private String value;
  private boolean valid;
  private Integer major;
  private Integer minor;
  private Integer patch;

  private ResourceVersion(String value) {
    this(value, false);
  }

  public ResourceVersion(String value, boolean validate) {
    this.value = value;
    this.valid = false;
    if (validate) {
      validate();
    }
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static ResourceVersion forValue(String value) {
    return new ResourceVersion(value);
  }

  public void validate() {
    if (value != null) {
      String[] split = value.split("\\.");
      if (split.length == 3) {
        major = getInteger(split[0]);
        minor = getInteger(split[1]);
        patch = getInteger(split[2]);
        if (major != null && minor != null && patch != null) {
          if (major > 0 || minor > 0 || patch > 0) {
            valid = true;
          }
        }
      }
    }
  }

  public boolean isValid() {
    return valid;
  }

  public static ResourceVersion forValueWithValidation(String value) {
    return new ResourceVersion(value, true);
  }

  private static Integer getInteger(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

}
