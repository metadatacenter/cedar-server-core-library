package org.metadatacenter.config;

public interface ServersConfig {

  ServerConfig getTemplate();

  FolderServerConfig getFolder();

  UserServerConfig getUser();

  ResourceServerConfig getResource();

  SchemaServerConfig getSchema();
}
