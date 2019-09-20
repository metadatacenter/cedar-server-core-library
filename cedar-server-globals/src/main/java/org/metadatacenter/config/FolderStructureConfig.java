package org.metadatacenter.config;

public class FolderStructureConfig {

  private GraphFolder rootFolder;

  private GraphFolder usersFolder;

  private GraphGroup everybodyGroup;

  private GraphCategory rootCategory;

  public GraphFolder getRootFolder() {
    return rootFolder;
  }

  public GraphFolder getUsersFolder() {
    return usersFolder;
  }

  public GraphGroup getEverybodyGroup() {
    return everybodyGroup;
  }

  public GraphCategory getRootCategory() {
    return rootCategory;
  }
}
