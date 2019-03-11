package org.metadatacenter.config;

import org.metadatacenter.model.ServerName;

public class ServersConfig {

  private ServerConfig group;

  private ServerConfig messaging;

  private ServerConfig repo;

  private ResourceServerConfig resource;

  private ServerConfig schema;

  private ServerConfig submission;

  private ServerConfig template;

  private ServerConfig terminology;

  private UserServerConfig user;

  private ServerConfig valuerecommender;

  private ServerConfig worker;

  private ServerConfig open;

  public ServerConfig getGroup() {
    return group;
  }

  public ServerConfig getMessaging() {
    return messaging;
  }

  public ServerConfig getRepo() {
    return repo;
  }

  public ResourceServerConfig getResource() {
    return resource;
  }

  public ServerConfig getSchema() {
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

  public ServerConfig getOpen() {
    return open;
  }

  public ServerConfig get(ServerName serverName) {
    switch (serverName) {
      case GROUP:
        return group;
      case MESSAGING:
        return messaging;
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
      case OPEN:
        return open;
      default:
        return null;
    }
  }
}
