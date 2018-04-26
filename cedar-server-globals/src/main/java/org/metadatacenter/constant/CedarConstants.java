package org.metadatacenter.constant;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class CedarConstants {

  private CedarConstants() {
  }

  // Date formats
  public static final String xsdDateTimeFormatterString = "uuuu-MM-dd'T'HH:mm:ssZ";
  public static final DateTimeFormatter xsdDateTimeFormatter = DateTimeFormatter.ofPattern(xsdDateTimeFormatterString)
      .withZone(ZoneId.systemDefault());
}