package org.metadatacenter.server.folder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QuerySortOptions {

  final static Map<String, QuerySortField> knownSortKeys;
  private static final QuerySortField DEFAULT_SORT_FIELD;

  static {
    knownSortKeys = new HashMap<>();
    DEFAULT_SORT_FIELD = new QuerySortField("name", "displayName", QuerySortFieldType.TEXTUAL);
    addField(DEFAULT_SORT_FIELD);
    addField(new QuerySortField("createdOnTS", "createdOnTS", QuerySortFieldType.NUMERIC));
    addField(new QuerySortField("lastUpdatedOnTS", "lastUpdatedOnTS", QuerySortFieldType.NUMERIC));
  }

  private static void addField(QuerySortField field) {
    knownSortKeys.put(field.getName(), field);
  }

  public static QuerySortField getDefaultSortField() {
    return DEFAULT_SORT_FIELD;
  }

  public static boolean isKnownField(String name) {
    return knownSortKeys.containsKey(name);
  }

  public static Set<String> getKnownFieldNames() {
    return knownSortKeys.keySet();
  }

  public static boolean isTextual(String name) {
    return knownSortKeys.get(name) != null && knownSortKeys.get(name).isTextual();
  }

  public static String getFieldName(String name) {
    return knownSortKeys.get(name) != null ? knownSortKeys.get(name).getFieldName() : name;
  }
}
