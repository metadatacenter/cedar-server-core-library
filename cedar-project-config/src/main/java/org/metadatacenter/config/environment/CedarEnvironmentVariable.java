package org.metadatacenter.config.environment;

public enum CedarEnvironmentVariable {

  CEDAR_VERSION("CEDAR_VERSION"),
  CEDAR_VERSION_MODIFIER("CEDAR_VERSION_MODIFIER"),

  CEDAR_HOME("CEDAR_HOME"),
  KEYCLOAK_HOME("KEYCLOAK_HOME"),
  NGINX_HOME("NGINX_HOME"),

  CEDAR_FRONTEND_BEHAVIOR("CEDAR_FRONTEND_BEHAVIOR"),
  CEDAR_FRONTEND_TARGET("CEDAR_FRONTEND_TARGET"),
  CEDAR_HOST("CEDAR_HOST"),

  CEDAR_BIOPORTAL_API_KEY("CEDAR_BIOPORTAL_API_KEY", CedarEnvironmentVariableSecure.YES),
  CEDAR_BIOPORTAL_REST_BASE("CEDAR_BIOPORTAL_REST_BASE"),

  CEDAR_ANALYTICS_KEY("CEDAR_ANALYTICS_KEY", CedarEnvironmentVariableSecure.YES),
  CEDAR_NCBI_SRA_FTP_PASSWORD("CEDAR_NCBI_SRA_FTP_PASSWORD", CedarEnvironmentVariableSecure.YES),

  CEDAR_ADMIN_USER_PASSWORD("CEDAR_ADMIN_USER_PASSWORD", CedarEnvironmentVariableSecure.YES),
  CEDAR_ADMIN_USER_API_KEY("CEDAR_ADMIN_USER_API_KEY", CedarEnvironmentVariableSecure.YES),

  CEDAR_NEO4J_HOST("CEDAR_NEO4J_HOST"),
  CEDAR_NEO4J_REST_PORT("CEDAR_NEO4J_REST_PORT"),
  CEDAR_NEO4J_USER_NAME("CEDAR_NEO4J_USER_NAME"),
  CEDAR_NEO4J_USER_PASSWORD("CEDAR_NEO4J_USER_PASSWORD", CedarEnvironmentVariableSecure.YES),

  CEDAR_RESOURCE_SERVER_USER_CALLBACK_URL("CEDAR_RESOURCE_SERVER_USER_CALLBACK_URL"),
  CEDAR_RESOURCE_SERVER_ADMIN_CALLBACK_URL("CEDAR_RESOURCE_SERVER_ADMIN_CALLBACK_URL"),
  CEDAR_KEYCLOAK_CLIENT_ID("CEDAR_KEYCLOAK_CLIENT_ID"),

  CEDAR_MONGO_APP_USER_NAME("CEDAR_MONGO_APP_USER_NAME"),
  CEDAR_MONGO_APP_USER_PASSWORD("CEDAR_MONGO_APP_USER_PASSWORD", CedarEnvironmentVariableSecure.YES),
  CEDAR_MONGO_HOST("CEDAR_MONGO_HOST"),
  CEDAR_MONGO_PORT("CEDAR_MONGO_PORT", CedarEnvironmentVariableNumeric.YES),

  CEDAR_ELASTICSEARCH_HOST("CEDAR_ELASTICSEARCH_HOST"),
  CEDAR_ELASTICSEARCH_TRANSPORT_PORT("CEDAR_ELASTICSEARCH_TRANSPORT_PORT", CedarEnvironmentVariableNumeric.YES),

  CEDAR_SALT_API_KEY("CEDAR_SALT_API_KEY", CedarEnvironmentVariableSecure.YES),

  CEDAR_LD_USER_BASE("CEDAR_LD_USER_BASE"),

  CEDAR_EVERYBODY_GROUP_NAME("CEDAR_EVERYBODY_GROUP_NAME"),

  CEDAR_TEST_USER1_ID("CEDAR_TEST_USER1_ID"),
  CEDAR_TEST_USER2_ID("CEDAR_TEST_USER2_ID"),

  CEDAR_REDIS_PERSISTENT_HOST("CEDAR_REDIS_PERSISTENT_HOST"),
  CEDAR_REDIS_PERSISTENT_PORT("CEDAR_REDIS_PERSISTENT_PORT", CedarEnvironmentVariableNumeric.YES),
  CEDAR_REDIS_NONPERSISTENT_HOST("CEDAR_REDIS_NONPERSISTENT_HOST"),
  CEDAR_REDIS_NONPERSISTENT_PORT("CEDAR_REDIS_NONPERSISTENT_PORT", CedarEnvironmentVariableNumeric.YES),

  CEDAR_PORT_FOLDER("CEDAR_PORT_FOLDER", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_GROUP("CEDAR_PORT_GROUP", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_REPO("CEDAR_PORT_REPO", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_RESOURCE("CEDAR_PORT_RESOURCE", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_SCHEMA("CEDAR_PORT_SCHEMA", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_SUBMISSION("CEDAR_PORT_SUBMISSION", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_TEMPLATE("CEDAR_PORT_TEMPLATE", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_TERMINOLOGY("CEDAR_PORT_TERMINOLOGY", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_USER("CEDAR_PORT_USER", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_VALUERECOMMENDER("CEDAR_PORT_VALUERECOMMENDER", CedarEnvironmentVariableNumeric.YES),
  CEDAR_PORT_WORKER("CEDAR_PORT_WORKER", CedarEnvironmentVariableNumeric.YES);

  private final String name;
  private final CedarEnvironmentVariableNumeric numeric;
  private final CedarEnvironmentVariableSecure secure;

  CedarEnvironmentVariable(String name) {
    this.name = name;
    this.numeric = CedarEnvironmentVariableNumeric.NO;
    this.secure = CedarEnvironmentVariableSecure.NO;
  }

  CedarEnvironmentVariable(String name, CedarEnvironmentVariableNumeric numeric) {
    this.name = name;
    this.numeric = numeric;
    this.secure = CedarEnvironmentVariableSecure.NO;
  }

  CedarEnvironmentVariable(String name, CedarEnvironmentVariableSecure secure) {
    this.name = name;
    this.numeric = CedarEnvironmentVariableNumeric.NO;
    this.secure = secure;
  }

  public String getName() {
    return name;
  }

  public CedarEnvironmentVariableNumeric getNumeric() {
    return numeric;
  }

  public CedarEnvironmentVariableSecure getSecure() {
    return secure;
  }

  public static CedarEnvironmentVariable forName(String name) {
    for(CedarEnvironmentVariable ev : values()) {
      if (ev.getName().equals(name)) {
        return ev;
      }
    }
    return null;
  }
}
