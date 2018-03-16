package org.metadatacenter.constant;

public final class ElasticsearchConstants {

  private ElasticsearchConstants() {
  }

  public static final String ES_RESOURCE_PREFIX = "info.";

  // Searchable fields
  public static final String ES_DOCUMENT_CEDAR_ID = "cid";
  public static final String ES_RESOURCE_NAME_FIELD = "schema:name";
  public static final String ES_RESOURCE_RESOURCETYPE_FIELD = "nodeType";

  public static final String ES_TEMPLATEID_FIELD = "templateId";

  // Sortable fields
  public static final String ES_RESOURCE_SORT_NAME_FIELD = "name";
  public static final String ES_RESOURCE_SORT_CREATEDONTS_FIELD = "createdOnTS";
  public static final String ES_RESOURCE_SORT_LASTUPDATEDONTS_FIELD = "lastUpdatedOnTS";

  // Other
  public static final String ES_SORT_DESC_PREFIX = "-";

}
