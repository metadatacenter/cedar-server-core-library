package org.metadatacenter.server.security.model.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum CedarUserRole {
  TEMPLATE_CREATOR("templateCreator"),
  METADATA_CREATOR("metadataCreator"),
  USER_ADMINISTRATOR("userAdministrator"),
  GROUP_ADMINISTRATOR("groupAdministrator"),
  FILESYSTEM_ADMINISTRATOR("filesystemAdministrator"),
  CATEGORY_ADMINISTRATOR("categoryAdministrator"),
  CATEGORY_PRIVILEGED_ADMINISTRATOR("categoryPrivilegedAdministrator"),
  SEARCH_REINDEXER("searchReindexer"),
  BUILT_IN_SYSTEM_ADMINISTRATOR("builtInSystemAdministrator");

  private final String value;

  CedarUserRole(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static CedarUserRole forValue(String value) {
    for (CedarUserRole t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return null;
  }

}
