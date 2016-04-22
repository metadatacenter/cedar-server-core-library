package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.String;

public enum CedarResourceType {

  FOLDER(Types.FOLDER),
  FIELD(Types.FIELD),
  ELEMENT(Types.ELEMENT),
  TEMPLATE(Types.TEMPLATE),
  INSTANCE(Types.INSTANCE);


  public static class Types {
    public static final String FOLDER = "folder";
    public static final String FIELD = "field";
    public static final String ELEMENT = "element";
    public static final String TEMPLATE = "template";
    public static final String INSTANCE = "instance";
  }

  private final String value;

  CedarResourceType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public static CedarResourceType forValue(String s) {
    for (CedarResourceType t : values()) {
      if (t.getValue().equals(s)) {
        return t;
      }
    }
    return null;
  }
}