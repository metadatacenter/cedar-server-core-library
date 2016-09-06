package org.metadatacenter.server.neo4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FolderContentSortOptions {

  final static Map<String, FolderContentSortField> knownSortKeys;
  private static final FolderContentSortField DEFAULT_SORT_FIELD;

  static {
    knownSortKeys = new HashMap<>();
    DEFAULT_SORT_FIELD = new FolderContentSortField("name", true);
    addField(DEFAULT_SORT_FIELD);
    addField(new FolderContentSortField("createdOnTS", false));
    addField(new FolderContentSortField("lastUpdatedOnTS", false));
  }

  private static void addField(FolderContentSortField field) {
    knownSortKeys.put(field.getName(), field);
  }

  public static FolderContentSortField getDefaultSortField() {
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
}
