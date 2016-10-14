package org.metadatacenter.server.neo4j;

import org.metadatacenter.config.CedarConfig;

public class Neo4jConfig {

  private String transactionUrl;
  private String authString;
  private String rootFolderPath;
  private String rootFolderDescription;
  private String usersFolderPath;
  private String usersFolderDescription;
  private String everybodyGroupName;
  private String everybodyGroupDisplayName;
  private String everybodyGroupDescription;

  private Neo4jConfig() {
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
    Neo4jConfig neoConfig = new Neo4jConfig();
    neoConfig.transactionUrl = cedarConfig.getNeo4jConfig().getRest().getTransactionUrl();
    neoConfig.authString = cedarConfig.getNeo4jConfig().getRest().getAuthString();
    neoConfig.rootFolderPath = cedarConfig.getFolderStructureConfig().getRootFolder().getPath();
    neoConfig.rootFolderDescription = cedarConfig.getFolderStructureConfig().getRootFolder().getDescription();
    neoConfig.usersFolderPath = cedarConfig.getFolderStructureConfig().getUsersFolder().getPath();
    neoConfig.usersFolderDescription = cedarConfig.getFolderStructureConfig().getUsersFolder().getDescription();
    neoConfig.everybodyGroupName = cedarConfig.getFolderStructureConfig().getEverybodyGroup().getName();
    neoConfig.everybodyGroupDisplayName = cedarConfig.getFolderStructureConfig().getEverybodyGroup().getDisplayName();
    neoConfig.everybodyGroupDescription = cedarConfig.getFolderStructureConfig().getEverybodyGroup().getDescription();
    return neoConfig;
  }
}
