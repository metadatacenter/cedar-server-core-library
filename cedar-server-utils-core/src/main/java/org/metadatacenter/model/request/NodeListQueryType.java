package org.metadatacenter.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum NodeListQueryType {

  VIEW_SHARED_WITH_ME("view-shared-with-me"),
  VIEW_ALL("view-all"),
  SEARCH_TERM("search-term"),
  SEARCH_IS_BASED_ON("search-is-based-on"),
  FOLDER_CONTENT("folder-content"),
  ALL_NODES("all-nodes"),
  UNKNOWN("unknown");

  private final String value;

  NodeListQueryType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static NodeListQueryType forValue(String value) {
    for (NodeListQueryType t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return UNKNOWN;
  }
}
