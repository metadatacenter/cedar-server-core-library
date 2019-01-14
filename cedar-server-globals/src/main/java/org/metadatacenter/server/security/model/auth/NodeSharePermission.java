package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NodeSharePermission {

  READ(Type.READ),
  WRITE(Type.WRITE),
  NONE(Type.NONE);

  public static class Type {
    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String NONE = null;
  }

  private final String value;

  NodeSharePermission(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public static NodeSharePermission forValue(String type) {
    for (NodeSharePermission t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return NONE;
  }

  public boolean equals(NodeSharePermission other) {
    return other != null &&
        (
            (this.value == null && other.value == null) || (this.value != null && this.value.equals(other.value))
        );
  }
}