package org.metadatacenter.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class CedarConstants {

  private CedarConstants() {
  }

  //TODO: remove this, move the CedarModelVocabulary to a central place
  public static final String SCHEMA_IS_BASED_ON = "schema:isBasedOn";

  // JSON ID constants
  public static final String TEMP_ID_PREFIX = "tmp-";

  // Date formats
  public static final String xsdDateTimeFormatterString = "uuuu-MM-dd'T'HH:mm:ssZ";
  public static final DateTimeFormatter xsdDateTimeFormatter = DateTimeFormatter.ofPattern(xsdDateTimeFormatterString)
      .withZone(ZoneId.systemDefault());
}