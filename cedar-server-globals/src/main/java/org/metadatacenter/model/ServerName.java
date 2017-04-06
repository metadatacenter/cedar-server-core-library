package org.metadatacenter.model;

public enum ServerName {

  FOLDER("folder"),
  GROUP("group"),
  REPO("repo"),
  RESOURCE("resource"),
  SCHEMA("schema"),
  SUBMISSION("submission"),
  TEMPLATE("template"),
  TERMINOLOGY("terminology"),
  USER("user"),
  VALUERECOMMENDER("valuerecommender"),
  WORKER("worker");

  private String name;

  ServerName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
