package org.metadatacenter.model;

import org.metadatacenter.server.security.model.auth.NodePermission;

public enum RelationLabel {

  OWNS(PlainLabels.OWNS, null),
  CONTAINS(PlainLabels.CONTAINS, null),
  MEMBEROF(PlainLabels.MEMBEROF, null),
  CANREAD(PlainLabels.CANREAD, NodePermission.READ),
  CANWRITE(PlainLabels.CANWRITE, NodePermission.WRITE),
  ADMINISTERS(PlainLabels.ADMINISTERS, null),
  PREVIOUSVERSION(PlainLabels.PREVIOUSVERSION, null),
  DERIVEDFROM(PlainLabels.DERIVEDFROM, null),
  CONTAINSCATEGORY(PlainLabels.CONTAINSCATEGORY, null);

  public static class PlainLabels {
    public static final String OWNS = "OWNS";
    public static final String CONTAINS = "CONTAINS";
    public static final String MEMBEROF = "MEMBEROF";
    public static final String CANREAD = "CANREAD";
    public static final String CANWRITE = "CANWRITE";
    public static final String ADMINISTERS = "ADMINISTERS";
    public static final String PREVIOUSVERSION = "PREVIOUSVERSION";
    public static final String DERIVEDFROM = "DERIVEDFROM";
    public static final String CONTAINSCATEGORY = "CONTAINSCATEGORY";
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
