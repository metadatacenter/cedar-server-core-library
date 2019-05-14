package org.metadatacenter.constant;

import org.metadatacenter.model.ModelNodeNames;

public final class ElasticsearchConstants {


  private ElasticsearchConstants() {
  }

  private static final String ES_INFO_PREFIX = "info.";

  // Searchable fields
  public static final String DOCUMENT_CEDAR_ID = "cid";
  public static final String SUMMARY_TEXT = "summaryText";
  public static final String SUMMARY_RAW_TEXT = "summaryText.raw";
  public static final String USERS = "users";
  public static final String INFO_SCHEMA_NAME = ES_INFO_PREFIX + ModelNodeNames.SCHEMA_NAME;
  public static final String RESOURCE_TYPE = ES_INFO_PREFIX + "resourceType";
  public static final String INFO_IS_LATEST_VERSION = ES_INFO_PREFIX + "isLatestVersion";
  public static final String INFO_IS_LATEST_PUBLISHED_VERSION = ES_INFO_PREFIX + "isLatestPublishedVersion";
  public static final String INFO_IS_LATEST_DRAFT_VERSION = ES_INFO_PREFIX + "isLatestDraftVersion";
  public static final String INFO_BIBO_STATUS = ES_INFO_PREFIX + ModelNodeNames.BIBO_STATUS;
  public static final String INFO_PAV_LAST_UPDATED_ON = ES_INFO_PREFIX + ModelNodeNames.PAV_LAST_UPDATED_ON;
  public static final String INFO_PAV_CREATED_ON = ES_INFO_PREFIX + ModelNodeNames.PAV_CREATED_ON;
  public static final String INFO_IS_BASED_ON = ES_INFO_PREFIX + ModelNodeNames.SCHEMA_IS_BASED_ON;
  public static final String INFO_EVERYBODY_PERMISSION = ES_INFO_PREFIX + "everybodyPermission";
  public static final String INFO_FIELDS = "infoFields";
  public static final String INFO_FIELDS_FIELD_NAME = INFO_FIELDS + "." + "fieldName";
  public static final String INFO_FIELDS_FIELD_VALUE = INFO_FIELDS + "." + "fieldValue";

  // Sortable fields
  public static final String SORT_BY_NAME = "name";
  public static final String SORT_CREATED_ON_FIELD = "createdOnTS";
  public static final String SORT_LAST_UPDATED_ON_FIELD = "lastUpdatedOnTS";

  // Other
  public static final String ES_SORT_DESC_PREFIX = "-";
  public static final String TEMPLATE_ID = "templateId";
  public static final String ANY_STRING = "_any_";

}
