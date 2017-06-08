package org.metadatacenter.config;

public class ServerConfig {

  private int httpPort;

  private int adminPort;

  private int stopPort;

  private String base;

  private boolean apiDoc;

  public int getHttpPort() {
    return httpPort;
  }

  public int getAdminPort() {
    return adminPort;
  }

  public int getStopPort() {
    return stopPort;
  }

  public String getBase() {
    return base;
  }

  public boolean isApiDoc() {
    return apiDoc;
  }
}
