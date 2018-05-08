package org.metadatacenter.server.neo4j;

import org.metadatacenter.config.CedarConfig;

public class Neo4jConfig {

  private final String transactionUrl;
  private final String authString;
  private final String rootFolderPath;
  private final String rootFolderDescription;
  private final String usersFolderPath;
  private final String usersFolderDescription;
  private final String everybodyGroupName;
  private final String everybodyGroupDisplayName;
  private final String everybodyGroupDescription;

  private final String uri;
  private final String userName;
  private final String userPassword;


  private Neo4jConfig(CedarConfig cedarConfig) {
    this.transactionUrl = cedarConfig.getNeo4jConfig().getRest().getTransactionUrl();
    this.authString = cedarConfig.getNeo4jConfig().getRest().getAuthString();
    this.rootFolderPath = cedarConfig.getFolderStructureConfig().getRootFolder().getPath();
    this.rootFolderDescription = cedarConfig.getFolderStructureConfig().getRootFolder().getDescription();
    this.usersFolderPath = cedarConfig.getFolderStructureConfig().getUsersFolder().getPath();
    this.usersFolderDescription = cedarConfig.getFolderStructureConfig().getUsersFolder().getDescription();
    this.everybodyGroupName = cedarConfig.getFolderStructureConfig().getEverybodyGroup().getName();
    this.everybodyGroupDisplayName = cedarConfig.getFolderStructureConfig().getEverybodyGroup().getDisplayName();
    this.everybodyGroupDescription = cedarConfig.getFolderStructureConfig().getEverybodyGroup().getDescription();
    this.uri = cedarConfig.getNeo4jConfig().getBolt().getUri();
    this.userName = cedarConfig.getNeo4jConfig().getBolt().getUserName();
    this.userPassword = cedarConfig.getNeo4jConfig().getBolt().getUserPassword();
  }

  public String getTransactionUrl() {
    return transactionUrl;
  }

  public String getAuthString() {
    return authString;
  }

  public String getRootFolderPath() {
    return rootFolderPath;
  }

  public String getRootFolderDescription() {
    return rootFolderDescription;
  }

  public String getUsersFolderPath() {
    return usersFolderPath;
  }

  public String getUsersFolderDescription() {
    return usersFolderDescription;
  }

  public String getEverybodyGroupName() {
    return everybodyGroupName;
  }

  public String getEverybodyGroupDisplayName() {
    return everybodyGroupDisplayName;
  }

  public String getEverybodyGroupDescription() {
    return everybodyGroupDescription;
  }

  public static Neo4jConfig fromCedarConfig(CedarConfig cedarConfig) {
    return new Neo4jConfig(cedarConfig);
  }

  public String getUri() {
    return uri;
  }

  public String getUserName() {
    return userName;
  }

  public String getUserPassword() {
    return userPassword;
  }
}
