package org.metadatacenter.config;

public class FolderStructureConfig {

  private GraphFolder rootFolder;

  private GraphFolder usersFolder;

  private GraphGroup everybodyGroup;

  public GraphFolder getRootFolder() {
    return rootFolder;
  }

  public GraphFolder getUsersFolder() {
    return usersFolder;
  }

  public GraphGroup getEverybodyGroup() {
    return everybodyGroup;
  }
}
