package org.metadatacenter.config;

public interface ServersConfig {

  ServerConfig getTemplate();

  ServerConfig getFolder();

  UserServerConfig getUser();

  ResourceServerConfig getResource();
}
