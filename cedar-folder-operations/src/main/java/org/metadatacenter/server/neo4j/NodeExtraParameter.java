package org.metadatacenter.server.neo4j;

public enum NodeExtraParameter {

  IS_ROOT(Keys.IS_ROOT),
  IS_SYSTEM(Keys.IS_SYSTEM),
  IS_USER_HOME(Keys.IS_USER_HOME),
  OWNED_BY(Keys.OWNED_BY),
  IS_PUBLICLY_READABLE(Keys.IS_PUBLICLY_READABLE),
  IS_PUBLICLY_WRITABLE(Keys.IS_PUBLICLY_WRITABLE);

  public static class Keys {
    public static final String IS_ROOT = "isRoot";
    public static final String IS_SYSTEM = "isSystem";
    public static final String IS_USER_HOME = "isUserHome";
    public static final String OWNED_BY = "ownedBy";
    public static final String IS_PUBLICLY_READABLE = "isPubliclyReadable";
    public static final String IS_PUBLICLY_WRITABLE = "isPubliclyWritable";
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