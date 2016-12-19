package org.metadatacenter.config;

public interface BioPortal extends ServerConfig {

  String getApiKey();
  String getBasePath();
  int getConnectTimeout();
  int getSocketTimeout();
  int getDefaultPageSize();

}
