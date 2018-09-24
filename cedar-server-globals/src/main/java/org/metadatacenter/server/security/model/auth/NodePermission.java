package org.metadatacenter.server.security.model.auth;

import com.fasterxml.jackson.annotation.JsonValue;

public enum NodePermission {

  READ(Type.READ),
  READ_THIS(Type.READ_THIS),
  WRITE(Type.WRITE),
  DELETE(Type.DELETE),
  CHANGE_OWNER(Type.CHANGE_OWNER),
  CHANGE_PERMISSIONS(Type.CHANGE_PERMISSIONS),
  PUBLISH(Type.PUBLISH),
  CREATE_DRAFT(Type.CREATE_DRAFT),
  SUBMIT(Type.SUBMIT);

  public static class Type {
    public static final String READ = "read";
    public static final String READ_THIS = "read-this";
    public static final String WRITE = "write";
    public static final String DELETE = "delete";
    public static final String CHANGE_OWNER = "change-owner";
    public static final String CHANGE_PERMISSIONS = "change-permissions";
    public static final String PUBLISH = "publish";
    public static final String CREATE_DRAFT = "create-draft";
    public static final String SUBMIT = "submit";
  }

  private final String value;

  NodePermission(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public static NodePermission forValue(String type) {
    for (NodePermission t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }
}