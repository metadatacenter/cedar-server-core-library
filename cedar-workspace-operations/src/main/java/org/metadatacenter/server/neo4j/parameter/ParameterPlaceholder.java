package org.metadatacenter.server.neo4j.parameter;

import org.metadatacenter.server.neo4j.cypher.CypherQueryParameter;

public enum ParameterPlaceholder implements CypherQueryParameter {

  FOLDER_ID("folderId"),
  PARENT_FOLDER_ID("parentFolderId"),
  RESOURCE_ID("resourceId"),
  NODE_ID("nodeId"),
  GROUP_ID("groupId"),
  PARENT_ID("parentId"),
  USER_ID("userId"),
  FROM_ID("fromId"),
  TO_ID("toId"),
  NODE_TYPE_LIST("nodeTypeList"),
  LIMIT("limit"),
  OFFSET("offset"),
  SOURCE_ID("sourceId"),
  TARGET_ID("targetId"),
  IS_BASED_ON("isBasedOn");

  private final String value;

  ParameterPlaceholder(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

  public static ParameterPlaceholder forValue(String type) {
    for (ParameterPlaceholder t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return value;
  }
}