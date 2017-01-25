package org.metadatacenter.config;

public class ServersConfig {

  private ServerConfig template;

  private FolderServerConfig folder;

  private UserServerConfig user;

  private ResourceServerConfig resource;

  private SchemaServerConfig schema;

  public ServerConfig getTemplate() {
    return template;
  }

  public FolderServerConfig getFolder() {
    return folder;
  }

  public UserServerConfig getUser() {
    return user;
  }

  public ResourceServerConfig getResource() {
    return resource;
  }

  public SchemaServerConfig getSchema() {
    return schema;
  }
}
