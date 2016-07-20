package org.metadatacenter.server.neo4j;

import org.metadatacenter.model.CedarNodeType;

public enum NodeLabel {

  FOLDER(ComposedLabels.FOLDER),
  SYSTEM_FOLDER(ComposedLabels.SYSTEM_FOLDER),
  USER_HOME_FOLDER(ComposedLabels.USER_HOME_FOLDER),
  RESOURCE(ComposedLabels.RESOURCE),
  USER(ComposedLabels.USER),
  FIELD(ComposedLabels.FIELD),
  ELEMENT(ComposedLabels.ELEMENT),
  TEMPLATE(ComposedLabels.TEMPLATE),
  INSTANCE(ComposedLabels.INSTANCE);

  public static class PlainLabels {
    public static final String SCOPE = "XCEDAR";

    public static final String FOLDER = "Folder";
    public static final String SYSTEM_FOLDER = "SystemFolder";
    public static final String USER_HOME_FOLDER = "UserHomeFolder";
    public static final String RESOURCE = "Resource";
    public static final String USER = "User";
    public static final String FIELD = "Field";
    public static final String ELEMENT = "Element";
    public static final String TEMPLATE = "Template";
    public static final String INSTANCE = "Instance";
  }

  public static class ComposedLabels {
    public static final String FOLDER = PlainLabels.FOLDER + ":" + PlainLabels.SCOPE;
    public static final String SYSTEM_FOLDER = PlainLabels.SYSTEM_FOLDER + ":" + FOLDER;
    public static final String USER_HOME_FOLDER = PlainLabels.USER_HOME_FOLDER + ":" + SYSTEM_FOLDER;
    public static final String RESOURCE = PlainLabels.RESOURCE + ":" + PlainLabels.SCOPE;
    public static final String USER = PlainLabels.USER + ":" + PlainLabels.SCOPE;
    public static final String FIELD = PlainLabels.FIELD + ":" + RESOURCE;
    public static final String ELEMENT = PlainLabels.ELEMENT + ":" + RESOURCE;
    public static final String TEMPLATE = PlainLabels.TEMPLATE + ":" + RESOURCE;
    public static final String INSTANCE = PlainLabels.INSTANCE + ":" + RESOURCE;
  }

  private final String value;

  NodeLabel(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static NodeLabel forValue(String type) {
    for (NodeLabel t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  public static NodeLabel forCedarNodeType(CedarNodeType nodeType) {
    switch (nodeType) {
      case FIELD:
        return FIELD;
      case ELEMENT:
        return ELEMENT;
      case TEMPLATE:
        return TEMPLATE;
      case INSTANCE:
        return INSTANCE;
    }
    return null;
  }

  @Override
  public String toString() {
    return value;
  }
}