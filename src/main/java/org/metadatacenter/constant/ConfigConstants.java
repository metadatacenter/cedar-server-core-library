package org.metadatacenter.constant;

public final class ConfigConstants {

  private ConfigConstants() {
  }

  // application.conf keys
  private static String MONGODB = "mongodb.";
  private static String MONGODB_COLLECTIONS = MONGODB + "collections.";
  public final static String MONGODB_DATABASE_NAME = MONGODB + "db";
  public final static String TEMPLATES_COLLECTION_NAME = MONGODB_COLLECTIONS + "templates";
  public final static String TEMPLATE_ELEMENTS_COLLECTION_NAME = MONGODB_COLLECTIONS + "template-elements";
  public final static String TEMPLATE_FIELDS_COLLECTION_NAME = MONGODB_COLLECTIONS + "template-fields";
  public final static String TEMPLATE_INSTANCES_COLLECTION_NAME = MONGODB_COLLECTIONS + "template-instances";

  private static String LD_IDPATH = "linkedData.idPath.";
  private static String LD_IDPATH_SUFFIX = LD_IDPATH + "suffix.";
  public final static String LINKED_DATA_ID_PATH_BASE = LD_IDPATH + "base";
  public final static String LINKED_DATA_ID_PATH_SUFFIX_TEMPLATES = LD_IDPATH_SUFFIX + "templates";
  public final static String LINKED_DATA_ID_PATH_SUFFIX_TEMPLATE_ELEMENTS = LD_IDPATH_SUFFIX + "template-elements";
  public final static String LINKED_DATA_ID_PATH_SUFFIX_TEMPLATE_FIELDS = LD_IDPATH_SUFFIX + "template-fields";
  public final static String LINKED_DATA_ID_PATH_SUFFIX_TEMPLATE_INSTANCES = LD_IDPATH_SUFFIX + "template-instances";

  private static String PAGINATION = "pagination.";
  public final static String PAGINATION_DEFAULT_PAGE_SIZE = PAGINATION + "defaultPageSize";
  public final static String PAGINATION_MAX_PAGE_SIZE = PAGINATION + "maxPageSize";

  private static String SUMMARY = "summary.";
  public final static String FIELD_NAMES_SUMMARY_TEMPLATE = SUMMARY + "template.fields";
  public final static String FIELD_NAMES_SUMMARY_TEMPLATE_ELEMENT = SUMMARY + "templateElement.fields";
  public final static String FIELD_NAMES_SUMMARY_TEMPLATE_FIELD = SUMMARY + "templateField.fields";
  public final static String FIELD_NAMES_SUMMARY_TEMPLATE_INSTANCE = SUMMARY + "templateInstance.fields";

  public final static String FIELD_NAMES_LIST_EXCLUSION = "list.excludedFields";

}
