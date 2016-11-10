package org.metadatacenter.model;

import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.String;

public enum CedarNodeType {

  FOLDER(Types.FOLDER, Prefix.FOLDERS, null),
  FIELD(Types.FIELD, Prefix.FIELDS, AtType.FIELD),
  ELEMENT(Types.ELEMENT, Prefix.ELEMENTS, AtType.ELEMENT),
  TEMPLATE(Types.TEMPLATE, Prefix.TEMPLATES, AtType.TEMPLATE),
  INSTANCE(Types.INSTANCE, Prefix.INSTANCES, null),
  USER(Types.USER, Prefix.USERS, null),
  GROUP(Types.GROUP, Prefix.GROUPS, null);

  public static class Types {
    public static final String FOLDER = "folder";
    public static final String FIELD = "field";
    public static final String ELEMENT = "element";
    public static final String TEMPLATE = "template";
    public static final String INSTANCE = "instance";
    public static final String USER = "user";
    public static final String GROUP = "group";
  }

  public static class Prefix {
    public static final String FOLDERS = "folders";
    public static final String FIELDS = "template-fields";
    public static final String ELEMENTS = "template-elements";
    public static final String TEMPLATES = "templates";
    public static final String INSTANCES = "template-instances";
    public static final String USERS = "users";
    public static final String GROUPS = "groups";
  }

  public static class AtType {
    public static final String AT_TYPE_PREFIX = "https://schema.metadatacenter.org/core/";
    public static final String FIELD = AT_TYPE_PREFIX + "TemplateField";
    public static final String ELEMENT = AT_TYPE_PREFIX + "TemplateElement";
    public static final String TEMPLATE = AT_TYPE_PREFIX + "Template";
  }

  private final String value;
  private final String prefix;
  private final String atType;

  CedarNodeType(String value, String prefix, String atType) {
    this.value = value;
    this.prefix = prefix;
    this.atType = atType;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getAtType() {
    return atType;
  }

  public static CedarNodeType forValue(String type) {
    for (CedarNodeType t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  public static CedarNodeType forAtType(String atType) {
    if (atType != null) {
      for (CedarNodeType t : values()) {
        if (atType.equals(t.getAtType())) {
          return t;
        }
      }
    }
    return null;
  }
}