package org.metadatacenter.config;

public class Neo4JConfig {

  private Neo4JRestConfig rest;

  private Neo4JBoltConfig bolt;

  public Neo4JRestConfig getRest() {
    return rest;
  }

  public Neo4JBoltConfig getBolt() {
    return bolt;
  }
}
