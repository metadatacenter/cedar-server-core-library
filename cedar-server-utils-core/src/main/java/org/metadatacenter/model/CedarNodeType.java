package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.String;

public enum CedarNodeType {

  FOLDER(Types.FOLDER, Prefix.FOLDERS),
  FIELD(Types.FIELD, Prefix.FIELDS),
  ELEMENT(Types.ELEMENT, Prefix.ELEMENTS),
  TEMPLATE(Types.TEMPLATE, Prefix.TEMPLATES),
  INSTANCE(Types.INSTANCE, Prefix.INSTANCES);

  public static class Types {
    public static final String FOLDER = "folder";
    public static final String FIELD = "field";
    public static final String ELEMENT = "element";
    public static final String TEMPLATE = "template";
    public static final String INSTANCE = "instance";
  }

  public static class Prefix {
    public static final String FOLDERS = "folders";
    public static final String FIELDS = "template-fields";
    public static final String ELEMENTS = "template-elements";
    public static final String TEMPLATES = "templates";
    public static final String INSTANCES = "template-instances";
  }

  private final String value;
  private final String prefix;

  CedarNodeType(String value, String prefix) {
    this.value = value;
    this.prefix = prefix;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public String getPrefix() {
    return prefix;
  }

  public static CedarNodeType forValue(String type) {
    for (CedarNodeType t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }
}