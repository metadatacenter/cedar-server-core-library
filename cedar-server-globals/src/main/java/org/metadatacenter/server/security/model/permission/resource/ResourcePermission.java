package org.metadatacenter.server.security.model.permission.resource;

import com.fasterxml.jackson.annotation.JsonValue;

public enum ResourcePermission {

  READ(Type.READ),
  WRITE(Type.WRITE),
  CHANGEOWNER(Type.CHANGEOWNER),
  CHANGEPERMISSIONS(Type.CHANGEPERMISSIONS),
  PUBLISH(Type.PUBLISH),
  CREATE_DRAFT(Type.CREATE_DRAFT);

  public static class Type {
    public static final String READ = "read";
    public static final String WRITE = "write";
    public static final String CHANGEOWNER = "changeowner";
    public static final String CHANGEPERMISSIONS = "changepermissions";
    public static final String PUBLISH = "publish";
    public static final String CREATE_DRAFT = "createdraft";
  }

  private final String value;

  ResourcePermission(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public static ResourcePermission forValue(String type) {
    for (ResourcePermission t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  public boolean equals(ResourcePermission other) {
    return other != null &&
        (
            (this.value == null && other.value == null) || (this.value != null && this.value.equals(other.value))
        );
  }
}
