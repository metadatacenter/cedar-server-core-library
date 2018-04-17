package org.metadatacenter.server.neo4j.cypher;

public enum NodeProperty implements CypherQueryParameter {

  ID("id"),
  LAST_UPDATED_ON_TS("lastUpdatedOnTS"),
  LAST_UPDATED_ON("lastUpdatedOn"),
  LAST_UPDATED_BY("lastUpdatedBy"),
  CREATED_ON_TS("createdOnTS"),
  CREATED_ON("createdOn"),
  CREATED_BY("createdBy"),
  DESCRIPTION("description"),
  OWNED_BY("ownedBy"),
  NAME("name"),
  FIRST_NAME("firstName"),
  LAST_NAME("lastName"),
  EMAIL("email"),
  NODE_TYPE("nodeType"),
  VERSION("version"),
  PUBLICATION_STATUS("publicationStatus"),
  SPECIAL_GROUP("specialGroup"),
  IS_ROOT("isRoot"),
  IS_SYSTEM("isSystem"),
  IS_USER_HOME("isUserHome"),
  NODE_SORT_ORDER("nodeSortOrder"),
  HOME_OF("homeOf"),
  PREVIOUS_VERSION("previousVersion"),
  IS_LATEST_VERSION("isLatestVersion"),
  DERIVED_FROM("derivedFrom");

  private final String value;

  NodeProperty(String value) {
    this.value = value;
  }

  @Override
  public String getValue() {
    return value;
  }

  public static NodeProperty forValue(String type) {
    for (NodeProperty t : values()) {
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