package org.metadatacenter.model;

public enum ServerName {

  GROUP("group"),
  MESSAGING("messaging"),
  REPO("repo"),
  RESOURCE("resource"),
  SCHEMA("schema"),
  SUBMISSION("submission"),
  TEMPLATE("template"),
  TERMINOLOGY("terminology"),
  USER("user"),
  VALUERECOMMENDER("valuerecommender"),
  WORKER("worker"),
  OPEN("open");

  private String name;

  ServerName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }
}
