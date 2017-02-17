package org.metadatacenter.constant;

public final class ElasticsearchConstants {

  private ElasticsearchConstants() {
  }

  private final static String ES = "elasticsearch.";
  public final static String ES_CLUSTER = ES + "cluster";
  public final static String ES_INDEX = ES + "index";
  public final static String ES_TYPE = ES + "type";
  public final static String ES_HOST = ES + "host";
  public final static String ES_TRANSPORT_PORT = ES + "transportPort";
  public final static String ES_SIZE = ES + "size";
  public final static String ES_SCROLL_KEEP_ALIVE = ES + "scrollKeepAlive";

  public static final String ES_RESOURCE_PREFIX = "info.";

  // Searchable fields
  public static final String ES_DOCUMENT_CEDAR_ID = "cid";
  public static final String ES_RESOURCE_ID_FIELD = "@id";
  public static final String ES_RESOURCE_NAME_FIELD = "name";
  public static final String ES_RESOURCE_DESCRIPTION_FIELD = "description";
  public static final String ES_RESOURCE_RESOURCETYPE_FIELD = "nodeType";

  public static final String ES_TEMPLATEID_FIELD = "templateId";

  // Sortable fields
  public static final String ES_RESOURCE_SORT_NAME_FIELD = "name";
  public static final String ES_RESOURCE_SORT_CREATEDONTS_FIELD = "createdOnTS";
  public static final String ES_RESOURCE_SORT_LASTUPDATEDONTS_FIELD = "lastUpdatedOnTS";

  // Fields used to check access permissions
  public static final String ES_RESOURCE_OWNER_FIELD = "ownedBy";

  // Other
  public static final String ES_SORT_DESC_PREFIX = "-";

}
