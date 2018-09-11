package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class ResourceVersion implements Comparable<ResourceVersion> {

  private String value;
  private boolean valid;
  private Integer major;
  private Integer minor;
  private Integer patch;

  public static ResourceVersion ZERO_ZERO_ONE = ResourceVersion.forValue("0.0.1");

  private ResourceVersion(String value, boolean validate) {
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
    return ResourceVersion.forValueWithoutValidation(value);
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

  public static ResourceVersion forValueWithoutValidation(String value) {
    return new ResourceVersion(value, false);
  }

  private static Integer getInteger(String s) {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  @Override
  public int compareTo(ResourceVersion o) {
    if (o == null) {
      return 1;
    }
    if (!o.isValid()) {
      return 1;
    } else if (!this.isValid()) {
      return -1;
    }
    if (this.major != o.major) {
      return this.major - o.major;
    } else if (this.minor != o.minor) {
      return this.minor - o.minor;
    } else if (this.patch != o.patch) {
      return this.patch - o.patch;
    } else {
      return 0;
    }
  }

  public boolean isBefore(ResourceVersion o) {
    return this.compareTo(o) < 0;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ResourceVersion && this.value != null) {
      ResourceVersion other = (ResourceVersion) obj;
      return this.valid == other.valid && this.value.equals(other.value) && this.major == other.major &&
          this.minor == other.minor && this.patch == other.patch;
    }
    return false;
  }
}
