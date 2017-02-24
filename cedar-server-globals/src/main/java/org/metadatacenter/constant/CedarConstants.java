package org.metadatacenter.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class CedarConstants {

  private CedarConstants() {
  }

  // JSON Schema
  public static final String JSON_SCHEMA_URL = "http://json-schema.org/draft-04/schema#";

  public static final String SCHEMA_IS_BASED_ON = "schema:isBasedOn";

  // Query String parameter names
  public static final String PARAM_OFFSET = "offset";
  public static final String PARAM_LIMIT = "limit";
  public static final String PARAM_DEFAULT_LIMIT = "defaultLimit";
  public static final String PARAM_MAX_ALLOWED_LIMIT = "maxAllowedLimit";
  public static final String SEARCH_PREFIX = "search.";
  public static final String SEARCH_PARAM_DEFAULT_LIMIT = SEARCH_PREFIX + PARAM_DEFAULT_LIMIT;
  public static final String SEARCH_PARAM_MAX_ALLOWED_LIMIT = SEARCH_PREFIX + PARAM_MAX_ALLOWED_LIMIT;
  public static final String HTTP_HEADER_DEBUG = "CEDAR-Debug";

  // JSON ID constants
  public static final String TEMP_ID_PREFIX = "tmp-";

  // Date formats
  public static final String xsdDateTimeFormatterString = "uuuu-MM-dd'T'HH:mm:ssZ";
  public static final DateTimeFormatter xsdDateTimeFormatter = DateTimeFormatter.ofPattern(xsdDateTimeFormatterString)
      .withZone(ZoneId.systemDefault());
}