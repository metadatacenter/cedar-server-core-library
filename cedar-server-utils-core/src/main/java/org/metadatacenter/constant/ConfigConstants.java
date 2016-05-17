package org.metadatacenter.constant;

public final class ConfigConstants {

  private ConfigConstants() {
  }

  // application.conf keys
  private static String MONGODB = "mongodb.";
  private static String MONGODB_COLLECTIONS = MONGODB + "collections.";
  public static final String MONGODB_DATABASE_NAME = MONGODB + "db";
  public static final String TEMPLATES_COLLECTION_NAME = MONGODB_COLLECTIONS + "templates";
  public static final String TEMPLATE_ELEMENTS_COLLECTION_NAME = MONGODB_COLLECTIONS + "template-elements";
  public static final String TEMPLATE_FIELDS_COLLECTION_NAME = MONGODB_COLLECTIONS + "template-fields";
  public static final String TEMPLATE_INSTANCES_COLLECTION_NAME = MONGODB_COLLECTIONS + "template-instances";
  public static final String USERS_COLLECTION_NAME = MONGODB_COLLECTIONS + "users";

  private static String LD_IDPATH = "linkedData.idPath.";
  private static String LD_IDPATH_SUFFIX = LD_IDPATH + "suffix.";
  public static final String LINKED_DATA_ID_PATH_BASE = LD_IDPATH + "base";
  public static final String LINKED_DATA_ID_PATH_SUFFIX_TEMPLATES = LD_IDPATH_SUFFIX + "templates";
  public static final String LINKED_DATA_ID_PATH_SUFFIX_TEMPLATE_ELEMENTS = LD_IDPATH_SUFFIX + "template-elements";
  public static final String LINKED_DATA_ID_PATH_SUFFIX_TEMPLATE_FIELDS = LD_IDPATH_SUFFIX + "template-fields";
  public static final String LINKED_DATA_ID_PATH_SUFFIX_TEMPLATE_INSTANCES = LD_IDPATH_SUFFIX + "template-instances";
  public static final String LINKED_DATA_ID_PATH_SUFFIX_FOLDERS = LD_IDPATH_SUFFIX + "folders";

  public static final String FOLDER_SERVER_BASE = "folderServer.base";

  public static final String TEMPLATE_SERVER_BASE = "templateServer.base";

  public static final String USER_SERVER_BASE = "userServer.base";
  public static final String USER_DATA_ID_PATH_BASE = "userData.base";
  public static final String USER_ADMIN_USER_UUID = "userData.adminUserUUID";


  public static final String NEO4J_REST_TRANSACTION_URL = "neo4j.rest.transactionUrl";
  public static final String NEO4J_REST_AUTH_STRING = "neo4j.rest.authString";

  public static final String NEO4J_FOLDERS_ROOT_PATH = "folderStructure.rootFolder.path";
  public static final String NEO4J_FOLDERS_ROOT_DESCRIPTION = "folderStructure.rootFolder.description";
  public static final String NEO4J_FOLDERS_USERS_PATH = "folderStructure.usersFolder.path";
  public static final String NEO4J_FOLDERS_USERS_DESCRIPTION = "folderStructure.usersFolder.description";
  public static final String NEO4J_FOLDERS_LOSTANDFOUND_PATH = "folderStructure.lostAndFoundFolder.path";
  public static final String NEO4J_FOLDERS_LOSTANDFOUND_DESCRIPTION = "folderStructure.lostAndFoundFolder.description";

  private static String PAGINATION = "pagination.";
  public static final String PAGINATION_DEFAULT_PAGE_SIZE = PAGINATION + "defaultPageSize";
  public static final String PAGINATION_MAX_PAGE_SIZE = PAGINATION + "maxPageSize";

  private static String SUMMARY = "summary.";
  public static final String FIELD_NAMES_SUMMARY_TEMPLATE = SUMMARY + "template.fields";
  public static final String FIELD_NAMES_SUMMARY_TEMPLATE_ELEMENT = SUMMARY + "templateElement.fields";
  public static final String FIELD_NAMES_SUMMARY_TEMPLATE_FIELD = SUMMARY + "templateField.fields";
  public static final String FIELD_NAMES_SUMMARY_TEMPLATE_INSTANCE = SUMMARY + "templateInstance.fields";

  public static final String FIELD_NAMES_LIST_EXCLUSION = "list.excludedFields";

}
