package org.metadatacenter.server.neo4j;

import org.metadatacenter.server.security.model.auth.NodePermission;

public enum RelationLabel {

  OWNS(PlainLabels.OWNS, null),
  CONTAINS(PlainLabels.CONTAINS, null),
  MEMBEROF(PlainLabels.MEMBEROF, null),
  CANREAD(PlainLabels.CANREAD, NodePermission.READ),
  CANREADTHIS(PlainLabels.CANREADTHIS, NodePermission.READTHIS),
  CANWRITE(PlainLabels.CANWRITE, NodePermission.WRITE),
  OWNSGROUP(PlainLabels.OWNSGROUP, null);

  public static class PlainLabels {
    public static final String OWNS = "OWNS";
    public static final String CONTAINS = "CONTAINS";
    public static final String MEMBEROF = "MEMBEROF";
    public static final String CANREAD = "CANREAD";
    public static final String CANREADTHIS = "CANREADTHIS";
    public static final String CANWRITE = "CANWRITE";
    public static final String OWNSGROUP = "OWNSGROUP";
  }

  private final String value;
  private final NodePermission nodePermission;

  RelationLabel(String value, NodePermission nodePermission) {
    this.value = value;
    this.nodePermission = nodePermission;
  }

  public String getValue() {
    return value;
  }

  public NodePermission getNodePermission() {
    return nodePermission;
  }

  public static RelationLabel forValue(String type) {
    for (RelationLabel t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }

  public static RelationLabel forNodePermission(NodePermission permission) {
    if (permission != null) {
      for (RelationLabel t : values()) {
        if (permission.equals(t.getNodePermission())) {
          return t;
        }
      }
    }
    return null;
  }

  @Override
  public String toString() {
    return value;
  }
}