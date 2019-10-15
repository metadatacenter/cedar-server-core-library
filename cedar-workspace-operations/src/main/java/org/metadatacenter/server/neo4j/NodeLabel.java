package org.metadatacenter.server.neo4j;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.metadatacenter.model.CedarResourceType;

public enum NodeLabel {

  RESOURCE(ComposedLabel.RESOURCE, SimpleLabel.RESOURCE),
  USER(ComposedLabel.USER, SimpleLabel.USER),
  GROUP(ComposedLabel.GROUP, SimpleLabel.GROUP),
  CATEGORY(ComposedLabel.CATEGORY, SimpleLabel.CATEGORY),
  FILESYSTEM_RESOURCE(SimpleLabel.FILESYSTEM_RESOURCE, SimpleLabel.FILESYSTEM_RESOURCE),
  FOLDER(ComposedLabel.FOLDER, SimpleLabel.FOLDER),
  SYSTEM_FOLDER(ComposedLabel.SYSTEM_FOLDER, SimpleLabel.SYSTEM_FOLDER),
  USER_HOME_FOLDER(ComposedLabel.USER_HOME_FOLDER, SimpleLabel.USER_HOME_FOLDER),
  ARTIFACT(ComposedLabel.ARTIFACT, SimpleLabel.ARTIFACT),
  FIELD(ComposedLabel.FIELD, SimpleLabel.FIELD),
  ELEMENT(ComposedLabel.ELEMENT, SimpleLabel.ELEMENT),
  TEMPLATE(ComposedLabel.TEMPLATE, SimpleLabel.TEMPLATE),
  INSTANCE(ComposedLabel.INSTANCE, SimpleLabel.INSTANCE);

  private static final String S = ":";

  public static class SimpleLabel {
    public static final String RESOURCE = "Resource";
    public static final String USER = "User";
    public static final String GROUP = "Group";
    public static final String CATEGORY = "Category";
    public static final String FILESYSTEM_RESOURCE = "FileSystemResource";
    public static final String FOLDER = "Folder";
    public static final String SYSTEM_FOLDER = "SystemFolder";
    public static final String USER_HOME_FOLDER = "UserHomeFolder";
    public static final String ARTIFACT = "Artifact";
    public static final String FIELD = "Field";
    public static final String ELEMENT = "Element";
    public static final String TEMPLATE = "Template";
    public static final String INSTANCE = "Instance";
  }

  public static class ComposedLabel {
    public static final String RESOURCE = SimpleLabel.RESOURCE;
    public static final String USER = RESOURCE + S + SimpleLabel.USER;
    public static final String GROUP = RESOURCE + S + SimpleLabel.GROUP;
    public static final String CATEGORY = RESOURCE + S + SimpleLabel.CATEGORY;
    public static final String FILESYSTEM_RESOURCE = RESOURCE + S + SimpleLabel.FILESYSTEM_RESOURCE;
    public static final String FOLDER = FILESYSTEM_RESOURCE + S + SimpleLabel.FOLDER;
    public static final String SYSTEM_FOLDER = FOLDER + S + SimpleLabel.SYSTEM_FOLDER;
    public static final String USER_HOME_FOLDER = FOLDER + S + SimpleLabel.USER_HOME_FOLDER;
    public static final String ARTIFACT = FILESYSTEM_RESOURCE + S + SimpleLabel.ARTIFACT;
    public static final String FIELD = ARTIFACT + S + SimpleLabel.FIELD;
    public static final String ELEMENT = ARTIFACT + S + SimpleLabel.ELEMENT;
    public static final String TEMPLATE = ARTIFACT + S + SimpleLabel.TEMPLATE;
    public static final String INSTANCE = ARTIFACT + S + SimpleLabel.INSTANCE;
  }

  private final String composedLabel;
  private final String simpleLabel;

  NodeLabel(String composedLabel, String simpleLabel) {
    this.composedLabel = composedLabel;
    this.simpleLabel = simpleLabel;
  }

  public String getComposedLabel() {
    return composedLabel;
  }

  public String getSimpleLabel() {
    return simpleLabel;
  }

  public static NodeLabel forComposedLabel(String composedLabel) {
    for (NodeLabel t : values()) {
      if (t.getComposedLabel().equals(composedLabel)) {
        return t;
      }
    }
    return null;
  }

  public static NodeLabel forSimpleLabel(String simpleLabel) {
    for (NodeLabel t : values()) {
      if (t.getSimpleLabel().equals(simpleLabel)) {
        return t;
      }
    }
    return null;
  }

  public static NodeLabel forCedarResourceType(CedarResourceType resourceType) {
    switch (resourceType) {
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

  @JsonIgnore
  public boolean isFolder() {
    return this == FOLDER || this == SYSTEM_FOLDER || this == USER_HOME_FOLDER;
  }

  @Override
  public String toString() {
    return composedLabel;
  }
}
