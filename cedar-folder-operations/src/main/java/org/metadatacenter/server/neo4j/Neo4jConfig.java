package org.metadatacenter.server.neo4j;

import org.metadatacenter.config.CedarConfig;

public class Neo4jConfig {

  private String transactionUrl;
  private String authString;
  private String rootFolderPath;
  private String rootFolderDescription;
  private String usersFolderPath;
  private String usersFolderDescription;
  private String lostAndFoundFolderPath;
  private String lostAndFoundFolderDescription;
  private String everybodyGroupName;
  private String everybodyGroupDisplayName;

  public Neo4jConfig() {
  }

  public String getTransactionUrl() {
    return transactionUrl;
  }

  public void setTransactionUrl(String transactionUrl) {
    this.transactionUrl = transactionUrl;
  }

  public String getAuthString() {
    return authString;
  }

  public void setAuthString(String authString) {
    this.authString = authString;
  }

  public String getRootFolderPath() {
    return rootFolderPath;
  }

  public void setRootFolderPath(String rootFolderPath) {
    this.rootFolderPath = rootFolderPath;
  }

  public String getRootFolderDescription() {
    return rootFolderDescription;
  }

  public void setRootFolderDescription(String rootFolderDescription) {
    this.rootFolderDescription = rootFolderDescription;
  }

  public String getUsersFolderPath() {
    return usersFolderPath;
  }

  public void setUsersFolderPath(String usersFolderPath) {
    this.usersFolderPath = usersFolderPath;
  }

  public String getUsersFolderDescription() {
    return usersFolderDescription;
  }

  public void setUsersFolderDescription(String usersFolderDescription) {
    this.usersFolderDescription = usersFolderDescription;
  }

  public String getLostAndFoundFolderPath() {
    return lostAndFoundFolderPath;
  }

  public void setLostAndFoundFolderPath(String lostAndFoundFolderPath) {
    this.lostAndFoundFolderPath = lostAndFoundFolderPath;
  }

  public String getLostAndFoundFolderDescription() {
    return lostAndFoundFolderDescription;
  }

  public void setLostAndFoundFolderDescription(String lostAndFoundFolderDescription) {
    this.lostAndFoundFolderDescription = lostAndFoundFolderDescription;
  }

  public String getEverybodyGroupName() {
    return everybodyGroupName;
  }

  public void setEverybodyGroupName(String everybodyGroupName) {
    this.everybodyGroupName = everybodyGroupName;
  }

  public String getEverybodyGroupDisplayName() {
    return everybodyGroupDisplayName;
  }

  public void setEverybodyGroupDisplayName(String everybodyGroupDisplayName) {
    this.everybodyGroupDisplayName = everybodyGroupDisplayName;
  }

  public static Neo4jConfig fromCedarConfig(CedarConfig cedarConfig) {
    Neo4jConfig neoConfig = new Neo4jConfig();
    neoConfig.setTransactionUrl(cedarConfig.getNeo4jConfig().getRest().getTransactionUrl());
    neoConfig.setAuthString(cedarConfig.getNeo4jConfig().getRest().getAuthString());
    neoConfig.setRootFolderPath(cedarConfig.getFolderStructureConfig().getRootFolder().getPath());
    neoConfig.setRootFolderDescription(cedarConfig.getFolderStructureConfig().getRootFolder().getDescription());
    neoConfig.setUsersFolderPath(cedarConfig.getFolderStructureConfig().getUsersFolder().getPath());
    neoConfig.setUsersFolderDescription(cedarConfig.getFolderStructureConfig().getUsersFolder().getDescription());
    neoConfig.setLostAndFoundFolderPath(cedarConfig.getFolderStructureConfig().getLostAndFoundFolder().getPath());
    neoConfig.setLostAndFoundFolderDescription(cedarConfig.getFolderStructureConfig().getLostAndFoundFolder()
        .getDescription());
    neoConfig.setEverybodyGroupName(cedarConfig.getFolderStructureConfig().getEverybodyGroup().getName());
    neoConfig.setEverybodyGroupDisplayName(cedarConfig.getFolderStructureConfig().getEverybodyGroup().getDisplayName());
    return neoConfig;
  }
}
