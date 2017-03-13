package org.metadatacenter.model.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OutputFormatType {

  JSONLD("jsonld"),
  JSON("json"),
  RDF_NQUAD("rdf-nquad"),
  UNKNOWN("unknown");

  private final String value;

  OutputFormatType(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @JsonCreator
  public static OutputFormatType forValue(String value) {
    for (OutputFormatType t : values()) {
      if (t.getValue().equals(value)) {
        return t;
      }
    }
    return UNKNOWN;
  }
}
