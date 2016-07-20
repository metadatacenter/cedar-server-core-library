package org.metadatacenter.server.neo4j;

public enum RelationLabel {

  OWNS(PlainLabels.OWNS),
  CONTAINS(PlainLabels.CONTAINS);

  public static class PlainLabels {
    public static final String OWNS = "OWNS";
    public static final String CONTAINS = "CONTAINS";
  }

  private final String value;

  RelationLabel(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static RelationLabel forValue(String type) {
    for (RelationLabel t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return value;
  }
}