package org.metadatacenter.server.neo4j;

public enum NodeExtraParameter {

  IS_ROOT(Keys.IS_ROOT),
  IS_SYSTEM(Keys.IS_SYSTEM),
  IS_USER_HOME(Keys.IS_USER_HOME);

  public static class Keys {
    public static final String IS_ROOT = "isRoot";
    public static final String IS_SYSTEM = "isSystem";
    public static final String IS_USER_HOME = "isUserHome";
  }

  private final String value;

  NodeExtraParameter(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public static NodeExtraParameter forValue(String type) {
    for (NodeExtraParameter t : values()) {
      if (t.getValue().equals(type)) {
        return t;
      }
    }
    return null;
  }
}