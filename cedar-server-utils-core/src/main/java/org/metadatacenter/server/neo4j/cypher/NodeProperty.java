package org.metadatacenter.server.neo4j.cypher;

public enum NodeProperty implements CypherQueryParameter {

  ID(Label.ID),
  LAST_UPDATED_ON_TS(Label.LAST_UPDATED_ON_TS),
  LAST_UPDATED_ON(Label.LAST_UPDATED_ON),
  LAST_UPDATED_BY(Label.LAST_UPDATED_BY),
  CREATED_ON_TS(Label.CREATED_ON_TS),
  CREATED_ON(Label.CREATED_ON),
  CREATED_BY(Label.CREATED_BY),
  OWNED_BY(Label.OWNED_BY),
  NAME(Label.NAME),
  IDENTIFIER(Label.IDENTIFIER),
  DESCRIPTION(Label.DESCRIPTION),
  FIRST_NAME(Label.FIRST_NAME),
  LAST_NAME(Label.LAST_NAME),
  EMAIL(Label.EMAIL),
  NODE_TYPE(Label.NODE_TYPE),
  VERSION(Label.VERSION),
  PUBLICATION_STATUS(Label.PUBLICATION_STATUS),
  SPECIAL_GROUP(Label.SPECIAL_GROUP),
  IS_ROOT(Label.IS_ROOT),
  IS_SYSTEM(Label.IS_SYSTEM),
  IS_USER_HOME(Label.IS_USER_HOME),
  NODE_SORT_ORDER(Label.NODE_SORT_ORDER),
  HOME_OF(Label.HOME_OF),
  PREVIOUS_VERSION(Label.PREVIOUS_VERSION),
  IS_LATEST_VERSION(Label.IS_LATEST_VERSION),
  IS_LATEST_PUBLISHED_VERSION(Label.IS_LATEST_PUBLISHED_VERSION),
  IS_LATEST_DRAFT_VERSION(Label.IS_LATEST_DRAFT_VERSION),
  IS_BASED_ON(Label.IS_BASED_ON),
  DERIVED_FROM(Label.DERIVED_FROM);

  public static class Label {
    public static final String ID ="@id";
    public static final String LAST_UPDATED_ON_TS = "lastUpdatedOnTS";
    public static final String LAST_UPDATED_ON = "pav:lastUpdatedOn";
    public static final String LAST_UPDATED_BY = "oslc:modifiedBy";
    public static final String CREATED_ON_TS = "createdOnTS";
    public static final String CREATED_ON = "pav:createdOn";
    public static final String CREATED_BY = "pav:createdBy";
    public static final String DESCRIPTION = "schema:description";
    public static final String IDENTIFIER = "schema:identifier";
    public static final String OWNED_BY = "ownedBy";
    public static final String NAME = "schema:name";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String EMAIL = "email";
    public static final String NODE_TYPE = "nodeType";
    public static final String VERSION = "pav:version";
    public static final String PUBLICATION_STATUS = "bibo:status";
    public static final String SPECIAL_GROUP = "specialGroup";
    public static final String IS_ROOT = "isRoot";
    public static final String IS_SYSTEM = "isSystem";
    public static final String IS_USER_HOME = "isUserHome";
    public static final String NODE_SORT_ORDER = "nodeSortOrder";
    public static final String HOME_OF = "homeOf";
    public static final String PREVIOUS_VERSION = "pav:previousVersion";
    public static final String IS_LATEST_VERSION = "isLatestVersion";
    public static final String IS_LATEST_DRAFT_VERSION = "isLatestDraftVersion";
    public static final String IS_LATEST_PUBLISHED_VERSION = "isLatestPublishedVersion";
    public static final String IS_BASED_ON = "schema:isBasedOn";
    public static final String DERIVED_FROM = "pav:derivedFrom";
  }

  public class OnTheFly {
    public static final String OWNED_BY_USER_NAME = "ownedByUserName";
    public static final String CREATED_BY_USER_NAME = "createdByUserName";
    public static final String LAST_UPDATED_BY_USER_NAME = "lastUpdatedByUserName";
    public static final String CONTEXT = "@context";
    public static final String CURRENT_USER_PERMISSIONS = "currentUserPermissions";
    public static final String PATH_INFO = "pathInfo";
    public static final String IS_BASED_ON ="isBasedOn";
    public static final String NUMBER_OF_INSTANCES = "numberOfInstances";
    public static final String DERIVED_FROM = "derivedFrom";
    public static final String VERSIONS = "versions";
    public static final String ACTIVE_USER_CAN_READ = "activeUserCanRead";
  }

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