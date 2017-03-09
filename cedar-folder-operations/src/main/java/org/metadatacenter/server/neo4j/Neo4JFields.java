package org.metadatacenter.server.neo4j;

public class Neo4JFields {

  // CEDAR fields
  public static final String LAST_UPDATED_ON_TS = "lastUpdatedOnTS";
  public static final String LAST_UPDATED_ON = "lastUpdatedOn";
  public static final String LAST_UPDATED_BY = "lastUpdatedBy";
  public static final String CREATED_ON_TS = "createdOnTS";
  public static final String CREATED_ON = "createdOn";
  public static final String CREATED_BY = "createdBy";
  public static final String DESCRIPTION = "description";
  public static final String OWNED_BY = "ownedBy";
  public static final String NAME = "name";
  public static final String DISPLAY_NAME = "displayName";
  public static final String FIRST_NAME = "firstName";
  public static final String LAST_NAME = "lastName";
  public static final String EMAIL = "email";
  public static final String ID = "id";
  public static final String FOLDER_ID = "folderId";
  public static final String PARENT_FOLDER_ID = "parentFolderId";
  public static final String RESOURCE_ID = "resourceId";
  public static final String NODE_ID = "nodeId";
  public static final String GROUP_ID = "groupId";
  public static final String PARENT_ID = "parentId";
  public static final String NODE_TYPE = "nodeType";
  public static final String FOLDER_ALIAS_PREFIX = "f";
  public static final String USER_ID = "userId";
  public static final String SPECIAL_GROUP = "specialGroup";
  public static final String IS_ROOT = "isRoot";
  public static final String IS_SYSTEM = "isSystem";
  public static final String IS_USER_HOME = "isUserHome";
  public static final String NODE_SORT_ORDER = "nodeSortOrder";
  public static final String HOME_OF = "homeOf";
  public static final String FROM_ID = "fromId";
  public static final String TO_ID = "toId";

  // CEDAR collections
  public static final String NODE_TYPE_LIST = "nodeTypeList";

  // Language related
  public static final String LIMIT = "limit";
  public static final String OFFSET = "offset";

  private Neo4JFields() {

  }
}
