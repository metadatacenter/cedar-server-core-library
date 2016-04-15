package org.metadatacenter.constant;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public final class CedarConstants {

  private CedarConstants() {
  }

  // JSON Schema
  public static final String JSON_SCHEMA_URL = "http://json-schema.org/draft-04/schema#";

  // Query String parameter names
  public static final String PARAM_OFFSET = "offset";
  public static final String PARAM_LIMIT = "limit";

  // JSON ID constants
  public static final String TEMP_ID_PREFIX = "tmp-";

  // Date formats
  public static String xsdDateTimeFormatString = "yyyy-MM-dd\'T\'HH:mm:ss\'Z\'";
  public static DateFormat xsdDateTimeFormat = new SimpleDateFormat(xsdDateTimeFormatString);
  public static String xsdDateTimeFormatterString = "uuuu-MM-dd\'T\'HH:mm:ss\'Z\'";
  public static DateTimeFormatter xsdDateTimeFormatter = DateTimeFormatter.ofPattern(xsdDateTimeFormatterString)
      .withZone(ZoneOffset.UTC);
  ;

}