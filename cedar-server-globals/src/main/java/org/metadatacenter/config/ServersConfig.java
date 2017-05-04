package org.metadatacenter.config;

import org.metadatacenter.model.ServerName;

public class ServersConfig {

  private FolderServerConfig folder;

  private ServerConfig group;

  private ServerConfig repo;

  private ResourceServerConfig resource;

  private SchemaServerConfig schema;

  private ServerConfig submission;

  private ServerConfig template;

  private ServerConfig terminology;

  private UserServerConfig user;

  private ServerConfig valuerecommender;

  private ServerConfig worker;

  public FolderServerConfig getFolder() {
    return folder;
  }

  public ServerConfig getGroup() {
    return group;
  }

  public ServerConfig getRepo() {
    return repo;
  }

  public ResourceServerConfig getResource() {
    return resource;
  }

  public SchemaServerConfig getSchema() {
    return schema;
  }

  public ServerConfig getSubmission() {
    return submission;
  }

  public ServerConfig getTemplate() {
    return template;
  }

  public ServerConfig getTerminology() {
    return terminology;
  }

  public UserServerConfig getUser() {
    return user;
  }

  public ServerConfig getValuerecommender() {
    return valuerecommender;
  }

  public ServerConfig getWorker() {
    return worker;
  }

  public ServerConfig get(ServerName serverName) {
    switch (serverName) {
      case FOLDER:
        return folder;
      case GROUP:
        return group;
      case REPO:
        return repo;
      case RESOURCE:
        return resource;
      case SCHEMA:
        return schema;
      case SUBMISSION:
        return submission;
      case TEMPLATE:
        return template;
      case TERMINOLOGY:
        return terminology;
      case USER:
        return user;
      case VALUERECOMMENDER:
        return valuerecommender;
      case WORKER:
        return worker;
      default:
        return null;
    }
  }
}
