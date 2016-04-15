package org.metadatacenter.provenance;

import com.fasterxml.jackson.annotation.JsonValue;

import java.lang.String;import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ProvenanceTime {

  private Instant value;
  private static final String PATTERN = "uuuu-MM-dd'T'HH:mm:ss'Z'";
  private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(PATTERN).withZone(ZoneOffset.UTC);

  public ProvenanceTime(String s) {
    if (s != null) {
      value = Instant.parse(s);
    }
  }

  public ProvenanceTime(Date date) {
    if (date != null) {
      value = date.toInstant();
    }
  }

  @JsonValue
  public String toString() {
    return dateFormatter.format(value);
  }

}
